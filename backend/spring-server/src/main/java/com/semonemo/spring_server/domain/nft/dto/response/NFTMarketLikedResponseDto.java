package com.semonemo.spring_server.domain.nft.dto.response;

import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

public record NFTMarketLikedResponseDto (
    Long marketId,
    int likedCount
) {
}
