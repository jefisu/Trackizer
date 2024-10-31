@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerOptionPicker
import com.jefisu.designsystem.components.TrackizerPickerDefaults
import com.jefisu.designsystem.components.TrackizerScaffold
import com.jefisu.designsystem.components.TrackizerSwitch
import com.jefisu.designsystem.components.TrackizerTopBar
import com.jefisu.designsystem.components.TrackizerTopBarDefaults
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.util.LocalAppConfig
import com.jefisu.domain.model.Settings
import com.jefisu.settings.R
import com.jefisu.settings.presentation.components.SettingOptionItem
import com.jefisu.settings.presentation.components.SettingOptions
import com.jefisu.settings.presentation.components.UserProfile
import com.jefisu.settings.presentation.util.SettingsConstants
import com.jefisu.ui.R as UiRes
import com.jefisu.ui.util.SampleData
import java.util.Locale

@Composable
internal fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    settings: Settings = LocalAppConfig.current.settings,
) {
    val navigationBarPadding = WindowInsets.safeDrawing.asPaddingValues().calculateBottomPadding()
    val context = LocalContext.current
    val userMapped = remember(state.user, settings) {
        val resource = context.getString(UiRes.string.user)
        state.user?.copy(
            name = "$resource ${state.user.id.filter { it.isDigit() }.take(8)}",
        )
    }

    TrackizerOptionPicker(
        title = stringResource(
            id = R.string.select_a,
            stringResource(R.string.currency),
        ),
        visible = state.isCurrencyPickerVisible,
        items = SettingsConstants.currencys,
        onDismiss = { onAction(SettingsAction.ToogleCurrencyPicker) },
        onSelectClick = { onAction(SettingsAction.CurrencyChanged(it.country)) },
        startIndex = SettingsConstants.currencys.indexOf(settings.currency),
    ) { currency ->
        TrackizerPickerDefaults.PickerItem(
            text = currency.displayNameWithSymbol(),
        )
    }

    TrackizerOptionPicker(
        title = stringResource(
            id = R.string.select_a,
            stringResource(R.string.language),
        ),
        visible = state.isLanguagePickerVisible,
        items = SettingsConstants.localesAvailable,
        onDismiss = { onAction(SettingsAction.ToogleLanguagePicker) },
        onSelectClick = { onAction(SettingsAction.LanguageChanged(it)) },
        startIndex = SettingsConstants.localesAvailable.indexOf(Locale.getDefault()),
    ) { locale ->
        TrackizerPickerDefaults.PickerItem(
            text = locale
                .displayLanguage
                .lowercase()
                .replaceFirstChar { it.titlecase() },
        )
    }

    userMapped?.let { user ->
        TrackizerScaffold(
            topBar = {
                TrackizerTopBar(
                    title = stringResource(R.string.screen_title),
                    navigationIcon = {
                        TrackizerTopBarDefaults.backNavigationIcon(
                            onClick = {
                                onAction(SettingsAction.NavigateBack)
                            },
                        )
                    },
                )
            },
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = TrackizerTheme.spacing.extraMedium)
                    .padding(
                        top = TrackizerTheme.spacing.large,
                        bottom = if (navigationBarPadding == 0.dp) {
                            TrackizerTheme.spacing.extraMedium
                        } else {
                            navigationBarPadding
                        },
                    )
                    .verticalScroll(rememberScrollState()),
            ) {
                UserProfile(
                    user = user,
                )
                Spacer(Modifier.height(TrackizerTheme.spacing.medium))
                TrackizerButton(
                    text = stringResource(R.string.edit_profile),
                    type = ButtonType.Secondary,
                    contentPadding = PaddingValues(
                        horizontal = TrackizerTheme.spacing.extraSmall,
                    ),
                    modifier = Modifier.height(36.dp),
                    onClick = {
                        onAction(SettingsAction.EditProfile)
                    },
                )
                Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
                SettingOptions(
                    title = stringResource(R.string.general),
                ) {
                    SettingOptionItem(
                        icon = R.drawable.ic_cloud_sync,
                        title = stringResource(R.string.cloud_sync),
                        leadingContent = {
                            TrackizerSwitch(
                                checked = settings.isCloudSyncEnabled,
                                onCheckedChange = { onAction(SettingsAction.ToggleCloudSync) },
                            )
                        },
                    )
                }
                Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
                SettingOptions(
                    title = stringResource(R.string.my_subscriptions),
                ) {
                    SettingOptionItem(
                        icon = R.drawable.ic_currency,
                        title = stringResource(R.string.default_currency),
                        settingSelected = settings.currency.displayCodeWithSymbol(),
                        onClick = {
                            onAction(SettingsAction.ToogleCurrencyPicker)
                        },
                    )
                }
                Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
                SettingOptions(
                    title = stringResource(R.string.appearance),
                ) {
                    SettingOptionItem(
                        icon = R.drawable.ic_language,
                        title = stringResource(R.string.language),
                        settingSelected = settings.run {
                            Locale.forLanguageTag(languageTag)
                                .displayLanguage
                                .lowercase()
                                .replaceFirstChar { it.titlecase() }
                        },
                        onClick = {
                            onAction(SettingsAction.ToogleLanguagePicker)
                        },
                    )
                }
                Spacer(Modifier.weight(1f))
                TrackizerButton(
                    text = stringResource(R.string.sign_out),
                    type = ButtonType.Secondary,
                    onClick = {
                        onAction(SettingsAction.SignOut)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    TrackizerTheme {
        SettingsScreen(
            state = SettingsState(
                user = SampleData.user,
            ),
            onAction = {},
        )
    }
}
