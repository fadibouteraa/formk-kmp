package io.cyan.formk.core

import kotlinx.serialization.Serializable

/**
 * Configuration data class used to define validation rules dynamically.
 * 
 * @property pattern An optional regular expression pattern the input must match.
 * @property minLength The minimum allowed length for the input string.
 * @property maxLength The maximum allowed length for the input string.
 * @property required If true, the input must not be blank. Default is true.
 * @property errorMessage An optional custom error message.
 */
@Serializable
data class FieldValidationConfig(
    val pattern: String? = null,
    val minLength: Int? = null,
    val maxLength: Int? = null,
    val required: Boolean = true,
    val errorMessage: String? = null
)
