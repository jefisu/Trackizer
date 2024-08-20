package com.jefisu.auth.presentation.custom_auth_provider.components

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.tooling.preview.Preview
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
import com.jefisu.auth.domain.AuthMessage
import com.jefisu.designsystem.Gray80
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerButton
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
internal fun GoogleButton(
    onSuccessAuth: () -> Unit,
    onFailureAuth: (AuthMessage) -> Unit,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val credentialManager = remember { CredentialManager.create(context) }

    val onClickSignIn: () -> Unit = {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(true)
                    .build(),
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

                onSuccessAuth()
            } catch (e: FirebaseAuthUserCollisionException) {
                onFailureAuth(AuthMessage.Error.USER_ALREADY_EXISTS)
            } catch (e: Exception) {
                onFailureAuth(AuthMessage.Error.GOOGLE_FAILED_TO_LOGIN)
            } finally {
                isLoading = false
            }
        }
    }

    TrackizerButton(
        text = stringResource(id = R.string.sign_up_with, "Google"),
        type = ButtonType.Dynamic(
            container = Color.White,
            content = Gray80,
        ),
        leadingIconRes = R.drawable.ic_google,
        onClick = onClickSignIn,
        isLoading = isLoading,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun GoogleButtonPreview() {
    TrackizerTheme {
        GoogleButton(
            onSuccessAuth = {},
            onFailureAuth = {},
        )
    }
}
