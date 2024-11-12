package com.xiaojinzi.tally.module.core.module.ai_bill_chat.view

import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.xiaojinzi.component.impl.routeApi
import com.xiaojinzi.reactive.template.view.BusinessContentView
import com.xiaojinzi.support.compose.util.circleClip
import com.xiaojinzi.support.compose.util.clickableNoRipple
import com.xiaojinzi.support.ktx.commonTimeFormat2
import com.xiaojinzi.support.ktx.format2f
import com.xiaojinzi.support.ktx.nothing
import com.xiaojinzi.support.ktx.tryFinishActivity
import com.xiaojinzi.tally.lib.res.model.tally.BillChatDto
import com.xiaojinzi.tally.lib.res.ui.APP_PADDING_LARGE
import com.xiaojinzi.tally.lib.res.ui.APP_PADDING_NORMAL
import com.xiaojinzi.tally.lib.res.ui.APP_PADDING_SMALL
import com.xiaojinzi.tally.lib.res.ui.AppBackgroundColor
import com.xiaojinzi.tally.lib.res.ui.AppHeightSpace
import com.xiaojinzi.tally.module.base.support.AppRouterMainApi
import com.xiaojinzi.tally.module.base.support.AppServices
import com.xiaojinzi.tally.module.base.view.compose.AppbarNormalM3
import com.xiaojinzi.tally.module.core.module.ai_bill_chat.domain.AiBillChatIntent
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@OptIn(ExperimentalComposeUiApi::class)
@InternalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
private fun AiBillChatView(
    needInit: Boolean? = false,
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val isVip by AppServices.userSpi.isVipStateOb.collectAsState(initial = false)
    BusinessContentView<AiBillChatViewModel>(
        needInit = needInit,
    ) { vm ->
        LaunchedEffect(key1 = Unit) {
            vm
                .hideKeyboardEvent
                .onEach {
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                }
                .launchIn(scope = this)
        }
        val billChatList by vm.billChatListStateObVo.collectAsState(initial = emptyList())
        val content by vm.contentStateOb.collectAsState(initial = "")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = AppBackgroundColor,
                )
                .clickableNoRipple {
                    keyboardController?.hide()
                    focusManager.clearFocus(force = true)
                }
                .nothing(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val listState: LazyListState = rememberLazyListState()
            LaunchedEffect(key1 = Unit) {
                delay(timeMillis = 100)
                listState.scrollToItem(index = 0)
            }
            LaunchedEffect(key1 = listState) {
                vm
                    .toFirstItemEvent
                    .onEach {
                        runCatching {
                            listState.scrollToItem(index = 0)
                        }
                    }
                    .launchIn(scope = this)
                snapshotFlow { listState.isScrollInProgress }
                    .filter { it }
                    .onEach {
                        keyboardController?.hide()
                        focusManager.clearFocus(force = true)
                    }
                    .launchIn(scope = this)
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(weight = 1f, fill = true)
                    .nothing(),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .align(alignment = Alignment.TopCenter)
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .nothing(),
                    state = listState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    reverseLayout = true,
                ) {
                    itemsIndexed(
                        items = billChatList,
                    ) { _, item ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(
                                    horizontal = APP_PADDING_NORMAL.dp,
                                    vertical = APP_PADDING_NORMAL.dp
                                )
                                .nothing(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .nothing(),
                            ) {
                                Spacer(
                                    modifier = Modifier
                                        .width(width = 36.dp)
                                        .nothing()
                                )
                                Row(
                                    modifier = Modifier
                                        .weight(weight = 1f, fill = true)
                                        .wrapContentHeight()
                                        .nothing(),
                                    horizontalArrangement = Arrangement.End,
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .padding(
                                                top = 2.dp,
                                            )
                                            .wrapContentSize()
                                            .clip(
                                                shape = MaterialTheme.shapes.small,
                                            )
                                            .background(
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                            )
                                            .padding(horizontal = 8.dp, vertical = 8.dp)
                                            .nothing(),
                                        text = item.core.content.orEmpty(),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        ),
                                        textAlign = TextAlign.End,
                                    )
                                }
                                Spacer(
                                    modifier = Modifier
                                        .width(width = 8.dp)
                                        .nothing()
                                )
                                Icon(
                                    modifier = Modifier
                                        .circleClip()
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                elevation = 1.dp,
                                            )
                                        )
                                        .padding(all = 4.dp)
                                        .size(size = 24.dp)
                                        .nothing(),
                                    painter = painterResource(
                                        id = com.xiaojinzi.tally.lib.res.R.drawable.res_people2,
                                    ),
                                    contentDescription = null,
                                )
                            }
                            Spacer(
                                modifier = Modifier
                                    .height(height = 16.dp)
                                    .nothing()
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .nothing(),
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .circleClip()
                                        .background(
                                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                elevation = 1.dp,
                                            )
                                        )
                                        .padding(all = 4.dp)
                                        .size(size = 28.dp)
                                        .nothing(),
                                    painter = painterResource(
                                        id = com.xiaojinzi.tally.lib.res.R.drawable.res_robot1,
                                    ),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.8f,
                                    )
                                )
                                Spacer(
                                    modifier = Modifier
                                        .width(width = 8.dp)
                                        .nothing()
                                )
                                when (item.core.state) {
                                    BillChatDto.STATE_INIT -> {
                                        val composition by rememberLottieComposition(
                                            LottieCompositionSpec.RawRes(com.xiaojinzi.tally.lib.res.R.raw.res_loading3)
                                        )
                                        LottieAnimation(
                                            modifier = Modifier
                                                .size(size = 50.dp)
                                                .nothing(),
                                            composition = composition,
                                            iterations = LottieConstants.IterateForever,
                                        )
                                    }

                                    BillChatDto.STATE_FAIL -> {
                                        Row(
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .nothing(),
                                            verticalAlignment = Alignment.CenterVertically,
                                        ) {
                                            Text(
                                                modifier = Modifier
                                                    .padding(
                                                        top = 2.dp,
                                                    )
                                                    .wrapContentSize()
                                                    .clip(
                                                        shape = MaterialTheme.shapes.small,
                                                    )
                                                    .background(
                                                        color = MaterialTheme.colorScheme.surface,
                                                    )
                                                    .padding(horizontal = 8.dp, vertical = 8.dp)
                                                    .nothing(),
                                                text = "记账失败~~~",
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                ),
                                                textAlign = TextAlign.End,
                                            )
                                            Icon(
                                                modifier = Modifier
                                                    .clickableNoRipple {
                                                        vm.addIntent(
                                                            intent = AiBillChatIntent.Retry(
                                                                context = context,
                                                                id = item.core.id,
                                                            )
                                                        )
                                                    }
                                                    .padding(all = APP_PADDING_NORMAL.dp)
                                                    .size(size = 16.dp)
                                                    .nothing(),
                                                painter = painterResource(
                                                    id = com.xiaojinzi.tally.lib.res.R.drawable.res_retry1,
                                                ),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSurface,
                                            )
                                        }

                                    }

                                    BillChatDto.STATE_SUCCESS -> {
                                        ConstraintLayout(
                                            modifier = Modifier
                                                .weight(weight = 1f, fill = true)
                                                .wrapContentHeight()
                                                .clip(
                                                    shape = MaterialTheme.shapes.small,
                                                )
                                                .background(
                                                    color = MaterialTheme.colorScheme.surface,
                                                )
                                                .clickableNoRipple {
                                                    vm.addIntent(
                                                        intent = AiBillChatIntent.ToBillDetail(
                                                            context = context,
                                                            billId = item.billId,
                                                        )
                                                    )
                                                }
                                                .padding(bottom = APP_PADDING_NORMAL.dp)
                                                .nothing(),
                                        ) {
                                            val (
                                                textAlreadyBill, time,
                                                cateIcon, cateName,
                                                note, amount,
                                            ) = createRefs()
                                            Text(
                                                modifier = Modifier
                                                    .constrainAs(ref = textAlreadyBill) {
                                                        this.top.linkTo(
                                                            anchor = parent.top,
                                                            margin = APP_PADDING_NORMAL.dp,
                                                        )
                                                        this.start.linkTo(
                                                            anchor = parent.start,
                                                            margin = APP_PADDING_NORMAL.dp,
                                                        )
                                                    }
                                                    .nothing(),
                                                text = "已记账: ",
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                ),
                                                textAlign = TextAlign.Start,
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .constrainAs(ref = time) {
                                                        this.width = Dimension.fillToConstraints
                                                        this.height = Dimension.wrapContent
                                                        this.top.linkTo(
                                                            anchor = parent.top,
                                                            margin = APP_PADDING_NORMAL.dp,
                                                        )
                                                        this.start.linkTo(
                                                            anchor = textAlreadyBill.end,
                                                            margin = 0.dp,
                                                        )
                                                        this.end.linkTo(
                                                            anchor = parent.end,
                                                            margin = APP_PADDING_NORMAL.dp,
                                                        )
                                                    }
                                                    .nothing(),
                                                text = item.time?.commonTimeFormat2().orEmpty(),
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                ),
                                                textAlign = TextAlign.End,
                                            )
                                            Icon(
                                                modifier = Modifier
                                                    .constrainAs(ref = cateIcon) {
                                                        this.top.linkTo(
                                                            anchor = time.bottom,
                                                            margin = APP_PADDING_NORMAL.dp,
                                                        )
                                                        this.start.linkTo(
                                                            anchor = parent.start,
                                                            margin = APP_PADDING_NORMAL.dp,
                                                        )
                                                    }
                                                    .circleClip()
                                                    .background(
                                                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                                            elevation = 1.dp,
                                                        )
                                                    )
                                                    .padding(all = 5.dp)
                                                    .size(size = 28.dp)
                                                    .nothing(),
                                                painter = item.cateIconRsd?.let {
                                                    painterResource(
                                                        id = it,
                                                    )
                                                } ?: ColorPainter(
                                                    color = Color.Transparent,
                                                ),
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .constrainAs(ref = cateName) {
                                                        this.width = Dimension.fillToConstraints
                                                        this.height = Dimension.wrapContent
                                                        this.top.linkTo(
                                                            anchor = cateIcon.top, margin = 2.dp,
                                                        )
                                                        this.start.linkTo(
                                                            anchor = cateIcon.end,
                                                            margin = APP_PADDING_NORMAL.dp,
                                                        )
                                                        this.end.linkTo(
                                                            anchor = amount.start,
                                                            margin = APP_PADDING_NORMAL.dp,
                                                        )
                                                    }
                                                    .nothing(),
                                                text = item.cateName.orEmpty(),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                ),
                                                textAlign = TextAlign.Start,
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .constrainAs(ref = note) {
                                                        this.width = Dimension.fillToConstraints
                                                        this.height = Dimension.wrapContent
                                                        this.top.linkTo(
                                                            anchor = cateName.bottom, margin = 4.dp,
                                                        )
                                                        this.start.linkTo(
                                                            anchor = cateName.start,
                                                            margin = 0.dp,
                                                        )
                                                        this.end.linkTo(
                                                            anchor = amount.start,
                                                            margin = APP_PADDING_NORMAL.dp,
                                                        )
                                                    }
                                                    .nothing(),
                                                text = item.note.orEmpty(),
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = MaterialTheme.colorScheme.outline,
                                                ),
                                                textAlign = TextAlign.Start,
                                            )
                                            Text(
                                                modifier = Modifier
                                                    .constrainAs(ref = amount) {
                                                        this.end.linkTo(
                                                            anchor = parent.end,
                                                            margin = APP_PADDING_NORMAL.dp,
                                                        )
                                                        this.centerVerticallyTo(other = cateIcon)
                                                    }
                                                    .nothing(),
                                                text = item.amount?.value?.format2f(
                                                    isKeepZero = false,
                                                ).orEmpty(),
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.Medium,
                                                    color = MaterialTheme.colorScheme.primary,
                                                ),
                                                textAlign = TextAlign.Start,
                                            )
                                        }
                                        Spacer(
                                            modifier = Modifier
                                                .width(width = 36.dp)
                                                .nothing()
                                        )
                                    }
                                }
                            }
                            AppHeightSpace()
                            when (item.core.state) {
                                BillChatDto.STATE_SUCCESS -> {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .wrapContentHeight()
                                            .nothing(),
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Spacer(
                                            modifier = Modifier
                                                .width(width = 48.dp)
                                                .nothing()
                                        )
                                        Text(
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .circleClip()
                                                .background(
                                                    color = MaterialTheme.colorScheme.surface,
                                                )
                                                .clickable {
                                                    vm.addIntent(
                                                        intent = AiBillChatIntent.EditBill(
                                                            context = context,
                                                            id = item.core.id,
                                                        )
                                                    )
                                                }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                                .nothing(),
                                            text = "修改账单",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = MaterialTheme.colorScheme.onSurface,
                                            ),
                                            textAlign = TextAlign.Start,
                                        )
                                        Spacer(
                                            modifier = Modifier
                                                .width(width = 12.dp)
                                                .nothing()
                                        )
                                        Text(
                                            modifier = Modifier
                                                .wrapContentSize()
                                                .circleClip()
                                                .background(
                                                    color = MaterialTheme.colorScheme.surface,
                                                )
                                                .clickable {
                                                    vm.addIntent(
                                                        intent = AiBillChatIntent.DeleteBill(
                                                            context = context,
                                                            id = item.core.id,
                                                        )
                                                    )
                                                }
                                                .padding(horizontal = 10.dp, vertical = 6.dp)
                                                .nothing(),
                                            text = "删除账单",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = MaterialTheme.colorScheme.onSurface,
                                            ),
                                            textAlign = TextAlign.Start,
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item(
                        key = "headerTipItem"
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(
                                    horizontal = APP_PADDING_NORMAL.dp,
                                    vertical = APP_PADDING_NORMAL.dp
                                )
                                .nothing(),
                        ) {
                            Icon(
                                modifier = Modifier
                                    .circleClip()
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                            elevation = 1.dp,
                                        )
                                    )
                                    .padding(all = 4.dp)
                                    .size(size = 28.dp)
                                    .nothing(),
                                painter = painterResource(
                                    id = com.xiaojinzi.tally.lib.res.R.drawable.res_robot1,
                                ),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary.copy(
                                    alpha = 0.8f,
                                )
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(width = 8.dp)
                                    .nothing()
                            )
                            Text(
                                modifier = Modifier
                                    .padding(
                                        top = 2.dp,
                                    )
                                    .weight(weight = 1f, fill = true)
                                    .wrapContentHeight()
                                    .clip(
                                        shape = MaterialTheme.shapes.small,
                                    )
                                    .background(
                                        color = MaterialTheme.colorScheme.surface,
                                    )
                                    .padding(horizontal = 8.dp, vertical = APP_PADDING_NORMAL.dp)
                                    .nothing(),
                                text = "哈喽, 欢迎使用 '一刻'  AI 记账小助手, 您可以输入文字内容, 我来帮您归类记录. 比如：打车 100, 早餐 12${
                                    if (isVip) {
                                        ""
                                    } else {
                                        "\n试用结束需开通 Vip 才能继续使用哦"
                                    }
                                }",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    // fontWeight = FontWeight(weight = (FontWeight.Normal.weight + FontWeight.Medium.weight) / 2),
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                ),
                                textAlign = TextAlign.Start,
                            )
                            Spacer(
                                modifier = Modifier
                                    .width(width = 36.dp)
                                    .nothing()
                            )
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                    )
                    .navigationBarsPadding()
                    .imePadding()
                    .nothing(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val focusRequesterForContent = remember { FocusRequester() }
                LaunchedEffect(key1 = focusRequesterForContent) {
                    delay(500)
                    focusRequesterForContent.requestFocus()
                    keyboardController?.show()
                }
                Box(
                    modifier = Modifier
                        .padding(
                            start = APP_PADDING_NORMAL.dp,
                            end = 0.dp,
                            top = APP_PADDING_SMALL.dp,
                            bottom = APP_PADDING_SMALL.dp,
                        )
                        .weight(weight = 1f, fill = true)
                        .wrapContentHeight()
                        .circleClip()
                        .background(
                            color = MaterialTheme.colorScheme.surfaceColorAtElevation(
                                elevation = 1.dp,
                            )
                        )
                        .clickableNoRipple {
                            focusRequesterForContent.requestFocus()
                            keyboardController?.show()
                            vm.toFirstItemEvent.add(
                                value = Unit,
                            )
                        }
                        .padding(
                            horizontal = APP_PADDING_LARGE.dp,
                            vertical = APP_PADDING_NORMAL.dp,
                        )
                        .nothing(),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    if (content.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .nothing(),
                            text = "比如：午餐 50",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.outline,
                            ),
                            textAlign = TextAlign.Start,
                        )
                    }
                    BasicTextField(
                        modifier = Modifier
                            .focusRequester(
                                focusRequester = focusRequesterForContent,
                            )
                            .align(alignment = Alignment.CenterStart)
                            .width(intrinsicSize = IntrinsicSize.Min)
                            .widthIn(min = 2.dp)
                            .wrapContentHeight()
                            .nothing(),
                        value = content,
                        onValueChange = {
                            vm.contentStateOb.value = it
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                vm.addIntent(
                                    intent = AiBillChatIntent.Submit(
                                        context = context,
                                    ),
                                )
                            },
                        ),
                        cursorBrush = SolidColor(
                            value = MaterialTheme.colorScheme.primary,
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                    )
                }
                TextButton(
                    onClick = {
                        vm.addIntent(
                            intent = AiBillChatIntent.Submit(
                                context = context,
                            ),
                        )
                    }
                ) {
                    Text(
                        text = "发送",
                        textAlign = TextAlign.Start,
                    )
                }
            }
        }
    }
}

@InternalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun AiBillChatViewWrap() {
    val context = LocalContext.current
    Scaffold(
        topBar = {
            AppbarNormalM3(
                backIcon = null,
                titleContent = {
                    Text(
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .wrapContentSize()
                            .nothing(),
                        text = "一刻记账",
                        fontFamily = FontFamily(Font(com.xiaojinzi.tally.lib.res.R.font.res_font_xdks)),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        textAlign = TextAlign.Start,
                    )
                },
                menu1IconRsd = com.xiaojinzi.tally.lib.res.R.drawable.res_setting1,
                menu1ClickListener = {
                    AppRouterMainApi::class
                        .routeApi()
                        .toSettingView(context = context) {
                            context.tryFinishActivity()
                        }
                },
            )
        },
    ) {
        Box(
            modifier = Modifier
                .padding(top = it.calculateTopPadding())
                .nothing(),
        ) {
            AiBillChatView()
        }
    }
}

@InternalCoroutinesApi
@ExperimentalMaterial3Api
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@Preview
@Composable
private fun AiBillChatViewPreview() {
    AiBillChatView(
        needInit = false,
    )
}