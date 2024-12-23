// SPDX-License-Identifier: MIT
pragma solidity ^0.8.7;

import "./NFTFrame.sol";
import "./AhoraCoin.sol";
import "@openzeppelin/contracts/access/Ownable.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";

// Base contract with common functionality
contract TradeBase is Ownable, ReentrancyGuard {
    NFTFrame public nftContract;
    AhoraCoin public tokenContract;

    // 사용자별 잔액을 추적하는 매핑
    mapping(address => uint256) private userBalances;

    struct TradeInfo {
        address from;
        address to;
        uint256 amount;
        uint256 timestamp;
    }

    struct TradeResult {
        TradeInfo[] trades;
        uint256 totalTrades;
        bool hasNext;
    }

    // 전체 거래 기록을 Id로 매핑해서 저장
    mapping(uint256 => TradeInfo) public trades;
    uint256 public tradeId;

    // 주소별 거래 ID를 저장하는 매핑 추가
    mapping(address => uint256[]) private userTrades;

    event TradeRecorded(uint256 indexed tradeId, address from, address to, uint256 amount, uint256 timestamp, uint256 fromBalance, uint256 toBalance);
    event Deposit(address indexed user, uint256 amount, uint256 coinBalance, uint256 payableBalance);
    event Withdrawal(address indexed user, uint256 amount, uint256 coinBalance, uint256 payableBalance);
    event BalanceAdjusted(address indexed from, address indexed to, uint256 amount, uint256 fromBalance, uint256 toBalance);
    event BalanceTransferred(address indexed from, address indexed to, uint256 amount, uint256 fromBalance, uint256 toBalance);

    // 초기설정
    function initializeContracts(address _nftContractAddress, address _tokenContractAddress) internal {
        require(address(nftContract) == address(0) && address(tokenContract) == address(0), "Contracts already initialized");
        nftContract = NFTFrame(_nftContractAddress);
        tokenContract = AhoraCoin(_tokenContractAddress);
    }

    // 거래기록 남기기
    function recordTrade(address from, address to, uint256 amount) internal {
        tradeId++;
        trades[tradeId] = TradeInfo(from, to, amount, block.timestamp);
        
        // 발신자와 수신자의 거래 ID 기록
        userTrades[from].push(tradeId);
        userTrades[to].push(tradeId);
        
        emit TradeRecorded(tradeId, from, to, amount, block.timestamp, userBalances[from], userBalances[to]);
    }

    // 특정 주소의 거래 기록을 조회하는 함수
    function getTradesByAddress(address user, uint256 page, uint256 size) public view returns (TradeResult memory) {
        uint256[] storage userTradeIds = userTrades[user];
        uint256 totalUserTrades = userTradeIds.length;
        
        uint256 startIndex = totalUserTrades > page * size ? totalUserTrades - (page + 1) * size : 0;
        uint256 endIndex = totalUserTrades - page * size;
        
        // 첫 페이지의 경우 startIndex 조정
        if (startIndex > totalUserTrades || startIndex < 0) {
            startIndex = 0;
        }
        
        // 페이지가 범위를 벗어나면 빈 배열 반환
        if (endIndex <= 0 || page * size >= totalUserTrades) {
            return TradeResult(new TradeInfo[](0), totalUserTrades, false);
        }
        
        uint256 resultLength = endIndex - startIndex;
        TradeInfo[] memory userTradeInfos = new TradeInfo[](resultLength);
        
        for (uint256 i = 0; i < resultLength; i++) {
            userTradeInfos[i] = trades[userTradeIds[endIndex - 1 - i]];
        }
        
        bool hasNext = startIndex > 0;

        return TradeResult(userTradeInfos, totalUserTrades, hasNext);
    }

    // 특정 tradeId의 trade 정보를 단일 조회하는 함수
    function getTradeInfo(uint256 _tradeId) public view returns (TradeInfo memory) {
        require(_tradeId > 0 && _tradeId <= tradeId, "Invalid trade ID");
        return trades[_tradeId];
    }
    
    // 입금 함수
    function deposit(uint256 amount) external nonReentrant {
        require(amount > 0, "Deposit amount must be greater than 0");
        require(tokenContract.transferCoinByAdmin(msg.sender, address(this), amount), "Transfer failed");
        
        userBalances[msg.sender] += amount;
        recordTrade(address(0), msg.sender, amount);
        emit Deposit(msg.sender, amount, tokenContract.balanceOf(msg.sender), userBalances[msg.sender]);
    }

    // 출금 함수
    function withdraw(uint256 amount) external nonReentrant {
        require(amount > 0, "Withdrawal amount must be greater than 0");
        require(userBalances[msg.sender] >= amount, "Insufficient balance");
        
        require(tokenContract.transferCoinByAdmin(address(this), msg.sender, amount), "Transfer failed");
        userBalances[msg.sender] -= amount;
        
        recordTrade(msg.sender, address(0), amount);
        emit Withdrawal(msg.sender, amount, tokenContract.balanceOf(msg.sender), userBalances[msg.sender]);
    }

    // 잔액 조정
    function _adjustBalances(address from, address to, uint256 amount) internal {
        require(userBalances[from] >= amount, "Insufficient balance");
        userBalances[from] -= amount;
        userBalances[to] += amount;
        recordTrade(from, to, amount);
        emit BalanceAdjusted(from, to, amount, userBalances[from], userBalances[to]);
    }

    // 유저 간 잔액 이동
    function transferBalanceByAdmin(address from, address to, uint256 amount) external onlyOwner nonReentrant {
        require(to != address(0), "Invalid recipient address");
        require(to != from, "Cannot transfer to yourself");
        require(amount > 0, "Transfer amount must be greater than 0");
        require(userBalances[from] >= amount, "Insufficient balance");

        userBalances[from] -= amount;
        userBalances[to] += amount;

        recordTrade(from, to, amount);
        emit BalanceTransferred(from, to, amount, userBalances[from], userBalances[to]);
    }

    // 유저 간 잔액 이동
    function transferBalance(address to, uint256 amount) external nonReentrant {
        require(to != address(0), "Invalid recipient address");
        require(to != msg.sender, "Cannot transfer to yourself");
        require(amount > 0, "Transfer amount must be greater than 0");
        require(userBalances[msg.sender] >= amount, "Insufficient balance");

        userBalances[msg.sender] -= amount;
        userBalances[to] += amount;

        recordTrade(msg.sender, to, amount);
        emit BalanceTransferred(msg.sender, to, amount, userBalances[msg.sender], userBalances[to]);
    }

    // 컨트랙트 보유 NFT
    function getContractOwnedNFTs() public view returns (uint256[] memory) {
        return nftContract.getUserNFTIds(address(this)); 
    }

    // 컨트랙트 보유 잔액
    function getContractBalance() public view returns (uint256) {
        return tokenContract.balanceOf(address(this));
    }

    // 유저 거래가능 잔액
    function getUserBalance(address user) public view returns (uint256) {
        return userBalances[user];
    }
}