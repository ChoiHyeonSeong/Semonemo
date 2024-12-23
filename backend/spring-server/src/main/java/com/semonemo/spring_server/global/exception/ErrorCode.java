package com.semonemo.spring_server.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Server
	INTERNAL_SERVER_ERROR("S001", "서버 내부 오류", HttpStatus.INTERNAL_SERVER_ERROR),
	RESOURCE_NOT_FOUND_ERROR("S002", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	BAD_REQUEST_ERROR("S003", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
	METHOD_NOT_ALLOWED_ERROR("S004", "허용되지 않은 HTTP 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),
	INVALID_CREDENTIALS_ERROR("S005", "잘못된 인증 정보입니다.", HttpStatus.UNAUTHORIZED),
	INSUFFICIENT_AUTHENTICATION_ERROR("S006","인증 정보가 부족합니다.", HttpStatus.UNAUTHORIZED),

	// Auth
	INVALID_USER_DATA_ERROR("AU001", "유효하지 않은 값이 입력되었습니다.", HttpStatus.BAD_REQUEST),
	INVALID_NICKNAME_ERROR("AU002", "유효하지 않은 닉네임 형식입니다.", HttpStatus.BAD_REQUEST),
	AUTHENTICATION_FAIL_ERROR("AU003", "사용자 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
	EXPIRED_TOKEN_ERROR("AU004", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
	INVALID_TOKEN_ERROR("AU005", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
	EXISTS_ADDRESS_ERROR("AU006", "이미 존재하는 주소입니다.", HttpStatus.BAD_REQUEST),

	// User
	USER_NOT_FOUND_ERROR("U001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	SELF_FOLLOW_ERROR("U002", "자기 자신은 팔로우할 수 없습니다.", HttpStatus.BAD_REQUEST),
	ALREADY_FOLLOW_ERROR("U003", "이미 팔로우한 사용자입니다.", HttpStatus.BAD_REQUEST),
	NOT_FOLLOW_ERROR("U004", "팔로우 목록에 없는 사용자입니다.", HttpStatus.NOT_FOUND),
	CHECK_SELF_FOLLOW_ERROR("U005", "자기 자신은 확인할 수 없습니다.", HttpStatus.BAD_REQUEST),

	// NFT
	NFT_NOT_FOUND_ERROR("N001", "NFT를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	USER_NFT_NOT_FOUND_ERROR("N002", "유저 NFT 조회를 실패했습니다", HttpStatus.NOT_FOUND),
	MINT_NFT_FAIL("N003", "NFT 발행에 실패했습니다.", HttpStatus.BAD_REQUEST),
    OWNER_NOT_MATCH("N004", "본인소유의 NFT가 아닙니다.", HttpStatus.BAD_REQUEST),
    NFT_ALREADY_MINT("N005", "이미 발행된 NFT 입니다.", HttpStatus.BAD_REQUEST),

	// NFT MARKET
	NFT_MARKET_NOT_FOUND_ERROR("NM001", "NFT 판매 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NFT_ALREADY_ON_SALE("NM002", "이미 판매중인 NFT 입니다.", HttpStatus.BAD_REQUEST),
    MARKET_CREATE_FAIL("NM003", "NFT 판매 등록에 실패했습니다.", HttpStatus.BAD_REQUEST),
    MARKET_LIKE_FAIL("NM004", "좋아요를 할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MARKET_DISLIKE_FAIL("NM005", "좋아요 취소를 할 수 없습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    MARKET_ALREADY_LIKE("NM006", "이미 좋아요된 판매 게시글입니다.", HttpStatus.BAD_REQUEST),
    MARKET_ALREADY_DISLIKE("NM007", "이미 좋아요 취소되어있습니다.", HttpStatus.BAD_REQUEST),
    MARKET_BUY_FAIL("NM008", "NFT 구매를 실패하였습니다.", HttpStatus.BAD_REQUEST),
    MARKET_OPEN_FAIL("NM009", "NFT 공개/비공개 전환에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    MARKET_ALREADY_SOLD("NM010", "이미 판매완료된 상품입니다.", HttpStatus.BAD_REQUEST),
    MARKET_CANCEL_FAIL("NM011", "NFT 판매 취소에 실패했습니다.", HttpStatus.BAD_REQUEST),

    // BlockChain
    BLOCKCHAIN_ERROR("BC001", "트랜잭션 처리과정에서 오류가 발생했습니다.", HttpStatus.BAD_REQUEST),

    // COIN
    COIN_MINT_FAIL("CO001", "코인 발행에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    COIN_GET_FAIL("CO002", "코인 조회에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    COIN_EXCHANGE_FAIL("CO003", "코인 전환에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    GET_LOG_FAIL("CO004", "코인 거래내역 조회에 실패하였습니다.", HttpStatus.BAD_REQUEST),
    COIN_BURN_FAIL("CO005", "코인 소각에 실패하였습니다.", HttpStatus.BAD_REQUEST),

	// Asset
	LIKE_NOT_FOUND_ERROR("AS001", "좋아요를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	LIKE_ALREADY_EXIST("AS002", "이미 좋아요 눌렀습니다.", HttpStatus.ALREADY_REPORTED),
	ASSET_UPLOAD_FAIL("AS003", "에셋 업로드 실패했습니다", HttpStatus.BAD_REQUEST),
	ASSET_DETAIL_FAIL("AS004", "에셋 상세 조회 실패했습니다", HttpStatus.NOT_FOUND),
	SELL_DETAIL_FAIL("AS005", "판매에셋 상세 조회 실패했습니다", HttpStatus.NOT_FOUND),
	ASSET_LOAD_FAIL("AS006", "판매에셋 전체 조회 실패했습니다", HttpStatus.NOT_FOUND),
	MINE_LOAD_FAIL("AS007", "보유중 에셋 조회 실패했습니다", HttpStatus.NOT_FOUND),
	USERS_LOAD_FAIL("AS008", "유저 보유 에셋 조회 실패했습니다", HttpStatus.NOT_FOUND),
	LIKE_FAIL("AS009", "좋아요 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR),
	DISLIKE_FAIL("AS010", "좋아요 취소 실패하였습니다", HttpStatus.INTERNAL_SERVER_ERROR),
	ASSET_ON_SALE("ASS011", "이미 판매중인 에셋입니다", HttpStatus.BAD_REQUEST),
	CREATOR_NOT_MATCH("ASS012", "에셋 제작자가 아닙니다", HttpStatus.BAD_REQUEST),
	NOT_ENOUGH_BALANCE("AS013","보유 자산이 부족합니다",HttpStatus.BAD_REQUEST),
	ASSET_BUY_FAIL("AS014", "에셋 구매 실패하였습니다.", HttpStatus.BAD_REQUEST),
	CREATORS_LOAD_FAIL("AS015", "유저 생성 에셋 조회 실패했습니다", HttpStatus.NOT_FOUND),
	ALREADY_PURCHASED_ASSET("AS016","이미 구매한 에셋입니다",HttpStatus.NOT_FOUND),

	// Auction
	BID_AMOUNT_LESS_THAN_HIGHEST("AU001", "최고 입찰가보다 낮은 금액으로 입찰할 수 없습니다.", HttpStatus.BAD_REQUEST),
	INSUFFICIENT_BALANCE("AU002", "잔액이 부족합니다.", HttpStatus.BAD_REQUEST),
	INVALID_AUCTION_STATUS("AU003", "유효하지 않은 경매 상태입니다.", HttpStatus.BAD_REQUEST),
	AUCTION_NOT_FOUND("AU004", "경매를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	;

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
