@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package com.jefisu.user.presentation.editprofile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.jefisu.designsystem.Gray30
import com.jefisu.designsystem.TrackizerTheme
import com.jefisu.designsystem.components.ButtonType
import com.jefisu.designsystem.components.TrackizerButton
import com.jefisu.designsystem.components.TrackizerScaffold
import com.jefisu.designsystem.components.TrackizerTopBar
import com.jefisu.designsystem.components.TrackizerTopBarDefaults
import com.jefisu.designsystem.spacing
import com.jefisu.designsystem.typography
import com.jefisu.ui.R as UiRes
import com.jefisu.ui.util.SampleData
import com.jefisu.user.R
import com.jefisu.user.presentation.editprofile.components.EditableProfileField
import com.jefisu.user.presentation.editprofile.components.ProfilePhoto

@Composable
internal fun EditProfileScreen(
    state: EditProfileState,
    onAction: (EditProfileAction) -> Unit,
) {
    val horizontalPadding = PaddingValues(horizontal = TrackizerTheme.spacing.extraMedium)

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
    ) { contentUri ->
        contentUri?.let {
            onAction(EditProfileAction.PhotoPicked(contentUri))
        }
    }

    val blockInteraction = @Composable {
        if (state.isUpdating) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {}
                    .zIndex(1f),
            )
        }
    }

    state.user?.let { user ->
        blockInteraction()
        TrackizerScaffold(
            topBar = {
                TrackizerTopBar(
                    title = stringResource(R.string.edit_profile),
                    navigationIcon = {
                        TrackizerTopBarDefaults.backNavigationIcon(
                            onClick = { onAction(EditProfileAction.NavigateBack) },
                        )
                    },
                )
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .imePadding(),
            ) {
                ProfilePhoto(
                    photo = user.pictureUrl,
                    onClick = {
                        photoPickerLauncher.launch(
                            PickVisualMediaRequest(
                                mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly,
                            ),
                        )
                    },
                    modifier = Modifier
                        .padding(top = TrackizerTheme.spacing.small)
                        .align(Alignment.CenterHorizontally),
                )
                Spacer(Modifier.height(TrackizerTheme.spacing.extraMedium))
                Text(
                    text = stringResource(R.string.about_you),
                    style = TrackizerTheme.typography.headline3,
                    color = Gray30,
                    modifier = Modifier.padding(horizontal = TrackizerTheme.spacing.extraMedium),
                )
                Spacer(Modifier.height(TrackizerTheme.spacing.small))
                EditableProfileField(
                    title = stringResource(UiRes.string.name),
                    textFieldValue = TextFieldValue(
                        text = user.name.orEmpty(),
                        selection = TextRange(user.name.orEmpty().length),
                    ),
                    onTextChange = { onAction(EditProfileAction.NameChanged(it.text)) },
                    contentPadding = horizontalPadding,
                )
                Spacer(Modifier.weight(1f))
                TrackizerButton(
                    text = stringResource(UiRes.string.save_changes),
                    type = ButtonType.Primary,
                    isLoading = state.isUpdating,
                    onClick = { onAction(EditProfileAction.SaveChanges) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontalPadding)
                        .padding(bottom = TrackizerTheme.spacing.extraMedium),
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditProfileScreenPreview() {
    TrackizerTheme {
        EditProfileScreen(
            state = EditProfileState(
                user = SampleData.user,
            ),
            onAction = { },
        )
    }
}