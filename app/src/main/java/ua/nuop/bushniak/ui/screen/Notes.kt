package ua.nuop.bushniak.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.text.parseAsHtml
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.room.util.TableInfo
import ua.nuop.bushniak.LocalAppThemeController
import ua.nuop.bushniak.R
import ua.nuop.bushniak.navigation.destination.ApplicationDestination
import ua.nuop.bushniak.ui.component.BottomBar
import ua.nuop.bushniak.ui.component.NoteDefaultCard
import ua.nuop.bushniak.viewModel.NotesViewModel

@Composable
fun Notes(navController: NavController, viewModel: NotesViewModel = hiltViewModel()) {

    val notes by viewModel.notes.collectAsStateWithLifecycle()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ApplicationDestination.MainScreens.CreateNote.route) },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_pen),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(
                        LocalContentColor.current
                    ),
                    modifier = Modifier.size(24.dp)
                )
            }

        }
    ) { innerPaddings ->

        if (notes.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box {
                    Text(text = "No notes")
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .padding(innerPaddings)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
//            item {
//                NoteDefaultCard()
//            }
            items(notes) {
                if (it.id != null) {
                    NoteDefaultCard(it, onClick = {
                        navController.navigate("${ApplicationDestination.MainScreens.CreateNote.route}?note=${it.id}")
                    },
                        onDelete = {
                            viewModel.deleteNote(it.id)

                        },
                        onToPrivate = {
                            viewModel.toggleVisibility(it.id)
                        })
                }
            }
        }
//
//        Column(modifier = Modifier.padding(innerPaddings)) {
//            Button(onClick = { theme.toggle() }) {
//
//            }
//        }

    }
}
