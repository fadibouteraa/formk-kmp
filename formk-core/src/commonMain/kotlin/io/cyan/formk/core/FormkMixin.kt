package io.cyan.formk.core

interface FormkMixin {
    val inputs: List<FormkInput<*, *>>

    val isValid: Boolean get() = inputs.all { it.isValid }
    val isNotValid: Boolean get() = !isValid
    val isPure: Boolean get() = inputs.all { it.isPure }
    val isDirty: Boolean get() = !isPure
}
