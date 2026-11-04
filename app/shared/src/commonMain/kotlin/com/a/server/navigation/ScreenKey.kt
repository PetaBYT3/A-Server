package com.a.server.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.authentication
import aserver.app.shared.generated.resources.dashboard
import aserver.app.shared.generated.resources.dashboard_24
import aserver.app.shared.generated.resources.database_24px
import aserver.app.shared.generated.resources.sqlite_database
import aserver.app.shared.generated.resources.storage
import aserver.app.shared.generated.resources.storage_24px
import com.a.server.presentation.model.IconUi
import com.a.server.presentation.model.TextUi

enum class ScreenKey(
    val label: TextUi,
    val icon: IconUi
) {
    DASHBOARD(
        TextUi.Resource(Res.string.dashboard),
        IconUi.Resource(Res.drawable.dashboard_24)
    ),
    AUTHENTICATION(
        TextUi.Resource(Res.string.authentication),
        IconUi.Vector(Icons.Rounded.Lock)
    ),
    DATABASE(
        TextUi.Resource(Res.string.sqlite_database),
        IconUi.Resource(Res.drawable.database_24px)
    ),
    STORAGE(
        TextUi.Resource(Res.string.storage),
        IconUi.Resource(Res.drawable.storage_24px)
    )
}