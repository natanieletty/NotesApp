package ua.nuop.bushniak.ui.screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import ua.nuop.bushniak.helper.PreventScreenshot
import ua.nuop.bushniak.navigation.destination.ApplicationDestination
import ua.nuop.bushniak.viewModel.CreateNoteViewModel

@Composable
fun Create(navController: NavController, viewModel: CreateNoteViewModel) {
    val isPrivate by viewModel.isPrivate.collectAsState()

    val state = viewModel.richTextState

    LaunchedEffect(Unit) {
        if (navController.previousBackStackEntry?.destination?.route == ApplicationDestination.MainScreens.PrivateNotes.route) {
            viewModel.updateIsPrivate(true)
        }
    }

    PreventScreenshot()
    Column(modifier = Modifier.padding(16.dp)) {
        Row(modifier = Modifier.clickable { viewModel.updateIsPrivate(!isPrivate) }) {
            Checkbox(checked = isPrivate, onCheckedChange = null)
            Text(text = "Private")
        }


        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { state.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold)) }) {
                Text(text = "B")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { state.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic)) }) {
                Text(text = "I")
            }
            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { state.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline)) }) {
                Text(text = "U")
            }

        }
        RichTextEditor(
            modifier = Modifier
                .fillMaxWidth()
                .weight(8f),
            state = state,
        )

        Button(onClick = {
            Log.d("create", state.toHtml())
            viewModel.addNote(state.toHtml())
            navController.popBackStack()
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Save")
        }
    }
}

