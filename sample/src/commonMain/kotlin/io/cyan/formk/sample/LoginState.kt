package io.cyan.formk.sample

import io.cyan.formk.core.FormSubmissionStatus
import io.cyan.formk.core.FormkMixin
import io.cyan.formk.sample.inputs.EmailInput
import io.cyan.formk.sample.inputs.PasswordInput

data class LoginState(
    val email: EmailInput = EmailInput(),
    val password: PasswordInput = PasswordInput(),
    val status: FormSubmissionStatus = FormSubmissionStatus.Initial
) : FormkMixin {
    override val inputs = listOf(email, password)
}
