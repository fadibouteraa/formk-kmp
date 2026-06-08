package io.cyan.formk.core

/**
 * Predefined validation errors for [DynamicInput].
 */
enum class DynamicValidationError {
    Empty, TooShort, TooLong, PatternMismatch
}

/**
 * A highly reusable and dynamic implementation of [CachedFormkInput].
 * 
 * Validates the input string based on rules provided by a [FieldValidationConfig].
 *
 * @property config The validation rules to apply to this input.
 */
class DynamicInput private constructor(
    value: String,
    isPure: Boolean,
    val config: FieldValidationConfig
) : CachedFormkInput<String, DynamicValidationError>(value, isPure) {

    /**
     * Creates a pure [DynamicInput] with an empty string and the given configuration.
     * 
     * @param config The validation configuration. Defaults to an empty configuration.
     */
    constructor(config: FieldValidationConfig = FieldValidationConfig())
        : this("", isPure = true, config)

    private val compiledRegex: Regex? by lazy {
        config.pattern?.let {
            runCatching { Regex(it) }.getOrNull()
        }
    }

    /**
     * Returns a new dirty [DynamicInput] with the updated value.
     * 
     * @param newValue The new value provided by the user.
     */
    fun dirty(newValue: String) = DynamicInput(newValue, isPure = false, config)

    /**
     * Returns a new dirty [DynamicInput] keeping the current value.
     * Useful for triggering validation displays on untouched fields during form submission.
     */
    fun markDirty() = DynamicInput(value, isPure = false, config)

    /**
     * Returns a new pure [DynamicInput] initialized with the given [newConfig].
     */
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
