package com.jefisu.domain.validation

fun interface Validation<V, M> {
    fun validate(value: V): ValidationResult<M>
}
