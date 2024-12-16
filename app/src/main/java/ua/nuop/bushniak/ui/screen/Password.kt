package ua.nuop.bushniak.ui.screen

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ua.nuop.bushniak.R
import ua.nuop.bushniak.helper.PreventScreenshot
import ua.nuop.bushniak.navigation.destination.ApplicationDestination
import ua.nuop.bushniak.viewModel.PasswordViewModel

@Composable
fun Password(navController: NavController, viewModel: PasswordViewModel = hiltViewModel()) {


    val isPasswordSet by viewModel.isPasswordSet.collectAsState()

    LaunchedEffect(isPasswordSet) {
        Log.d("Password", isPasswordSet.toString())
        if (!isPasswordSet) {
            navController.navigate(ApplicationDestination.MainScreens.CreatePassword.route) {
                popUpTo(ApplicationDestination.MainScreens.PasswordInput.route) {
                    inclusive = true
                }
            }
        }
    }

    val isPasswordValid by viewModel.isPasswordValid.collectAsState()
    LaunchedEffect(isPasswordValid) {
        if (isPasswordValid == true) {
            navController.navigate(ApplicationDestination.MainScreens.PrivateNotes.route) {
                popUpTo(ApplicationDestination.MainScreens.PasswordInput.route) {
                    inclusive = true
                }
            }
        }
    }

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

    PreventScreenshot()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Box(contentAlignment = Alignment.Center) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.width(IntrinsicSize.Max)
            ) {
                Text(text = "Enter password", textAlign = TextAlign.Center)

                OutlinedTextField(
                    value = password,
                    onValueChange = viewModel::updatePasswordField,
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

                AnimatedVisibility(visible = isPasswordValid == false) {
                    Text(text = "Password incorrect", color = Color.Red)
                }

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
                        viewModel.validatePassword()
                    }) {
                        Text(text = "Ok")
                    }

                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun PasswordPreview() {
    val navController = rememberNavController()
    Password(navController = navController)
}