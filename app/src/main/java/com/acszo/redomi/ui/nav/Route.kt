package com.acszo.redomi.ui.nav

import com.acszo.redomi.ui.nav.Pages.appsPage
import com.acszo.redomi.ui.nav.Pages.layoutPage
import com.acszo.redomi.ui.nav.Pages.settingsPage

sealed class Route(var route: String) {

    object SettingsPage: Route(settingsPage)
    object AppsPage: Route(appsPage)
    object LayoutPage: Route(layoutPage)

}
