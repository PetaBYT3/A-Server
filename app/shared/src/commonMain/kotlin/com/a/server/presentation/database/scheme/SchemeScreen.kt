package com.a.server.presentation.database.scheme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastForEachIndexed
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.a.server.navigation.NavigationState
import com.a.server.presentation.database.scheme.component.TableCard
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DatabaseSchemeScreen(
    navigationState: NavigationState = koinInject(),
    viewModel: SchemeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Content(
        navigationState = navigationState,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun Content(
    navigationState: NavigationState,
    state: SchemeState,
    onAction: (SchemeAction) -> Unit
) {
    val density = LocalDensity.current

    val worldWidthDp = 3200.dp
    val worldHeightDp = 2400.dp
    val worldWidthPx = with(density) { worldWidthDp.toPx() }
    val worldHeightPx = with(density) { worldHeightDp.toPx() }

    var viewPortSize by remember { mutableStateOf(IntSize.Zero) }
    var canvasPanOffset by remember { mutableStateOf(Offset.Zero) }
    var scale by remember { mutableFloatStateOf(1f) }

    val localPositions = remember { mutableStateMapOf<String, Offset>() }
    val localSizes = remember { mutableStateMapOf<String, IntSize>() }

    val minorGridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
    val majorGridColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)

    LaunchedEffect(state.tables) {
        state.tables.fastForEachIndexed { index, item ->
            val name = item.table.name
            if (!localPositions.containsKey(name)) {
                localPositions[name] = if (item.table.position == Offset.Zero && index > 0) {
                    val col = index % 4
                    val row = index / 4
                    Offset(x = 80f + (col * 320f), y = 80f + (row * 260f))
                } else {
                    item.table.position
                }
            }
        }
        val activeNames = state.tables.map { it.table.name }.toSet()
        localPositions.keys.retainAll(activeNames)
        localSizes.keys.retainAll(activeNames)
    }

    fun clampCardPosition(pos: Offset, cardSize: IntSize): Offset {
        val maxX = (worldWidthPx - cardSize.width).coerceAtLeast(0f)
        val maxY = (worldHeightPx - cardSize.height).coerceAtLeast(0f)
        return Offset(
            x = pos.x.coerceIn(0f, maxX),
            y = pos.y.coerceIn(0f, maxY)
        )
    }

    fun clampCanvasPan(pan: Offset, currentScale: Float): Offset {
        val scaledWidth = worldWidthPx * currentScale
        val scaledHeight = worldHeightPx * currentScale
        val minX = -(scaledWidth - viewPortSize.width).coerceAtLeast(0f)
        val minY = -(scaledHeight - viewPortSize.height).coerceAtLeast(0f)
        return Offset(
            x = pan.x.coerceIn(minX, 0f),
            y = pan.y.coerceIn(minY, 0f)
        )
    }

    fun updateZoomFromCenter(newScale: Float) {
        val clampedScale = newScale.coerceIn(0.4f, 2.0f)
        val center = Offset(viewPortSize.width / 2f, viewPortSize.height / 2f)
        val newPan = (canvasPanOffset - center) * (clampedScale / scale) + center
        scale = clampedScale
        canvasPanOffset = clampCanvasPan(newPan, clampedScale)
    }

    Box(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
            .fillMaxSize()
            .border(
                width = ListItemDefaults.SegmentedGap,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(RoundedCornerShape(15.dp))
            .clipToBounds()
            .onSizeChanged { viewPortSize = it }
            .pointerInput(worldWidthPx, worldHeightPx, viewPortSize) {
                detectTransformGestures { centroid, pan, zoom, _ ->
                    val oldScale = scale
                    val newScale = (scale * zoom).coerceIn(0.4f, 2.0f)
                    val nextPan = (canvasPanOffset - centroid) * (newScale / oldScale) + centroid + pan
                    scale = newScale
                    canvasPanOffset = clampCanvasPan(nextPan, newScale)
                }
            },
        contentAlignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .wrapContentSize(unbounded = true, align = Alignment.TopStart)
                .size(worldWidthDp, worldHeightDp)
                .graphicsLayer {
                    translationX = canvasPanOffset.x
                    translationY = canvasPanOffset.y
                    scaleX = scale
                    scaleY = scale
                    transformOrigin = TransformOrigin(0f, 0f)
                }
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val cellSize = 32.dp.toPx()
                val minorStroke = 1.dp.toPx()
                val majorStroke = 1.5.dp.toPx()

                // Garis Vertikal
                var colIndex = 0
                var x = 0f
                while (x <= size.width) {
                    val isMajor = colIndex % 4 == 0
                    drawLine(
                        color = if (isMajor) majorGridColor else minorGridColor,
                        start = Offset(x, 0f),
                        end = Offset(x, size.height),
                        strokeWidth = if (isMajor) majorStroke else minorStroke
                    )
                    x += cellSize
                    colIndex++
                }

                // Garis Horizontal
                var rowIndex = 0
                var y = 0f
                while (y <= size.height) {
                    val isMajor = rowIndex % 4 == 0
                    drawLine(
                        color = if (isMajor) majorGridColor else minorGridColor,
                        start = Offset(0f, y),
                        end = Offset(size.width, y),
                        strokeWidth = if (isMajor) majorStroke else minorStroke
                    )
                    y += cellSize
                    rowIndex++
                }
            }

            state.tables.fastForEach { tableWithFieldsModel ->
                val tableName = tableWithFieldsModel.table.name
                key(tableName) {
                    TableCard(
                        tableWithFieldsModel = tableWithFieldsModel,
                        positionProvider = {
                            localPositions[tableName] ?: tableWithFieldsModel.table.position
                        },
                        onDrag = { delta ->
                            val current = localPositions[tableName] ?: tableWithFieldsModel.table.position
                            val size = localSizes[tableName] ?: IntSize.Zero
                            localPositions[tableName] = clampCardPosition(current + delta, size)
                        },
                        onDragEnd = {
                            val finalPos = localPositions[tableName] ?: return@TableCard
                            onAction(
                                SchemeAction.OnDragComplete(
                                    tableWithField = tableWithFieldsModel.copy(
                                        table = tableWithFieldsModel.table.copy(position = finalPos)
                                    )
                                )
                            )
                        },
                        onSizeMeasured = { size ->
                            localSizes[tableName] = size
                        }
                    )
                }
            }
        }

        SegmentedListItem(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(15.dp)
                .width(250.dp),
            colors = ListItemDefaults.segmentedColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            shapes = ListItemDefaults.segmentedShapes(
                index = 0,
                count = 1
            ),
            content = {
                Row {
                    Slider(
                        modifier = Modifier
                            .weight(1f),
                        value = scale,
                        onValueChange = { updateZoomFromCenter(it) },
                        valueRange = 0.4f..2.0f
                    )
                    Spacer(
                        modifier = Modifier
                            .width(10.dp)
                    )
                    FilledTonalIconButton(
                        onClick = { updateZoomFromCenter(scale - 0.1f) },
                        content = { Icon(Icons.Rounded.Remove, null) }
                    )
                    FilledTonalIconButton(
                        onClick = { updateZoomFromCenter(scale + 0.1f) },
                        content = { Icon(Icons.Rounded.Add, null) }
                    )
                }
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    Content(
        navigationState = NavigationState(),
        state = SchemeState(),
        onAction = {}
    )
}