package ua.nuop.bushniak.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ua.nuop.bushniak.R
import ua.nuop.bushniak.helper.PreventScreenshot
import ua.nuop.bushniak.navigation.destination.ApplicationDestination
import ua.nuop.bushniak.viewModel.CreatePasswordViewModel

@Composable
fun CreatePassword(
    navController: NavController,
    viewModel: CreatePasswordViewModel = hiltViewModel()
) {

    PreventScreenshot()

    var isPasswordShowed by remember {
        mutableStateOf(false)
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


    val password by viewModel.password.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Text(text = "Create password", textAlign = TextAlign.Center)

                OutlinedTextField(
                    value = password,
                    onValueChange = viewModel::updatePassword,
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

                    )

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(onClick = {
                        navController.popBackStack()
                    }) {
                        Text(text = "Cancel")
                    }

                    Button(onClick = {
                        viewModel.setPassword()
                        navController.popBackStack()
                    }) {
                        Text(text = "Ok")
                    }

                }
            }
        }
    }

}