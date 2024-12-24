package com.jefisu.user.presentation.editprofile

import android.net.Uri

sealed interface EditProfileAction {
    data class NameChanged(val name: String) : EditProfileAction
    data class PhotoPicked(val photoUri: Uri) : EditProfileAction
    data object SaveChanges : EditProfileAction
    data object NavigateBack : EditProfileAction
}