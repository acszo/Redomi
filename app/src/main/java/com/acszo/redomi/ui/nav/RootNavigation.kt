package com.acszo.redomi.ui.nav

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
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
fun DecideLayout(
    windowSizeClass: WindowSizeClass,
    dataStoreViewModel: DataStoreViewModel,
    updateViewModel: UpdateViewModel,
    isUpdateAvailable: Boolean
) {
    val navController = rememberNavController()

    val isCompactWidth = windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact
    val isCompactHeight = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact

    if (isCompactWidth || isCompactHeight) {
        RootNavigation(
            navController = navController,
            isCompact = true,
            startDestination = Route.SettingsPage.route,
            dataStoreViewModel = dataStoreViewModel,
            updateViewModel = updateViewModel,
            isUpdateAvailable = isUpdateAvailable,
        )
    } else {
        Row {
            SettingsPage(
                modifier = Modifier.weight(1.15f),
                dataStoreViewModel = dataStoreViewModel,
                navigateTo = {
                    navController.navigate(it) {
                        launchSingleTop = true
                    }
                },
                isUpdateAvailable = isUpdateAvailable
            )

            RootNavigation(
                modifier = Modifier.weight(2f),
                navController = navController,
                isCompact = false,
                startDestination = Route.AppsPage.route,
                dataStoreViewModel = dataStoreViewModel,
                updateViewModel = updateViewModel,
                isUpdateAvailable = isUpdateAvailable,
            )
        }
    }
}

@Composable
fun RootNavigation(
    modifier: Modifier = Modifier,
    isCompact: Boolean,
    navController: NavHostController,
    startDestination: String,
    dataStoreViewModel: DataStoreViewModel,
    updateViewModel: UpdateViewModel,
    isUpdateAvailable: Boolean
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = { enterHorizontalTransition(TransitionDirection.FORWARD) },
        exitTransition = { exitHorizontalTransition(TransitionDirection.BACKWARD) },
        popEnterTransition = { enterHorizontalTransition(TransitionDirection.BACKWARD) },
        popExitTransition = { exitHorizontalTransition(TransitionDirection.FORWARD) },
    ) {
        if (isCompact) {
            composable(
                route = Route.SettingsPage.route
            ) {
                SettingsPage(
                    dataStoreViewModel = dataStoreViewModel,
                    navigateTo = { navController.navigate(it) },
                    isUpdateAvailable = isUpdateAvailable
                )
            }
        }
        composable(
            route = Route.AppsPage.route
        ) {
            AppsPage(
                dataStoreViewModel = dataStoreViewModel,
            ) {
                if (isCompact) {
                    BackButton { navController.popBackStack() }
                }
            }
        }
        composable(
            route = Route.LayoutPage.route
        ) {
            LayoutPage(
                dataStoreViewModel = dataStoreViewModel,
            ) {
                if (isCompact) {
                    BackButton { navController.popBackStack() }
                }
            }
        }

        if (isGithubBuild) {
            composable(
                route = Route.UpdatePage.route
            ) {
                UpdatePage(
                    updateViewModel = updateViewModel
                ) {
                    if (isCompact) {
                        BackButton { navController.popBackStack() }
                    }
                }
            }
        }
    }

}