package com.semonemo.spring_server.domain.auction.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class BidRequestDTO {
	private long userId;
	private long bidAmount;
}
