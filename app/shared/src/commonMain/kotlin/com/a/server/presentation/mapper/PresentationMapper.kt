package com.a.server.presentation.mapper

interface PresentationMapper<Model, Ui> {
    fun toUi(model: Model): Ui
    fun toModel(ui: Ui): Model
}