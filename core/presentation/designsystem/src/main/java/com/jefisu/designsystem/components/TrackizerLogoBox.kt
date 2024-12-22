package com.jefisu.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.R
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.size
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.ui.screen.LocalScreenIsSmall

@Composable
fun TrackizerLogoBox(content: @Composable BoxScope.() -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(top = 70.dp.times(if (LocalScreenIsSmall.current) 0.8f else 1f))
                .align(Alignment.TopCenter),
        ) {
            Image(
                painter = painterResource(id = R.drawable.app_logo_no_background),
                contentDescription = null,
                modifier = Modifier.size(TrackizerTheme.size.appLogoSmall),
            )
            Spacer(modifier = Modifier.width(TrackizerTheme.spacing.extraSmall))
            Text(
                text = stringResource(id = R.string.app_name).uppercase(),
                style = TrackizerTheme.typography.headline5,
            )
        }
        content()
    }
}

@Preview
@Composable
private fun TrackizerLogoBoxPreview() {
    TrackizerTheme {
        TrackizerLogoBox {
        }
    }
}