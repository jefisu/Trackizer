package com.jefisu.domain.validation

data class ValidationResult<T>(val successfully: Boolean, val error: T? = null)