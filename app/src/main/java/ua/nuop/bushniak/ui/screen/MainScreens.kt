package ua.nuop.bushniak.ui.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ua.nuop.bushniak.R
import ua.nuop.bushniak.navigation.destination.ApplicationDestination
import ua.nuop.bushniak.ui.component.BottomBar
import ua.nuop.bushniak.ui.component.BottomItemInfo
import ua.nuop.bushniak.viewModel.CreateNoteViewModel
import ua.nuop.bushniak.viewModel.MainScreensViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreens(viewModel: MainScreensViewModel = hiltViewModel()) {
    val navController = rememberNavController()
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
    Scaffold(bottomBar = {
        if (bottomItems.any { currentDestination?.route == it.destination }) {
            BottomBar(navController = navController)
        }
    },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "NotesApp") },
                modifier = Modifier.padding(horizontal = 16.dp),
                actions = {
                    if (currentDestination?.route == ApplicationDestination.MainScreens.Notes.route) {
                        Row(
                            modifier = Modifier.height(24.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    viewModel.synchronize()
                                })

                            Image(
                                painter = painterResource(id = R.drawable.ic_mask),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(LocalContentColor.current),
                                modifier = Modifier.clickable {
                                    navController.navigate(ApplicationDestination.MainScreens.PasswordInput.route)
                                }
                            )
                            Image(
                                painter = painterResource(id = R.drawable.ic_trash),
                                contentDescription = null,
                                modifier = Modifier.clickable {
                                    navController.navigate(ApplicationDestination.MainScreens.Trash.route)
                                },
                                colorFilter = ColorFilter.tint(LocalContentColor.current)
                            )

                        }
                    }
                })
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = ApplicationDestination.MainScreens.Notes.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(ApplicationDestination.MainScreens.Notes.route) {
                Notes(navController = navController)
            }
            composable(ApplicationDestination.MainScreens.Settings.route) {
                Settings(navController = navController)
            }

            composable(ApplicationDestination.MainScreens.Trash.route) {
                Trash(navController)
            }

            composable(ApplicationDestination.MainScreens.PasswordInput.route) {
                Password(navController = navController)
            }

            composable(ApplicationDestination.MainScreens.CreatePassword.route) {
                CreatePassword(navController = navController)
            }

            composable(ApplicationDestination.MainScreens.PrivateNotes.route) {
                Private(navController)
            }

            composable("${ApplicationDestination.MainScreens.CreateNote.route}?note={id}",
                arguments = listOf(
                    navArgument("id") {
                        Log.d("navigation", "here")
                        defaultValue = -1L
                        type = NavType.LongType
                    }
                )) { args ->

                val noteId = args.arguments?.getLong("id")

                Log.d("mainScreens", noteId.toString())

                val createNoteViewModel: CreateNoteViewModel =
                    hiltViewModel(creationCallback = { factory: CreateNoteViewModel.Factory ->
                        factory.create(if (noteId == -1L) null else noteId)
                    })
                Create(navController = navController, createNoteViewModel)
            }


        }
    }
}