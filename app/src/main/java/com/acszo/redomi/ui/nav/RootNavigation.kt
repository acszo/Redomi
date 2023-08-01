package com.acszo.redomi.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acszo.redomi.ui.component.BackButton
import com.acszo.redomi.ui.settings.AppsPage
import com.acszo.redomi.ui.settings.LayoutPage
import com.acszo.redomi.ui.settings.SettingsPage

@Composable
fun RootNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SettingsPage.route,
    ) {
        composable(route = Route.SettingsPage.route) {
            SettingsPage(navController = navController)
        }
        composable(route = Route.AppsPage.route) {
            AppsPage {
                BackButton { navController.popBackStack() }
            }
        }
        composable(route = Route.LayoutPage.route) {
            LayoutPage {
                BackButton { navController.popBackStack() }
            }
        }
    }

}