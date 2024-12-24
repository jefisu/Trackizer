package com.jefisu.user.presentation.editprofile

import com.jefisu.domain.model.User

data class EditProfileState(
    val user: User? = null,
    val isUpdating: Boolean = false,
)