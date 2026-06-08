package io.cyan.formk.core

sealed class FormSubmissionStatus {
    data object Initial : FormSubmissionStatus()
    data object InProgress : FormSubmissionStatus()
    data object Success : FormSubmissionStatus()
    data class Failure(val message: String? = null) : FormSubmissionStatus()

    val isInProgress: Boolean get() = this is InProgress
}
