package io.cyan.formk.core

abstract class FormkInput<T, E>(
    val value: T,
    val isPure: Boolean = true
) {
    abstract fun validator(value: T): E?

    open val isValid: Boolean get() = validator(value) == null
    val isNotValid: Boolean get() = !isValid
    open val error: E? get() = validator(value)

    val displayError: E? get() = if (isPure) null else error

    override fun equals(other: Any?): Boolean {
        if (other?.javaClass != javaClass) return false
        other as FormkInput<*, *>
        return other.value == value && other.isPure == isPure
    }

    override fun hashCode() = 31 * (value?.hashCode() ?: 0) + isPure.hashCode()
}

abstract class CachedFormkInput<T, E>(
    value: T,
    isPure: Boolean = true
) : FormkInput<T, E>(value, isPure) {
    private val _cachedError: E? by lazy { validator(value) }
    override val error: E? get() = _cachedError
    override val isValid: Boolean get() = _cachedError == null
}
