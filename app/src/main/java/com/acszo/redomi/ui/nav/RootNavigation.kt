package com.acszo.redomi.ui.nav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acszo.redomi.isGithubBuild
import com.acszo.redomi.ui.common.BackButton
import com.acszo.redomi.ui.common.TransitionDirection
import com.acszo.redomi.ui.common.enterHorizontalTransition
import com.acszo.redomi.ui.common.exitHorizontalTransition
import com.acszo.redomi.ui.page.settings.SettingsPage
import com.acszo.redomi.ui.page.settings.apps.AppsPage
import com.acszo.redomi.ui.page.settings.layout.LayoutPage
import com.acszo.redomi.ui.page.settings.update.UpdatePage
import com.acszo.redomi.viewmodel.DataStoreViewModel
import com.acszo.redomi.viewmodel.UpdateViewModel

@Composable
fun RootNavigation(
    dataStoreViewModel: DataStoreViewModel,
    updateViewModel: UpdateViewModel,
    isUpdateAvailable: Boolean
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SettingsPage.route,
        enterTransition = { enterHorizontalTransition(TransitionDirection.FORWARD) },
        exitTransition = { exitHorizontalTransition(TransitionDirection.BACKWARD) },
        popEnterTransition = { enterHorizontalTransition(TransitionDirection.BACKWARD) },
        popExitTransition = { exitHorizontalTransition(TransitionDirection.FORWARD) },
    ) {
        composable(
            route = Route.SettingsPage.route
        ) {
            SettingsPage(
                dataStoreViewModel = dataStoreViewModel,
                navController = navController,
                isUpdateAvailable = isUpdateAvailable
            )
        }
        composable(
            route = Route.AppsPage.route
        ) {
            AppsPage(
                dataStoreViewModel = dataStoreViewModel,
            ) {
                BackButton { navController.popBackStack() }
            }
        }
        composable(
            route = Route.LayoutPage.route
        ) {
            LayoutPage(
                dataStoreViewModel = dataStoreViewModel,
            ) {
                BackButton { navController.popBackStack() }
            }
        }

        if (isGithubBuild) {
            composable(
                route = Route.UpdatePage.route
            ) {
                UpdatePage(
                    updateViewModel = updateViewModel
                ) {
                    BackButton { navController.popBackStack() }
                }
            }
        }
    }

}