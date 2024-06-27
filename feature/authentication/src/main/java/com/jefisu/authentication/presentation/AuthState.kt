package com.jefisu.authentication.presentation

import com.jefisu.authentication.presentation.components.DifficultPassword


data class AuthState(
    val registerEmail: String = "",
    val registerPassword: String = "",
    val loginEmail: String = "",
    val loginPassword: String = "",
    val difficultPassword: DifficultPassword? = null,
    val rememberMeCredentials: Boolean = false,
    val hasUserLogged: Boolean = false
)