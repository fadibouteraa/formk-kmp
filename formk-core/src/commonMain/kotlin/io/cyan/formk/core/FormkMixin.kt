package io.cyan.formk.core

/**
 * A mixin interface to be implemented by state classes that hold multiple [FormkInput]s.
 * 
 * Automatically computes the global validity and purity of the entire form based on its inputs.
 */
interface FormkMixin {
    /** The list of all form inputs to be validated. */
    val inputs: List<FormkInput<*, *>>

    /** True if all [inputs] are valid. */
    val isValid: Boolean get() = inputs.all { it.isValid }

    /** True if any of the [inputs] are invalid. */
    val isNotValid: Boolean get() = !isValid

    /** True if all [inputs] are pure (untouched). */
    val isPure: Boolean get() = inputs.all { it.isPure }

    /** True if at least one input has been modified (dirty). */
    val isDirty: Boolean get() = !isPure
}
