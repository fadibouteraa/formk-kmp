package io.cyan.formk.core

/**
 * Represents the lifecycle status of a form submission.
 * 
 * Useful for handling UI states such as displaying loaders or error messages.
 */
sealed class FormSubmissionStatus {
    /** The form has not been submitted yet. */
    data object Initial : FormSubmissionStatus()

    /** The form submission is currently in progress (e.g., network request pending). */
    data object InProgress : FormSubmissionStatus()

    /** The form submission completed successfully. */
    data object Success : FormSubmissionStatus()

    /**
     * The form submission failed.
     * 
     * @property message An optional error message detailing the failure.
     */
    data class Failure(val message: String? = null) : FormSubmissionStatus()

    /** Convenience property to check if the status is [InProgress]. */
    val isInProgress: Boolean get() = this is InProgress
}
