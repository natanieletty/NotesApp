package ua.nuop.bushniak.ui.screen

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import ua.nuop.bushniak.LocalAppThemeController
import ua.nuop.bushniak.R
import ua.nuop.bushniak.ui.modal.SetPasswordDialog
import ua.nuop.bushniak.ui.modal.UpdatePasswordDialog
import ua.nuop.bushniak.viewModel.SettingsViewModel


@Composable
fun Settings(navController: NavController, viewModel: SettingsViewModel = hiltViewModel()) {
    val currentScheme by LocalAppThemeController.current.isDarkEnabled.collectAsState()

    val context = LocalContext.current
    val email by viewModel.email.collectAsState()
    val isPasswordSet by viewModel.isPasswordSet.collectAsState()
    var showPasswordDialog by remember {
        mutableStateOf(false)
    }

    val isPasswordUpdatedSuccessfully = viewModel.isPasswordUpdatedSuccessfully.collectAsState()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) {
            val account = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val result = account.result
                val credentials = GoogleAuthProvider.getCredential(result.idToken, null)
                Log.d("settings", account.result.email.toString())
                Log.d("settings", account.result.idToken.toString())
                Log.d("settings", account.result.isExpired.toString())
                viewModel.googleLogin(credentials)
            } catch (it: Exception) {
                print(it)
            }
        }


    LaunchedEffect(isPasswordUpdatedSuccessfully.value) {
        if(isPasswordUpdatedSuccessfully.value == true){
            showPasswordDialog = false
            viewModel.resetIsPasswordUpdatedSuccessfully()
        }
    }


    if (showPasswordDialog) {
        if (isPasswordSet) {
            UpdatePasswordDialog(
                onDismissRequest = { showPasswordDialog = false },
                onSubmitRequest = { old, new ->
                    viewModel.updatePassword(old, new)

                },
                hasNoErrors = isPasswordUpdatedSuccessfully
            )
        } else {
            SetPasswordDialog(
                onDismissRequest = { showPasswordDialog = false },
                onSubmitRequest = { it ->
                    viewModel.setPassword(it)
                    showPasswordDialog = false


                },

                )
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Dark theme")
                Switch(checked = currentScheme, onCheckedChange = { viewModel.toggleScheme() })
            }
        }
        item {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    val gso = GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(context.getString(R.string.web_client_id))
                        .build()

                    val googleSingInClient = GoogleSignIn.getClient(context, gso)

                    launcher.launch(googleSingInClient.signInIntent)
                }) {
                Text(text = "Google account")

                Text(text = email)
            }
        }

        item {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showPasswordDialog = true
                }) {
                Text(text = "Set password to private notes")
            }
        }
    }
}