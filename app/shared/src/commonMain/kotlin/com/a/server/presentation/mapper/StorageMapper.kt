package com.a.server.presentation.mapper

import com.a.server.core.util.toReadableSize
import com.a.server.domain.model.storage.StorageModel
import com.a.server.presentation.model.storage.StorageUi

object StorageMapper: PresentationMapper<StorageModel, StorageUi> {
    override fun toUi(model: StorageModel): StorageUi {
        return StorageUi(
            usedAndFreeSize = "${model.usedSpace.toReadableSize()} / ${model.freeSpace.toReadableSize()}"
        )
    }

    override fun toModel(ui: StorageUi): StorageModel {
        return StorageModel()
    }
}