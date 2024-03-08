package com.acszo.redomi.ui.nav

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acszo.redomi.ui.component.BackButton
import com.acszo.redomi.ui.page.AppsPage
import com.acszo.redomi.ui.page.LayoutPage
import com.acszo.redomi.ui.page.SettingsPage
import com.acszo.redomi.ui.page.UpdatePage
import com.acszo.redomi.viewmodel.DataStoreViewModel
import com.acszo.redomi.viewmodel.UpdateViewModel

const val initialOffset = 0.10f

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
    ) {
        navigationComposable(
            route = Route.SettingsPage.route
        ) {
            SettingsPage(
                dataStoreViewModel = dataStoreViewModel,
                navController = navController,
                isUpdateAvailable = isUpdateAvailable
            )
        }
        navigationComposable(
            route = Route.AppsPage.route
        ) {
            AppsPage(
                dataStoreViewModel = dataStoreViewModel,
            ) {
                BackButton { navController.popBackStack() }
            }
        }
        navigationComposable(
            route = Route.LayoutPage.route
        ) {
            LayoutPage(
                dataStoreViewModel = dataStoreViewModel,
            ) {
                BackButton { navController.popBackStack() }
            }
        }
        navigationComposable(
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

fun NavGraphBuilder.navigationComposable(
    route: String,
    content: @Composable (AnimatedContentScope.(NavBackStackEntry) -> Unit)
) = composable(
    route = route,
    enterTransition = { enterTransition(1) },
    exitTransition = { exitTransition(-1) },
    popEnterTransition = { enterTransition(-1) },
    popExitTransition = { exitTransition(1) },
    content = content,
)

fun enterTransition(sign: Int): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = 210,
            delayMillis = 90,
            easing = LinearOutSlowInEasing
        )
    ) + slideInHorizontally(
        animationSpec = tween(durationMillis = 300)
    ) {
        sign * (it * initialOffset).toInt()
    }
}

fun exitTransition(sign: Int): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = 90,
            easing = FastOutLinearInEasing
        )
    ) + slideOutHorizontally(
        animationSpec = tween(durationMillis = 300)
    ) {
        sign * (it * initialOffset).toInt()
    }
}
