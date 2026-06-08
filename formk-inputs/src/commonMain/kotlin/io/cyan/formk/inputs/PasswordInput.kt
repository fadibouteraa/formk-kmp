package io.cyan.formk.inputs

import io.cyan.formk.core.FormkInput

enum class PasswordValidationError { Empty, TooShort }

class PasswordInput(
    value: String = "",
    isPure: Boolean = true
) : FormkInput<String, PasswordValidationError>(value, isPure) {

    override fun validator(value: String): PasswordValidationError? = when {
        value.isEmpty() -> PasswordValidationError.Empty
        value.length < 6 -> PasswordValidationError.TooShort
        else -> null
    }

    fun dirty(newValue: String) = PasswordInput(newValue, false)
    fun markDirty() = PasswordInput(value, false)
}
