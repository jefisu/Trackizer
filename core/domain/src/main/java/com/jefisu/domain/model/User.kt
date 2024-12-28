package com.jefisu.domain.model

data class User(
    val id: String,
    val name: String?,
    val email: String,
    val pictureUrl: String?,
) {
    companion object {
        const val E_MAIL_MAX_LENGTH = 50
        const val NAME_MAX_LENGTH = 50
        const val PASSWORD_MAX_LENGTH = 24
    }
}