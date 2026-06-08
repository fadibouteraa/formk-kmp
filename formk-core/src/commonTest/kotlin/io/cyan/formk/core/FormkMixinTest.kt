package io.cyan.formk.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FormkMixinTest {

    // Simple mock input for testing the mixin
    class MockInput(value: String, isPure: Boolean = true, val shouldBeValid: Boolean = true) : FormkInput<String, String>(value, isPure) {
        override fun validator(value: String): String? = if (shouldBeValid) null else "Error"
    }

    class MockState(
        val input1: MockInput,
        val input2: MockInput
    ) : FormkMixin {
        override val inputs: List<FormkInput<*, *>> = listOf(input1, input2)
    }

    @Test
    fun `test mixin validity when all inputs are valid`() {
        val state = MockState(
            input1 = MockInput("A", shouldBeValid = true),
            input2 = MockInput("B", shouldBeValid = true)
        )

        assertTrue(state.isValid)
        assertFalse(state.isNotValid)
    }

    @Test
    fun `test mixin validity when one input is invalid`() {
        val state = MockState(
            input1 = MockInput("A", shouldBeValid = true),
            input2 = MockInput("B", shouldBeValid = false)
        )

        assertFalse(state.isValid)
        assertTrue(state.isNotValid)
    }

    @Test
    fun `test mixin purity`() {
        val pureState = MockState(
            input1 = MockInput("A", isPure = true),
            input2 = MockInput("B", isPure = true)
        )
        assertTrue(pureState.isPure)
        assertFalse(pureState.isDirty)

        val dirtyState = MockState(
            input1 = MockInput("A", isPure = true),
            input2 = MockInput("B", isPure = false) // One dirty input makes the form dirty
        )
        assertFalse(dirtyState.isPure)
        assertTrue(dirtyState.isDirty)
    }
}
