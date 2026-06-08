package io.cyan.formk.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DynamicInputTest {

    @Test
    fun `test valid input without config`() {
        val input = DynamicInput(FieldValidationConfig())
        val dirtyInput = input.dirty("some text")

        assertTrue(dirtyInput.isValid)
        assertFalse(dirtyInput.isNotValid)
        assertEquals(null, dirtyInput.error)
    }

    @Test
    fun `test empty input when required`() {
        val input = DynamicInput(FieldValidationConfig(required = true))
        val dirtyInput = input.dirty("")

        assertFalse(dirtyInput.isValid)
        assertEquals(DynamicValidationError.Empty, dirtyInput.error)
    }

    @Test
    fun `test minLength validation`() {
        val input = DynamicInput(FieldValidationConfig(minLength = 5))
        val dirtyInput = input.dirty("1234")

        assertFalse(dirtyInput.isValid)
        assertEquals(DynamicValidationError.TooShort, dirtyInput.error)

        val validDirtyInput = input.dirty("12345")
        assertTrue(validDirtyInput.isValid)
    }

    @Test
    fun `test maxLength validation`() {
        val input = DynamicInput(FieldValidationConfig(maxLength = 3))
        val dirtyInput = input.dirty("1234")

        assertFalse(dirtyInput.isValid)
        assertEquals(DynamicValidationError.TooLong, dirtyInput.error)
    }

    @Test
    fun `test pattern validation`() {
        // Only allow digits
        val input = DynamicInput(FieldValidationConfig(pattern = "^[0-9]+$"))
        
        val dirtyInputInvalid = input.dirty("123a")
        assertFalse(dirtyInputInvalid.isValid)
        assertEquals(DynamicValidationError.PatternMismatch, dirtyInputInvalid.error)

        val dirtyInputValid = input.dirty("1234")
        assertTrue(dirtyInputValid.isValid)
    }
}
