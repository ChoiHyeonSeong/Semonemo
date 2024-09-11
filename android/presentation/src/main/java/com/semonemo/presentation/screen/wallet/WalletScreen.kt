package com.semonemo.presentation.screen.wallet

import android.widget.Space
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semonemo.presentation.R
import com.semonemo.presentation.component.BoldTextWithKeywords
import com.semonemo.presentation.theme.SemonemoTheme
import com.semonemo.presentation.theme.Typography
import com.semonemo.presentation.ui.theme.Blue1
import com.semonemo.presentation.ui.theme.Blue2
import com.semonemo.presentation.ui.theme.Blue3
import com.semonemo.presentation.ui.theme.Main02
import com.semonemo.presentation.ui.theme.White
import com.semonemo.presentation.util.noRippleClickable
import java.util.Locale

data class Trancation(
    val date: String,
    val isSell: Boolean = true,
    val price: Double,
    val product: String,
)

data class ProductInfo(
    val message: String,
    val imageRes: Int,
    val color: Color,
)

val testData =
    listOf(
        Trancation(date = "2024.09.09", isSell = true, price = 100000.0, product = "프레임"),
        Trancation(date = "2024.09.09", isSell = false, price = -103.2, product = "에셋"),
        Trancation(date = "2024.09.08", isSell = false, price = +10000.0, product = "코인"),
        Trancation(date = "2024.09.08", isSell = true, price = -10000.0, product = "코인"),
    )

@Composable
fun WalletRoute() {
}

@Composable
fun WalletContent() {
}

@Composable
fun WalletScreen(
    modifier: Modifier = Modifier,
    userName: String = "짜이한",
    userCoin: Double = 100000.0,
    coinPrice: Double = 100000.0,
    changePercent: Double = 8.7,
    changePrice: Double = 8300.0,
) {
    Column(
        modifier = modifier.padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        WalltetCardBox(modifier = modifier, userName = userName, userCoin = userCoin)
        Spacer(modifier = Modifier.height(10.dp))
        WalletCoinBox(
            modifier = modifier,
            coinPrice = coinPrice,
            changePercent = changePercent,
            changePrice = changePrice,
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(text = "최근 거래내역이에요")
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(1),
            state = rememberLazyGridState(),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(testData.size, span = { _ ->
                GridItemSpan(1)
            }) { index ->
                val item = testData[index]
                transactionHistoryBox(
                    modifier = modifier,
                    date = item.date,
                    isSell = item.isSell,
                    product = item.product,
                    price = item.price,
                )
            }
        }
    }
}

@Composable
fun WalltetCardBox(
    modifier: Modifier = Modifier,
    userName: String,
    userCoin: Double,
) {
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .wrapContentHeight(),
    ) {
        Row(
            modifier =
            modifier
                .fillMaxWidth()
                .matchParentSize(),
            horizontalArrangement = Arrangement.End,
        ) {
            Card(
                modifier =
                Modifier
                    .fillMaxWidth(0.3f)
                    .fillMaxHeight(),
                shape = RoundedCornerShape(10.dp),
                elevation = CardDefaults.cardElevation(2.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .fillMaxSize()
                            .padding(start = 30.dp)
                            .background(brush = Main02)
                            .padding(vertical = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier =
                            Modifier
                                .size(20.dp)
                                .noRippleClickable { {} },
                        painter = painterResource(id = R.drawable.ic_coin_exchange),
                        contentDescription = null,
                    )
                    Text(text = "환전", style = Typography.labelMedium.copy(color = White))
                    Spacer(modifier = Modifier.weight(1f))
                    HorizontalDivider(
                        modifier =
                            Modifier
                                .width(50.dp)
                                .height(0.5.dp),
                        color = White,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier =
                            Modifier
                                .size(20.dp)
                                .noRippleClickable { {} },
                        painter = painterResource(id = R.drawable.ic_coin_plus),
                        contentDescription = null,
                    )
                    Text(text = "충전", style = Typography.labelMedium.copy(color = White))
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        Card(
            modifier =
                modifier
                    .fillMaxWidth(0.8f)
                    .height(150.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(2.dp),
        ) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(color = White)
                        .padding(10.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    BoldTextWithKeywords(
                        modifier = Modifier.padding(start = 5.dp, top = 5.dp),
                        fullText = "$userName 님의 지갑",
                        keywords = listOf(userName),
                        brushFlag = listOf(true),
                        style = Typography.bodyLarge,
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_color_sene_coin),
                            contentDescription = "",
                        )
                        Text(
                            text = String.format(Locale.KOREAN, "%,.0f", userCoin),
                            style = Typography.bodyLarge,
                            fontSize = 20.sp,
                        )

                        Text(text = "SN", style = Typography.labelMedium)
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Image(
                        modifier =
                            Modifier
                                .padding(10.dp)
                                .size(105.dp),
                        painter = painterResource(id = R.drawable.img_money),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
fun WalletCoinBox(
    modifier: Modifier = Modifier,
    coinPrice: Double,
    changePercent: Double,
    changePrice: Double,
) {
    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .background(White)
                    .padding(horizontal = 10.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Column(
                modifier =
                    Modifier
                        .wrapContentSize()
                        .weight(1f),
            ) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(id = R.drawable.ic_color_sene_coin),
                    contentDescription = "",
                )
                Text(text = "세네코인", style = Typography.bodyLarge.copy(fontSize = 15.sp))
            }
            Image(
                modifier = Modifier.size(50.dp),
                painter = painterResource(id = R.drawable.img_graph),
                contentDescription = null,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "현재가", style = Typography.labelLarge)
                Text(text = String.format(Locale.KOREAN, "%,.0f", coinPrice))
            }
            Spacer(modifier = Modifier.weight(0.1f))
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "등락율", style = Typography.labelLarge)
                Text(
                    text =
                        if (changePrice > 0) {
                            "+$changePercent%"
                        } else {
                            "-$changePercent%"
                        },
                    color = if (changePercent > 0) Color.Red else Color.Blue,
                    style = Typography.bodyMedium,
                )
                Text(
                    text = String.format(Locale.KOREAN, "%,.0f", changePrice),
                    color = if (changePercent > 0) Color.Red else Color.Blue,
                    style = Typography.labelSmall,
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}

@Composable
fun transactionHistoryBox(
    modifier: Modifier = Modifier,
    isSell: Boolean = true,
    date: String = "2024.09.09",
    price: Double = +100000.0,
    product: String = "프레임",
) {
    val productInfo =
        when (product) {
            "코인" -> {
                if (isSell) {
                    ProductInfo("환전", R.drawable.ic_outline_coin, color = Blue3)
                } else {
                    ProductInfo("충전", R.drawable.ic_outline_coin, color = Blue3)
                }
            }

            "프레임" -> {
                if (isSell) {
                    ProductInfo("판매", R.drawable.ic_fab_frame, color = Blue2)
                } else {
                    ProductInfo("구매", R.drawable.ic_fab_frame, color = Blue2)
                }
            }

            else -> {
                if (isSell) {
                    ProductInfo("판매", R.drawable.ic_fab_asset, color = Blue1)
                } else {
                    ProductInfo("구매", R.drawable.ic_fab_asset, color = Blue1)
                }
            }
        }

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .wrapContentHeight(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = 10.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(productInfo.color),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    painter = painterResource(id = productInfo.imageRes),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                )
            }
            Spacer(modifier = Modifier.weight(0.1f))
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = date, style = Typography.labelSmall)
                Text(text = "$product ${productInfo.message}", style = Typography.bodyLarge)
            }
            Spacer(modifier = Modifier.weight(0.5f))
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = R.drawable.ic_color_sene_coin),
                contentDescription = null,
            )
            Text(
                text =
                    if (price > 0) {
                        String.format(Locale.KOREAN, "%,.0f", price)
                    } else {
                        String.format(Locale.KOREAN, "%,.0f", price)
                    },
                color = if (price > 0) Color.Red else Color.Blue,
                style = Typography.bodyLarge,
            )

            Text(text = "SN", style = Typography.labelLarge)
            Spacer(modifier = Modifier.weight(0.1f))
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun WalletScreenPreview() {
    SemonemoTheme {
        Scaffold { innerPadding ->
            WalletScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
