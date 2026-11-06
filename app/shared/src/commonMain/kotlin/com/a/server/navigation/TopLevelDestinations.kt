package com.a.server.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import aserver.app.shared.generated.resources.Res
import aserver.app.shared.generated.resources.authentication
import aserver.app.shared.generated.resources.dashboard
import aserver.app.shared.generated.resources.dashboard_24
import aserver.app.shared.generated.resources.database
import aserver.app.shared.generated.resources.database_24px
import aserver.app.shared.generated.resources.storage
import aserver.app.shared.generated.resources.storage_24px
import com.a.server.presentation.model.IconUi
import com.a.server.presentation.model.TextUi

enum class TopLevelDestinations(
    val key: NavigationKey,
    val icon: IconUi,
    val label: TextUi
) {
    DASHBOARD(
        key = NavigationKey.DashboardMain,
        icon = IconUi.Resource(Res.drawable.dashboard_24),
        label = TextUi.Resource(Res.string.dashboard)
    ),
    AUTHENTICATION(
        key = NavigationKey.AuthenticationList,
        icon = IconUi.Vector(Icons.Rounded.Lock),
        label = TextUi.Resource(Res.string.authentication)
    ),
    DATABASE(
        key = NavigationKey.DatabaseList,
        icon = IconUi.Resource(Res.drawable.database_24px),
        label = TextUi.Resource(Res.string.database)
    ),
    STORAGE(
        key = NavigationKey.StorageMain,
        icon = IconUi.Resource(Res.drawable.storage_24px),
        label = TextUi.Resource(Res.string.storage)
    )
}