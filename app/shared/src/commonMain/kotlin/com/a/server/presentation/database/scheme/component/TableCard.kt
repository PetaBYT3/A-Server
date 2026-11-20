package com.a.server.presentation.database.scheme.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import com.a.server.domain.model.sqlite.TableModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import kotlin.math.roundToInt

@Composable
fun TableCard(
    tableWithFieldsModel: TableWithFieldsModel,
    positionProvider: () -> Offset,
    onDrag: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    onSizeMeasured: (IntSize) -> Unit
) {


    val currentOnDrag by rememberUpdatedState(onDrag)
    val currentOnDragEnd by rememberUpdatedState(onDragEnd)

    Column(
        modifier = Modifier
            .offset {
                val pos = positionProvider()
                IntOffset(pos.x.roundToInt(), pos.y.roundToInt())
            }
            .width(240.dp)
            .onSizeChanged { onSizeMeasured(it) }
            .pointerInput(tableWithFieldsModel.table.name) {
                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        change.consume()
                        currentOnDrag(dragAmount)
                    },
                    onDragEnd = { currentOnDragEnd() },
                    onDragCancel = { currentOnDragEnd() }
                )
            },
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        SegmentedListItem(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            shapes = ListItemDefaults.segmentedShapes(
                index = 0,
                count = 2
            ),
            content = { Text(text = tableWithFieldsModel.table.name) }
        )
        SegmentedListItem(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            ),
            shapes = ListItemDefaults.segmentedShapes(
                index = 1,
                count = 2
            ),
            content = {
                Column {
                    tableWithFieldsModel.fields.fastForEachIndexed { index, fieldModel ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 5.dp, bottom = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = fieldModel.name)
                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                            )
                            if (fieldModel.primary) {
                                Icon(Icons.Rounded.Key, null)
                            }
                            Text(text = fieldModel.type.name)
                        }
                        if (index != tableWithFieldsModel.fields.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier
                                    .height(DividerDefaults.Thickness)
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
@Preview
private fun Preview() {
    TableCard(
        tableWithFieldsModel = TableWithFieldsModel(
            table = TableModel(
                name = "hero_table",
                position = Offset(0f, 0f)
            ),
            fields = emptyList()
        ),
        positionProvider = { Offset(0f, 0f) },
        onDrag = {},
        onDragEnd = {},
        onSizeMeasured = {}
    )
}