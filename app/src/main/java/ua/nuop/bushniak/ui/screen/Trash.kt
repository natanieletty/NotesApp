package ua.nuop.bushniak.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import ua.nuop.bushniak.navigation.destination.ApplicationDestination
import ua.nuop.bushniak.ui.component.NoteDeletedCard
import ua.nuop.bushniak.viewModel.TrashViewModel

@Composable
fun Trash(navController: NavController, viewModel: TrashViewModel = hiltViewModel()) {
    val notes by viewModel.notes.collectAsStateWithLifecycle()

    Scaffold { innerPaddings ->

        if (notes.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Box {
                    Text(text = "Trash is empty")
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

            items(notes) {
                if (it.id != null) {
                    NoteDeletedCard(it, onClick = {
                        navController.navigate("${ApplicationDestination.MainScreens.CreateNote.route}?note=${it.id}")
                    },
                        onDelete = {
                            viewModel.deleteNote(it.id)

                        },
                        onRestore = {
                            viewModel.restoreNote(it.id)
                        })
                }
            }
        }


    }

}