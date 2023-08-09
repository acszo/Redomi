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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.acszo.redomi.ui.component.BackButton
import com.acszo.redomi.ui.settings.AppsPage
import com.acszo.redomi.ui.settings.LayoutPage
import com.acszo.redomi.ui.settings.SettingsPage

const val initialOffset = 0.10f

@Composable
fun RootNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Route.SettingsPage.route,
    ) {
        navigationComposable(
            route = Route.SettingsPage.route
        ) {
            SettingsPage(navController = navController)
        }
        navigationComposable(
            route = Route.AppsPage.route
        ) {
            AppsPage {
                BackButton { navController.popBackStack() }
            }
        }
        navigationComposable(
            route = Route.LayoutPage.route
        ) {
            LayoutPage {
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
    enterTransition = { enterTransition(initialOffset) },
    exitTransition = { exitTransition(-initialOffset) },
    popEnterTransition = { enterTransition(-initialOffset) },
    popExitTransition = { exitTransition(initialOffset) },
    content = content,
)

fun enterTransition(
    initialOffset: Float
): EnterTransition {
    return fadeIn(
        animationSpec = tween(
            durationMillis = 210,
            delayMillis = 90,
            easing = LinearOutSlowInEasing
        )
    ) + slideInHorizontally(
        animationSpec = tween(durationMillis = 300)
    ) {
        (it * initialOffset).toInt()
    }
}

fun exitTransition(
    targetOffset: Float
): ExitTransition {
    return fadeOut(
        animationSpec = tween(
            durationMillis = 90,
            easing = FastOutLinearInEasing
        )
    ) + slideOutHorizontally(
        animationSpec = tween(durationMillis = 300)
    ) {
        (it * targetOffset).toInt()
    }
}
