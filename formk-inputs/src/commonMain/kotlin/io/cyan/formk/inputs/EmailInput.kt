package io.cyan.formk.inputs

import io.cyan.formk.core.FormkInput

enum class EmailValidationError { InvalidFormat, Empty }

class EmailInput(
    value: String = "",
    isPure: Boolean = true
) : FormkInput<String, EmailValidationError>(value, isPure) {

    override fun validator(value: String): EmailValidationError? = when {
        value.isEmpty() -> EmailValidationError.Empty
        !Regex("^[A-Za-z0-9+_.-]+@(.+)$").matches(value) -> EmailValidationError.InvalidFormat
        else -> null
    }

    fun dirty(newValue: String) = EmailInput(newValue, false)
    fun markDirty() = EmailInput(value, false)
}
