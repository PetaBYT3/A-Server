package com.a.server.data.mapper

interface DataMapper<DTO, MODEL> {
    fun toModel(dto: DTO): MODEL
    fun toDto(model: MODEL): DTO
}