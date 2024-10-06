package com.semonemo.presentation.screen.auction.subScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.semonemo.presentation.R
import com.semonemo.presentation.component.AuctionBidPriceInput
import com.semonemo.presentation.component.BidPriceUnitButton
import com.semonemo.presentation.component.CustomAuctionProgressBar
import com.semonemo.presentation.component.CustomNoRippleButton
import com.semonemo.presentation.screen.auction.AuctionDetailViewModel
import com.semonemo.presentation.screen.auction.BidMessage
import com.semonemo.presentation.theme.Typography
import com.walletconnect.android.internal.common.scope
import kotlinx.coroutines.launch

@Composable
fun AuctionProgressScreen(
    modifier: Modifier = Modifier,
    viewModel: AuctionDetailViewModel = hiltViewModel(),
) {
    Surface(
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier,
        ) {
            val adjustEndTime =
                viewModel.auctionBidLog.value
                    .lastOrNull()
                    ?.endTime
            CustomAuctionProgressBar(endTime = adjustEndTime ?: viewModel.endTime.value)
            Spacer(modifier = Modifier.height(20.dp))
            BidInfo(
                bidLogList = viewModel.auctionBidLog.value,
                startPrice = viewModel.topPrice.longValue,
                // 위에서 선언한 auctionBidLog
            )

            Spacer(modifier = Modifier.height(4.dp))
            AuctionBidPriceInput(
                modifier = Modifier,
                currentPrice = viewModel.topPrice.longValue,
                adjustedAmount = viewModel.myBidPrice.longValue,
                adjustedPercentage = viewModel.myPercentage.intValue,
                onClear = { viewModel.adjustClear() },
            )

            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = "10% = ${viewModel.bidPriceUnit.longValue} ${stringResource(id = R.string.coin_price_unit)}",
                    style =
                        Typography.labelMedium.copy(
                            fontFeatureSettings = "tnum",
                            fontSize = 20.sp,
                        ),
                )
                Text(
                    text = "",
                    style =
                        Typography.labelMedium.copy(
                            fontFeatureSettings = "tnum",
                            fontSize = 20.sp,
                        ),
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                BidPriceUnitButton(
                    modifier = Modifier.weight(1f),
                    bidPricePercentage = 100,
                    bidPriceUnit = viewModel.bidPriceUnit.longValue * 10,
                    onClick = {
                        viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue * 10, 100)
                    },
                )
                BidPriceUnitButton(
                    modifier = Modifier.weight(1f),
                    bidPricePercentage = 50,
                    bidPriceUnit = viewModel.bidPriceUnit.longValue * 5,
                    onClick = {
                        viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue * 5, 50)
                    },
                )
                BidPriceUnitButton(
                    modifier = Modifier.weight(1f),
                    bidPricePercentage = 10,
                    bidPriceUnit = viewModel.bidPriceUnit.longValue,
                    onClick = {
                        viewModel.adjustBidPrice(viewModel.bidPriceUnit.longValue, 10)
                    },
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            CustomNoRippleButton(
                text = "입찰하기",
                onClick = {
                    scope.launch {
                        viewModel.stompSession.value?.let { session ->
                            viewModel.webSocketManager.value?.sendBid(
                                session,
                                viewModel.auctionId,
                                BidMessage(
                                    viewModel.userId,
                                    viewModel.anonym.intValue,
                                    viewModel.topPrice.longValue + viewModel.myBidPrice.longValue,
                                ),
                            )
                        }
                    }
                },
            )
        }
    }
}
