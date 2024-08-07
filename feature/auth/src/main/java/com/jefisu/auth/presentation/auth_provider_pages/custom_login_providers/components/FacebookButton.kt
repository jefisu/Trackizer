package com.jefisu.auth.presentation.auth_provider_pages.custom_login_providers.components

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
import com.facebook.login.LoginManager.FacebookLoginActivityResultContract
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.jefisu.auth.R
import com.jefisu.auth.data.AuthMessage
import com.jefisu.ui.components.Button
import com.jefisu.ui.components.ButtonProperties
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.theme.facebookColor
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

typealias FacebookActivityLauncher = ManagedActivityResultLauncher<Collection<String>, CallbackManager.ActivityResultParameters>

object FacebookAuthUi {
    val callbackManager by lazy { CallbackManager.Factory.create() }
    val loginManager by lazy { LoginManager.getInstance() }

    fun loginActivityContract(): FacebookLoginActivityResultContract =
        loginManager.createLogInActivityResultContract(callbackManager, null)
}

@Composable
fun FacebookButton(
    onSuccessfulLogin: () -> Unit,
    onFailureLogin: (AuthMessage.Error) -> Unit,
    modifier: Modifier = Modifier,
) {
    val view = LocalView.current
    val scope = rememberCoroutineScope()
    var isLoading by rememberSaveable { mutableStateOf(false) }
    val contentColor = Color.White
    var facebookActivityLauncher: FacebookActivityLauncher? = null

    val isDisabledPreview: Boolean = !view.isInEditMode
    if (isDisabledPreview) {
        facebookActivityLauncher = rememberLauncherForActivityResult(
            contract = FacebookAuthUi.loginActivityContract(),
            onResult = {},
        )
        DisposableEffect(Unit) {
            val loginManager = FacebookAuthUi.loginManager
            val callbackManager = FacebookAuthUi.callbackManager

            loginManager.registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onCancel() {
                        onFailureLogin(AuthMessage.Error.FACEBOOK_FAILED_TO_LOGIN)
                    }

                    override fun onError(error: FacebookException) {
                        onFailureLogin(AuthMessage.Error.FACEBOOK_FAILED_TO_LOGIN)
                    }

                    override fun onSuccess(result: LoginResult) {
                        scope.launch {
                            isLoading = true
                            try {
                                val token = result.accessToken.token
                                FacebookAuthProvider.getCredential(token).also {
                                    Firebase.auth.signInWithCredential(it).await()
                                }

                                onSuccessfulLogin()
                            } catch (e: FirebaseAuthUserCollisionException) {
                                onFailureLogin(AuthMessage.Error.USER_ALREADY_EXISTS)
                            } catch (e: Exception) {
                                onFailureLogin(AuthMessage.Error.FACEBOOK_FAILED_TO_LOGIN)
                            } finally {
                                isLoading = false
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

    Button(
        modifier = modifier,
        text = stringResource(id = R.string.sign_up_with, "Facebook"),
        properties = ButtonProperties(
            leadingIconRes = R.drawable.ic_facebook,
            type = ButtonType.DynamicColor(
                containerColor = facebookColor,
                contentColor = contentColor,
            ),
            isLoading = isLoading,
        ),
        onClick = {
            facebookActivityLauncher?.launch(listOf("email", "public_profile"))
        },
    )
}
