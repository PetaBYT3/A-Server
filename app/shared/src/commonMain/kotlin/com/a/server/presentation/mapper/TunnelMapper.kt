package com.a.server.presentation.mapper

import com.a.server.core.util.millisToDuration
import com.a.server.domain.model.tunnel.TunnelModel
import com.a.server.presentation.model.tunnel.TunnelUi

object TunnelMapper: PresentationMapper<TunnelModel, TunnelUi> {
    override fun toUi(model: TunnelModel): TunnelUi {
        return TunnelUi(
            isRunning = model.isRunning,
            uptime = model.uptime.millisToDuration(),
            publicUrl = model.publicUrl
        )
    }

    override fun toModel(ui: TunnelUi): TunnelModel {
        return TunnelModel(
            isRunning = ui.isRunning,
            uptime = 0L,
            publicUrl = ui.publicUrl
        )
    }
}