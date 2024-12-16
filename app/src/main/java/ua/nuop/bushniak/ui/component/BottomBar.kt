package ua.nuop.bushniak.ui.component

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ua.nuop.bushniak.R
import ua.nuop.bushniak.navigation.destination.ApplicationDestination

@Composable
fun BottomBar(navController: NavController) {
    val bottomItems = listOf(
        BottomItemInfo(

            ApplicationDestination.MainScreens.Notes.route,
            "Notes",
            R.drawable.ic_list
        ),
        BottomItemInfo(
            ApplicationDestination.MainScreens.Settings.route,
            "Settings",
            R.drawable.baseline_settings_24
        )
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    BottomAppBar {
        bottomItems.forEach { bottomItem ->
            NavigationBarItem(selected = currentDestination?.route == bottomItem.destination,
                onClick = {
                    navController.navigate(bottomItem.destination) {
                        popUpTo(ApplicationDestination.MainScreens.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true

                    }
                    Log.d("navigation", navController.currentDestination?.route.toString())
                },
                icon = {
                    Image(
                        painter = painterResource(id = bottomItem.icon),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(LocalContentColor.current)
                    )
                },
                label = { Text(text = bottomItem.label) })

        }
    }
}


data class BottomItemInfo(val destination: String, val label: String, val icon: Int)