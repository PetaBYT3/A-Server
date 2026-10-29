package com.a.server.presentation.mapper

import com.a.server.core.util.millisToDuration
import com.a.server.domain.model.server.ServerModel
import com.a.server.presentation.model.server.ServerUi

object ServerMapper: PresentationMapper<ServerModel, ServerUi> {
    override fun toUi(model: ServerModel): ServerUi {
        return ServerUi(
            isRunning = model.isRunning,
            uptime = model.uptime.millisToDuration()
        )
    }

    override fun toModel(ui: ServerUi): ServerModel {
        return ServerModel(
            isRunning = ui.isRunning,
            uptime = 0L
        )
    }
}