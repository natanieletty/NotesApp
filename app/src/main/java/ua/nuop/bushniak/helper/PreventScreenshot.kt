package ua.nuop.bushniak.helper

import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle

@Composable
fun PreventScreenshot(){
    val activity = LocalContext.current  as Activity

    ComposableLifeCycle { source, event ->
        when (event) {
            Lifecycle.Event.ON_START-> {
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE
                )
            }

            Lifecycle.Event.ON_PAUSE -> {
                activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }

            else -> {}
        }
    }
}