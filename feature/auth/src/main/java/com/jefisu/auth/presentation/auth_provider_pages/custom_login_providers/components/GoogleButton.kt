package com.jefisu.auth.presentation.auth_provider_pages.custom_login_providers.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jefisu.auth.BuildConfig
import com.jefisu.auth.R
import com.jefisu.auth.data.AuthMessage
import com.jefisu.ui.components.ButtonProperties
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.components.StandardButton
import com.jefisu.ui.theme.Gray80
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun GoogleButton(
    onSuccessfulLogin: () -> Unit,
    onFailureLogin: (AuthMessage.Error) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val credentialManager = remember { CredentialManager.create(context) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val contentColor = Gray80

    val onClickSignIn: () -> Unit = {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(true)
                    .build()
            )
            .build()

        scope.launch {
            isLoading = true
            try {
                val result = credentialManager.getCredential(context, request)
                val credential = result.credential
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                GoogleAuthProvider
                    .getCredential(googleIdTokenCredential.idToken, null)
                    .also {
                        Firebase.auth.signInWithCredential(it).await()
                    }

                onSuccessfulLogin()
            } catch (e: FirebaseAuthUserCollisionException) {
                onFailureLogin(AuthMessage.Error.USER_ALREADY_EXISTS)
            } catch (e: Exception) {
                onFailureLogin(AuthMessage.Error.GOOGLE_FAILED_TO_LOGIN)
            } finally {
                isLoading = false
            }
        }
    }

    StandardButton(
        modifier = modifier,
        text = stringResource(id = R.string.sign_up_with, "Google"),
        properties = ButtonProperties(
            leadingIconRes = R.drawable.ic_google,
            type = ButtonType.DynamicColor(
                containerColor = Color.White,
                contentColor = contentColor
            ),
            isLoading = isLoading
        ),
        onClick = onClickSignIn
    )
}