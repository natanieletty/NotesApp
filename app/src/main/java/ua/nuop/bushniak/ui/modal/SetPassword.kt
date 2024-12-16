package ua.nuop.bushniak.ui.modal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import ua.nuop.bushniak.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetPasswordDialog(
    onDismissRequest: () -> Unit,
    onSubmitRequest: (password: String) -> Unit,
) {
    var isPasswordShowed by remember {
        mutableStateOf(false)
    }

    var password by remember {
        mutableStateOf("")
    }

    val iconType by remember {
        derivedStateOf {
            if (isPasswordShowed) {
                R.drawable.ic_eye_off
            } else {
                R.drawable.ic_eye
            }
        }
    }

    BasicAlertDialog(onDismissRequest = { onDismissRequest() }) {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    trailingIcon = {
                        Image(
                            painter = painterResource(id = iconType),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(LocalContentColor.current),
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = null
                            ) {
                                isPasswordShowed = !isPasswordShowed
                            }
                        )
                    },
                    singleLine = true,
                    placeholder = { Text(text = "Password") },
                    visualTransformation = if (isPasswordShowed) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { onSubmitRequest(password) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Set password")
                }
            }
        }
    }
}