package com.acszo.redomi.ui.nav

import com.acszo.redomi.ui.nav.Pages.appsPage
import com.acszo.redomi.ui.nav.Pages.layoutPage
import com.acszo.redomi.ui.nav.Pages.settingsPage
import com.acszo.redomi.ui.nav.Pages.updatePage

sealed class Route(var route: String) {

    data object SettingsPage: Route(settingsPage)
    data object AppsPage: Route(appsPage)
    data object LayoutPage: Route(layoutPage)
    data object UpdatePage: Route(updatePage)

}
