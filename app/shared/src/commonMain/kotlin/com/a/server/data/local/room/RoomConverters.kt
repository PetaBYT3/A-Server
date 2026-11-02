package com.a.server.data.local.room

import androidx.room3.ColumnTypeConverter
import androidx.room3.ProvidedColumnTypeConverter
import com.a.server.core.type.SqlDataType

@ProvidedColumnTypeConverter
class RoomConverters {
    @ColumnTypeConverter
    fun fromSqlDataType(sqlDataType: SqlDataType): String {
        return sqlDataType.name
    }

    @ColumnTypeConverter
    fun toSqlDataType(name: String): SqlDataType {
        return SqlDataType.valueOf(name)
    }
}