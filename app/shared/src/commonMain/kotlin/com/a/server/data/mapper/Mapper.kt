package com.a.server.data.mapper

interface Mapper<DTO, MODEL> {
    fun toModel(dto: DTO): MODEL
    fun toDto(model: MODEL): DTO
}