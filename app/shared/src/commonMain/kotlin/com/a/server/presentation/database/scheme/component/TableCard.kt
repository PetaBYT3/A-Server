package com.a.server.presentation.database.scheme.component

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.a.server.domain.model.sqlite.TableModel
import com.a.server.domain.model.sqlite.TableWithFieldsModel
import kotlin.math.roundToInt

@Composable
fun TableCard(
    tableWithFieldsModel: TableWithFieldsModel,
    position: Offset,
    onDrag: (Offset) -> Unit,
    onSizeMeasured: (IntSize) -> Unit
) {
    Column(
        modifier = Modifier
            .offset {
                IntOffset(
                    x = position.x.roundToInt(),
                    y = position.y.roundToInt()
                )
            }
            .width(500.dp)
            .onSizeChanged { onSizeMeasured(it) }
            .pointerInput(tableWithFieldsModel.table.name) {
                detectDragGestures { change, dragAmount ->
                    change.consume()
                    onDrag(dragAmount)
                }
            },
        verticalArrangement = Arrangement.spacedBy(ListItemDefaults.SegmentedGap)
    ) {
        SegmentedListItem(
            colors = ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                    tableWithFieldsModel.fields.fastForEach { fieldModel ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
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
                name = "hero_table"
            ),
            fields = emptyList()
        ),
        position = Offset(50f, 100f),
        onDrag = {},
        onSizeMeasured = {}
    )
}