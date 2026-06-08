package io.cyan.formk.core

import kotlinx.serialization.Serializable

@Serializable
data class FieldValidationConfig(
    val pattern: String? = null,
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val required: Boolean = true,
    val errorMessage: String? = null
)
