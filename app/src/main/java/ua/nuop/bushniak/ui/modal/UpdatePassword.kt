package ua.nuop.bushniak.ui.modal

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.State
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
fun UpdatePasswordDialog(
    onDismissRequest: () -> Unit,
    onSubmitRequest: (oldPassword: String, newPassword: String) -> Unit,
    hasNoErrors: State<Boolean?>
) {
    var isOldPasswordShowed by remember {
        mutableStateOf(false)
    }

    var isNewPasswordShowed by remember {
        mutableStateOf(false)
    }

    var oldPassword by remember {
        mutableStateOf("")
    }
    var newPassword by remember {
        mutableStateOf("")
    }



    BasicAlertDialog(onDismissRequest = { onDismissRequest() }) {
        Surface {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = oldPassword,
                    onValueChange = { oldPassword = it },
                    trailingIcon = {
                        Image(
                            painter = painterResource(
                                id =
                                if (isOldPasswordShowed) {
                                    R.drawable.ic_eye_off
                                } else {
                                    R.drawable.ic_eye

                                }
                            ),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(LocalContentColor.current),
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = null
                            ) {
                                isOldPasswordShowed = !isOldPasswordShowed
                            }
                        )
                    },
                    singleLine = true,
                    placeholder = { Text(text = "Old password") },
                    visualTransformation = if (isOldPasswordShowed) VisualTransformation.None else PasswordVisualTransformation(),

                    )

                Spacer(modifier = Modifier.height(10.dp))


                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    trailingIcon = {
                        Image(
                            painter = painterResource(
                                id = if (isNewPasswordShowed) {
                                    R.drawable.ic_eye_off
                                } else {
                                    R.drawable.ic_eye

                                }
                            ),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(LocalContentColor.current),
                            modifier = Modifier.clickable(
                                interactionSource = null,
                                indication = null
                            ) {
                                isNewPasswordShowed = !isNewPasswordShowed
                            }
                        )
                    },
                    singleLine = true,
                    placeholder = { Text(text = "New password") },
                    visualTransformation = if (isNewPasswordShowed) VisualTransformation.None else PasswordVisualTransformation(),

                    )

                Spacer(modifier = Modifier.height(20.dp))
                AnimatedVisibility(visible = hasNoErrors.value == false) {
                    Text(text = "Old password incorrect", color = Color.Red)
                    Spacer(modifier = Modifier.height(20.dp))
                }
                Button(
                    onClick = { onSubmitRequest(oldPassword, newPassword) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Update password")
                }
            }
        }
    }
}