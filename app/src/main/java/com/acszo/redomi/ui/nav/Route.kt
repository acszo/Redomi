package com.acszo.redomi.ui.nav

import com.acszo.redomi.ui.nav.Pages.APPS_PAGE
import com.acszo.redomi.ui.nav.Pages.LAYOUT_PAGE
import com.acszo.redomi.ui.nav.Pages.SETTINGS_PAGE
import com.acszo.redomi.ui.nav.Pages.UPDATE_PAGE

sealed class Route(var route: String) {

    data object SettingsPage: Route(SETTINGS_PAGE)
    data object AppsPage: Route(APPS_PAGE)
    data object LayoutPage: Route(LAYOUT_PAGE)
    data object UpdatePage: Route(UPDATE_PAGE)

}
