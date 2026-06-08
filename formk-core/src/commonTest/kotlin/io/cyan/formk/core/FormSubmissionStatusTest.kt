package io.cyan.formk.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FormSubmissionStatusTest {

    @Test
    fun `test isInProgress property`() {
        assertTrue(FormSubmissionStatus.InProgress.isInProgress)
        
        assertFalse(FormSubmissionStatus.Initial.isInProgress)
        assertFalse(FormSubmissionStatus.Success.isInProgress)
        assertFalse(FormSubmissionStatus.Failure().isInProgress)
    }

    @Test
    fun `test Failure message is accessible`() {
        val message = "Network Error"
        val status = FormSubmissionStatus.Failure(message)

        assertEquals(message, status.message)
    }
}
