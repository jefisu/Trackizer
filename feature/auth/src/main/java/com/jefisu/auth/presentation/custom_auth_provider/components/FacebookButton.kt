package com.jefisu.auth.presentation.custom_auth_provider.components

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
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
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerButton
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

typealias FacebookActivityLauncher = ManagedActivityResultLauncher<Collection<String>, CallbackManager.ActivityResultParameters>

@Composable
fun FacebookButton(
    onSuccessAuth: () -> Unit,
    onFailureAuth: (AuthMessage) -> Unit,
) {
    val scope = rememberCoroutineScope()
    val view = LocalView.current
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var facebookActivityLauncher: FacebookActivityLauncher? = null

    if (!view.isInEditMode) {
        val loginManager = remember { LoginManager.getInstance() }
        val callbackManager = remember { CallbackManager.Factory.create() }

        facebookActivityLauncher = rememberLauncherForActivityResult(
            contract = loginManager.FacebookLoginActivityResultContract(callbackManager),
            onResult = {
                isLoading = false
            },
        )

        DisposableEffect(Unit) {
            loginManager.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onCancel() {
                        onFailureAuth(AuthMessage.Error.FACEBOOK_FAILED_TO_LOGIN)
                    }

                    override fun onError(error: FacebookException) {
                        onFailureAuth(AuthMessage.Error.FACEBOOK_FAILED_TO_LOGIN)
                    }

                    override fun onSuccess(result: LoginResult) {
                        scope.launch {
                            try {
                                val token = result.accessToken.token
                                FacebookAuthProvider.getCredential(token).also {
                                    Firebase.auth.signInWithCredential(it).await()
                                }
                                onSuccessAuth()
                            } catch (e: FirebaseAuthUserCollisionException) {
                                onFailureAuth(AuthMessage.Error.USER_ALREADY_EXISTS)
                            } catch (e: Exception) {
                                onFailureAuth(AuthMessage.Error.FACEBOOK_FAILED_TO_LOGIN)
                            }
                        }
                    }
                },
            )
            onDispose {
                loginManager.unregisterCallback(callbackManager)
            }
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
