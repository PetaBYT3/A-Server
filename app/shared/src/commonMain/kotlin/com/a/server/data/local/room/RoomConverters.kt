package com.a.server.data.local.room

import androidx.compose.ui.geometry.Offset
import androidx.room3.ColumnTypeConverter
import androidx.room3.ProvidedColumnTypeConverter
import com.a.server.core.type.DataType

@ProvidedColumnTypeConverter
class RoomConverters {
    @ColumnTypeConverter
    fun fromOffset(offset: Offset): String {
        return "${offset.x},${offset.y}"
    }

    @ColumnTypeConverter
    fun toOffset(offset: String): Offset {
        val parts = offset.split(",")
        val x = parts.getOrNull(0)?.toFloatOrNull() ?: 0f
        val y = parts.getOrNull(1)?.toFloatOrNull() ?: 0f
        return Offset(x, y)
    }

    @ColumnTypeConverter
    fun fromSqlDataType(dataType: DataType): String {
        return dataType.name
    }

    @ColumnTypeConverter
    fun toSqlDataType(name: String): DataType {
        return DataType.valueOf(name)
    }
}