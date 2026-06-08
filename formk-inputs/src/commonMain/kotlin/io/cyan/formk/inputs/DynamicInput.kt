package io.cyan.formk.inputs

import io.cyan.formk.core.CachedFormkInput
import io.cyan.formk.core.FieldValidationConfig

enum class DynamicValidationError {
    Empty, TooShort, TooLong, PatternMismatch
}

class DynamicInput private constructor(
    value: String,
    isPure: Boolean,
    val config: FieldValidationConfig
) : CachedFormkInput<String, DynamicValidationError>(value, isPure) {

    constructor(config: FieldValidationConfig = FieldValidationConfig())
        : this("", isPure = true, config)

    private val compiledRegex: Regex? by lazy {
        config.pattern?.let {
            runCatching { Regex(it) }.getOrNull()
        }
    }

    fun dirty(newValue: String) = DynamicInput(newValue, isPure = false, config)
    fun markDirty() = DynamicInput(value, isPure = false, config)
    fun withConfig(newConfig: FieldValidationConfig) = DynamicInput("", isPure = true, newConfig)

    override fun validator(value: String): DynamicValidationError? {
        if (value.isBlank()) {
            return if (config.required) DynamicValidationError.Empty else null
        }
        config.minLength?.let {
            if (value.length < it) return DynamicValidationError.TooShort
        }
        config.maxLength?.let {
            if (value.length > it) return DynamicValidationError.TooLong
        }
        compiledRegex?.let {
            if (!it.matches(value)) return DynamicValidationError.PatternMismatch
        }
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (other !is DynamicInput) return false
        return value == other.value &&
               isPure == other.isPure &&
               config == other.config
    }

    override fun hashCode(): Int {
        var result = value.hashCode()
        result = 31 * result + isPure.hashCode()
        result = 31 * result + config.hashCode()
        return result
    }
}
