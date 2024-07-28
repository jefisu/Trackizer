package com.jefisu.home.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.jefisu.ui.components.Icon
import com.jefisu.ui.theme.AppTheme
import com.jefisu.ui.theme.Gray30
import com.jefisu.ui.theme.Gray70
import com.jefisu.ui.theme.Theme
import java.time.LocalDate

@Composable
internal fun DateIcon(date: LocalDate) {
    val day = date.dayOfMonth
    val month = date.month.name.take(3)
        .lowercase()
        .replaceFirstChar { it.uppercase() }

    Icon(
        containerColor = Gray70,
        content = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize(),
            ) {
                Text(
                    text = month,
                    style = Theme.typography.bodyMedium,
                    fontSize = 10.sp,
                    color = Gray30,
                )
                Text(
                    text = day.toString(),
                    style = Theme.typography.headline2,
                    color = Gray30,
                )
            }
        },
    )
}

@Preview
@Composable
private fun DateIconPreview() {
    AppTheme {
        DateIcon(LocalDate.of(2024, 7, 10))
    }
}
