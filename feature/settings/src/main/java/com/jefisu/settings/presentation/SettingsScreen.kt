@file:OptIn(ExperimentalMaterial3Api::class)

package com.jefisu.settings.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.composables.core.SheetDetent
import com.composables.core.rememberModalBottomSheetState
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
import com.jefisu.settings.R
import com.jefisu.settings.presentation.components.SettingOptionItem
import com.jefisu.settings.presentation.components.SettingOptions
import com.jefisu.settings.presentation.components.UserProfile
import com.jefisu.settings.presentation.util.SettingsConstants
import com.jefisu.ui.screen.LocalScreenIsSmall
import com.jefisu.ui.util.SampleData
import java.util.Locale
import com.jefisu.ui.R as UiRes

@Composable
internal fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
) {
    val context = LocalContext.current
    val settings = state.settings
    val userMapped = remember(state.user, settings) {
        val resource = context.getString(UiRes.string.user)
        state.user?.let {
            if (it.name.isBlank()) {
                return@let it.copy(
                    name = "$resource ${it.id.filter { it.isDigit() }.take(8)}",
                )
            }
            it
        }
    }

    val currentySheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    TrackizerOptionPicker(
        sheetState = currentySheetState,
        title = stringResource(
            id = R.string.select_a,
            stringResource(R.string.currency),
        ),
        items = SettingsConstants.currencys,
        onDismiss = {},
        onSelectClick = { onAction(SettingsAction.CurrencyChanged(it)) },
        startIndex = SettingsConstants.currencys.indexOfFirst { it.symbol == settings.currency.symbol },
    ) { currency ->
        TrackizerPickerDefaults.PickerItem(
            text = currency.displayNameWithSymbol(),
        )
    }

    val selectLanguageSheetState = rememberModalBottomSheetState(initialDetent = SheetDetent.Hidden)
    TrackizerOptionPicker(
        sheetState = selectLanguageSheetState,
        title = stringResource(
            id = R.string.select_a,
            stringResource(R.string.language),
        ),
        items = SettingsConstants.localesAvailable,
        onDismiss = { },
        onSelectClick = { onAction(SettingsAction.LanguageChanged(it)) },
        startIndex = SettingsConstants.localesAvailable.indexOfFirst { it.language == settings.languageTag },
    ) { locale ->
        TrackizerPickerDefaults.PickerItem(
            text = locale
                .displayLanguage
                .lowercase()
                .replaceFirstChar { it.titlecase() },
        )
    }

    @Composable
    fun Space(defaultSpace: Dp, smallerSpace: Dp) {
        Spacer(
            modifier = Modifier.height(
                if (LocalScreenIsSmall.current) smallerSpace
                else defaultSpace,
            ),
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
                    .verticalScroll(rememberScrollState()),
            ) {
                UserProfile(
                    user = user,
                    modifier = Modifier
                        .padding(
                            top = if (LocalScreenIsSmall.current) {
                                0.dp
                            } else TrackizerTheme.spacing.large,
                        ),
                )
                Space(
                    defaultSpace = TrackizerTheme.spacing.medium,
                    smallerSpace = TrackizerTheme.spacing.small,
                )
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
                Space(
                    defaultSpace = TrackizerTheme.spacing.extraMedium,
                    smallerSpace = TrackizerTheme.spacing.medium,
                )
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
                Space(
                    defaultSpace = TrackizerTheme.spacing.extraMedium,
                    smallerSpace = TrackizerTheme.spacing.medium,
                )
                SettingOptions(
                    title = stringResource(R.string.my_subscriptions),
                ) {
                    SettingOptionItem(
                        icon = R.drawable.ic_currency,
                        title = stringResource(R.string.default_currency),
                        settingSelected = settings.currency.displayCodeWithSymbol(),
                        onClick = {
                            currentySheetState.currentDetent = SheetDetent.FullyExpanded
                        },
                    )
                }
                Space(
                    defaultSpace = TrackizerTheme.spacing.extraMedium,
                    smallerSpace = TrackizerTheme.spacing.medium,
                )
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
                            selectLanguageSheetState.currentDetent = SheetDetent.FullyExpanded
                        },
                    )
                }
                Spacer(
                    Modifier
                        .weight(1f)
                        .heightIn(min = TrackizerTheme.spacing.medium),
                )
                TrackizerButton(
                    text = stringResource(R.string.sign_out),
                    type = ButtonType.Secondary,
                    onClick = {
                        onAction(SettingsAction.SignOut)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = TrackizerTheme.spacing.extraMedium),
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
