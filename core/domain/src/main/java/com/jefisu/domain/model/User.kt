package com.jefisu.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class User(val name: String, val email: String, val pictureUrl: String?)
