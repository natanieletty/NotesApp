package ua.nuop.bushniak.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.text.parseAsHtml
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox
import ua.nuop.bushniak.R
import ua.nuop.bushniak.entity.Note

@Composable
fun NoteDefaultCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onToPrivate: () -> Unit,

    ) {
    val delete = SwipeAction(
        onSwipe = {
            onDelete()
        },
        background = Color.Red,
        icon = {
            Row {

                Spacer(modifier = Modifier.width(16.dp))
                Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))

            }
        }
    )

    val toPrivate = SwipeAction(
        onSwipe = {
            onToPrivate()
        },
        background = Color.Magenta,
        icon = {
            Spacer(modifier = Modifier.width(16.dp))
            Icon(painter = painterResource(R.drawable.ic_mask), contentDescription = null)
            Spacer(modifier = Modifier.width(16.dp))

        }
    )

    SwipeableActionsBox(endActions = listOf(delete), startActions = listOf(toPrivate)) {
        NoteCard(text = note.text.parseAsHtml().toString()) {
            onClick()
        }
    }
}