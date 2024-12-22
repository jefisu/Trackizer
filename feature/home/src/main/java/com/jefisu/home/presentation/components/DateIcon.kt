package com.jefisu.home.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.TrackizerIcon
import com.jefisu.designsystem.typography
import java.time.LocalDate

@Composable
internal fun DateIcon(date: LocalDate) {
    val day = date.dayOfMonth
    val month = date.month.name.take(3)
        .lowercase()
        .replaceFirstChar { it.uppercase() }

    TrackizerIcon {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = month,
                style = TrackizerTheme.typography.bodyMedium,
                fontSize = 10.sp,
            )
            Text(
                text = day.toString(),
                style = TrackizerTheme.typography.headline2,
            )
        }
    }
}

@Preview
@Composable
private fun DateIconPreview() {
    TrackizerTheme {
        DateIcon(LocalDate.of(2024, 7, 10))
    }
}