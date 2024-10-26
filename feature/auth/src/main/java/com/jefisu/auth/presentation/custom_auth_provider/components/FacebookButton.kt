package com.jefisu.auth.presentation.custom_auth_provider.components

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jefisu.auth.R
import com.jefisu.auth.domain.AuthMessage
import com.jefisu.designsystem.FacebookColor
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.domain.util.Result
import com.jefisu.domain.util.onError
import com.jefisu.domain.util.onSuccess
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

typealias FacebookActivityLauncher = ManagedActivityResultLauncher<Collection<String>, CallbackManager.ActivityResultParameters>

@Composable
fun FacebookButton(
    onSuccessAuth: () -> Unit,
    onFailureAuth: (AuthMessage) -> Unit,
) {
    val view = LocalView.current
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var facebookActivityLauncher: FacebookActivityLauncher? = null
    val facebookAuth by remember { lazy { FacebookAuthUi() } }

    if (!view.isInEditMode) {
        facebookActivityLauncher = rememberLauncherForActivityResult(
            contract = with(facebookAuth) {
                loginManager.FacebookLoginActivityResultContract(callbackManager)
            },
            onResult = {},
        )
    }

    val authResult by facebookAuth.observeFlow().collectAsStateWithLifecycle(null)
    LaunchedEffect(authResult) {
        authResult
            ?.onSuccess {
                onSuccessAuth()
                isLoading = false
            }
            ?.onError {
                onFailureAuth(it)
                isLoading = false
            }
    }

    TrackizerButton(
        text = stringResource(id = R.string.sign_up_with, "Facebook"),
        type = ButtonType.Dynamic(
            container = FacebookColor,
            content = Color.White,
        ),
        leadingIconRes = R.drawable.ic_facebook,
        onClick = {
            isLoading = true
            facebookActivityLauncher?.launch(listOf("email", "public_profile"))
        },
        isLoading = isLoading,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Preview
@Composable
private fun FacebookButtonPreview() {
    TrackizerTheme {
        FacebookButton(
            onSuccessAuth = {},
            onFailureAuth = {},
        )
    }
}

private class FacebookAuthUi {
    val loginManager = LoginManager.getInstance()
    val callbackManager = CallbackManager.Factory.create()

    fun observeFlow() = callbackFlow<Result<Unit, AuthMessage>> {
        val callback = object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                launch { send(Result.Error(AuthMessage.Error.FACEBOOK_FAILED_TO_LOGIN)) }
            }

            override fun onError(error: FacebookException) {
                launch { send(Result.Error(AuthMessage.Error.FACEBOOK_FAILED_TO_LOGIN)) }
            }

            override fun onSuccess(result: LoginResult) {
                launch {
                    val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                    runCatching {
                        Firebase.auth.signInWithCredential(credential).await()
                    }.onSuccess {
                        send(Result.Success(Unit))
                    }.onFailure {
                        if (it is FirebaseAuthUserCollisionException) {
                            send(Result.Error(AuthMessage.Error.USER_ALREADY_EXISTS))
                            return@onFailure
                        }
                        send(Result.Error(AuthMessage.Error.FACEBOOK_FAILED_TO_LOGIN))
                    }
                }
            }
        }
        loginManager.registerCallback(callbackManager, callback)
        awaitClose {
            loginManager.unregisterCallback(callbackManager)
        }
    }
}
