package io.cyan.formk.core

/**
 * A generic form input representation.
 * 
 * Encapsulates the value, its validation state, and whether the input has been touched by the user.
 *
 * @param T The type of the input value (e.g., String, Int).
 * @param E The type of the validation error (usually an Enum).
 * @property value The current value of the input.
 * @property isPure Indicates whether the input has been modified by the user. True by default.
 */
abstract class FormkInput<T, E>(
    val value: T,
    val isPure: Boolean = true
) {
    /**
     * Abstract function to define the validation logic.
     * 
     * @param value The value to validate.
     * @return An error of type [E] if validation fails, or null if the value is valid.
     */
    abstract fun validator(value: T): E?

    /** True if the input is valid (i.e., [validator] returns null). */
    open val isValid: Boolean get() = validator(value) == null

    /** True if the input is invalid. */
    val isNotValid: Boolean get() = !isValid

    /** The current validation error, or null if valid. */
    open val error: E? get() = validator(value)

    /** The error to display in the UI. Returns null if the input is still pure (untouched). */
    val displayError: E? get() = if (isPure) null else error

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as FormkInput<*, *>
        return other.value == value && other.isPure == isPure
    }

    override fun hashCode() = 31 * (value?.hashCode() ?: 0) + isPure.hashCode()
}

/**
 * A cached version of [FormkInput] that lazily computes and stores the validation error.
 * 
 * Useful for expensive validation operations (e.g., complex Regex matching) so that the
 * validation is not re-run every time [error] or [isValid] is accessed.
 */
abstract class CachedFormkInput<T, E>(
    value: T,
    isPure: Boolean = true
) : FormkInput<T, E>(value, isPure) {
    private val _cachedError: E? by lazy { validator(value) }
    override val error: E? get() = _cachedError
    override val isValid: Boolean get() = _cachedError == null
}
