package com.mgpark.trackbox.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mgpark.trackbox.ui.add.AddScreen
import com.mgpark.trackbox.ui.detail.DetailScreen
import com.mgpark.trackbox.ui.home.HomeScreen

@Composable
fun TrackBoxNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
    ) {
        composable<HomeRoute> {
            HomeScreen(
                onAddClick = { navController.navigate(AddRoute) },
                onTrackingClick = { id -> navController.navigate(DetailRoute(id)) },
            )
        }
        composable<AddRoute> {
            AddScreen(
                onBack = { navController.popBackStack() },
                onAdded = { navController.popBackStack() },
            )
        }
        composable<DetailRoute> {
            DetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
