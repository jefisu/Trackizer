package com.jefisu.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jefisu.ui.R
import com.jefisu.ui.theme.Theme

@Composable
fun StandardScreenLogo(content: @Composable BoxScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
                Modifier
                    .padding(top = 70.dp)
                    .align(Alignment.TopCenter),
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo_no_background),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(Theme.spacing.extraSmall))
            Text(
                text = stringResource(id = R.string.app_name).uppercase(),
                style = Theme.typography.headline5,
            )
        }
        content()
    }
}
