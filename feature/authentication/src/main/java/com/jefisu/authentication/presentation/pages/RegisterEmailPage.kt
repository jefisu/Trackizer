package com.jefisu.authentication.presentation.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jefisu.authentication.R
import com.jefisu.authentication.presentation.AuthAction
import com.jefisu.authentication.presentation.AuthState
import com.jefisu.authentication.presentation.components.MeasureDificultyPassword
import com.jefisu.ui.components.ButtonType
import com.jefisu.ui.components.DynamicButton
import com.jefisu.ui.components.StandardTextField
import com.jefisu.ui.theme.Gray50
import com.jefisu.ui.theme.Theme

@Composable
fun RegisterEmailPage(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    onNavigateToLoginPage: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StandardTextField(
            text = state.registerEmail,
            onTextChange = { onAction(AuthAction.RegisterEmailChanged(it)) },
            fieldName = stringResource(R.string.e_mail_address),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Theme.spacing.medium))
        StandardTextField(
            text = state.registerPassword,
            onTextChange = { onAction(AuthAction.RegisterPasswordChanged(it)) },
            fieldName = stringResource(R.string.password),
            isPasswordField = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(Theme.spacing.extraMedium))
        MeasureDificultyPassword(
            difficultPassword = null
        )
        Spacer(modifier = Modifier.height(Theme.spacing.medium))
        Text(
            text = stringResource(R.string.use_8_or_more_characters),
            style = Theme.typography.bodySmall,
            color = Gray50,
            textAlign = TextAlign.Justify
        )
        Spacer(modifier = Modifier.height(40.dp))
        DynamicButton(
            text = stringResource(R.string.get_started_it_s_free),
            buttonType = ButtonType.Primary,
            onClick = {
                onAction(AuthAction.SubmitRegister)
            }
        )
        Spacer(modifier = Modifier.height(130.dp))
        Text(
            text = stringResource(R.string.do_you_have_already_an_account),
            style = Theme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(Theme.spacing.extraMedium))
        DynamicButton(
            text = stringResource(R.string.sign_in),
            buttonType = ButtonType.Secondary,
            onClick = onNavigateToLoginPage
        )
    }
}