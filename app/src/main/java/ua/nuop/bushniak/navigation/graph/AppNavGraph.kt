package ua.nuop.bushniak.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ua.nuop.bushniak.navigation.destination.ApplicationDestination
import ua.nuop.bushniak.ui.screen.MainScreens


@Composable
fun AppNavGraph(startDestination: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(ApplicationDestination.MainScreens.route){
            MainScreens()
        }

    }
}