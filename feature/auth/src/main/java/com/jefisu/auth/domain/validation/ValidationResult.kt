package com.jefisu.auth.domain.validation

data class ValidationResult<T>(
    val successfully: Boolean,
    val error: T? = null
)
