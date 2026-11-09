package com.a.server.presentation.ui

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Suppress("FrequentlyChangingValue")
@Composable
fun UniversalVerticalScrollbar(
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    val maxScroll = scrollState.maxValue.toFloat()
    if (maxScroll <= 0f) return

    val viewportHeight = scrollState.viewportSize.toFloat()
    val totalHeight = maxScroll + viewportHeight

    val thumbHeightFraction = (viewportHeight / totalHeight).coerceIn(0.1f, 1f)
    val scrollFraction = (scrollState.value.toFloat() / maxScroll).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .width(8.dp)
            .fillMaxHeight()
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(thumbHeightFraction)
                .offset(y = (viewportHeight * (1f - thumbHeightFraction) * scrollFraction).dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
        )
    }
}

@Suppress("FrequentlyChangingValue")
@Composable
fun UniversalHorizontalLazyListScrollbar(
    modifier: Modifier = Modifier,
    scrollState: LazyListState
) {
    val coroutineScope = rememberCoroutineScope()
    val layoutInfo = scrollState.layoutInfo
    val totalItemsCount = layoutInfo.totalItemsCount
    if (totalItemsCount <= 0) return

    val visibleItemsInfo = layoutInfo.visibleItemsInfo
    if (visibleItemsInfo.isEmpty()) return

    val viewportWidth = (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset).toFloat()
    if (viewportWidth <= 0f) return

    val averageItemSize = visibleItemsInfo.sumOf { it.size }.toFloat() / visibleItemsInfo.size
    val totalEstimatedWidth = averageItemSize * totalItemsCount
    if (totalEstimatedWidth <= 0f) return

    val thumbWidthFraction = (viewportWidth / totalEstimatedWidth).coerceIn(0.1f, 1f)
    val thumbWidth = viewportWidth * thumbWidthFraction
    val trackWidth = viewportWidth - thumbWidth

    val firstVisible = visibleItemsInfo.first()
    val currentScrollOffset = (firstVisible.index * averageItemSize) - firstVisible.offset
    val maxScroll = (totalEstimatedWidth - viewportWidth).coerceAtLeast(1f)
    val scrollFraction = (currentScrollOffset / maxScroll).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .height(10.dp)
            .fillMaxWidth()
            .background(androidx.compose.ui.graphics.Color.Transparent)
            .pointerInput(scrollState, totalEstimatedWidth, viewportWidth) {
                detectTapGestures { offset ->
                    if (trackWidth > 0f) {
                        val targetFraction = (offset.x - thumbWidth / 2f).coerceIn(0f, trackWidth) / trackWidth
                        val targetScroll = targetFraction * maxScroll
                        val targetIndex = (targetScroll / averageItemSize).toInt().coerceIn(0, totalItemsCount - 1)
                        val targetItemOffset = (targetScroll % averageItemSize).toInt()
                        coroutineScope.launch {
                            scrollState.scrollToItem(targetIndex, targetItemOffset)
                        }
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(thumbWidthFraction)
                .offset(x = with(LocalDensity.current) {
                    (trackWidth * scrollFraction).toDp()
                })
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                .pointerInput(scrollState, totalEstimatedWidth, viewportWidth) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        if (trackWidth > 0f) {
                            val scrollDelta = dragAmount.x * (maxScroll / trackWidth)
                            coroutineScope.launch {
                                scrollState.scrollBy(scrollDelta)
                            }
                        }
                    }
                }
        )
    }
}

@Suppress("FrequentlyChangingValue")
@Composable
fun UniversalHorizontalScrollStateScrollbar(
    modifier: Modifier = Modifier,
    scrollState: ScrollState
) {
    val coroutineScope = rememberCoroutineScope()

    val maxScroll = scrollState.maxValue.toFloat()
    if (maxScroll <= 0f) return

    val viewportWidth = scrollState.viewportSize.toFloat()
    if (viewportWidth <= 0f) return

    val totalWidth = maxScroll + viewportWidth
    val thumbWidthFraction = (viewportWidth / totalWidth).coerceIn(0.1f, 1f)
    val thumbWidth = viewportWidth * thumbWidthFraction
    val trackWidth = viewportWidth - thumbWidth

    val currentScroll = scrollState.value.toFloat()
    val scrollFraction = (currentScroll / maxScroll).coerceIn(0f, 1f)

    Box(
        modifier = modifier
            .height(10.dp)
            .fillMaxWidth()
            .background(androidx.compose.ui.graphics.Color.Transparent)
            .pointerInput(scrollState, maxScroll, viewportWidth) {
                detectTapGestures { offset ->
                    if (trackWidth > 0f) {
                        val targetFraction = (offset.x - thumbWidth / 2f).coerceIn(0f, trackWidth) / trackWidth
                        val targetScroll = targetFraction * maxScroll
                        coroutineScope.launch {
                            scrollState.scrollTo(targetScroll.toInt())
                        }
                    }
                }
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(thumbWidthFraction)
                .offset(x = with(LocalDensity.current) {
                    (trackWidth * scrollFraction).toDp()
                })
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                .pointerInput(scrollState, maxScroll, viewportWidth) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        if (trackWidth > 0f) {
                            val scrollDelta = dragAmount.x * (maxScroll / trackWidth)
                            coroutineScope.launch {
                                scrollState.scrollBy(scrollDelta)
                            }
                        }
                    }
                }
        )
    }
}