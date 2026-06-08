package io.cyan.formk.sample

import io.cyan.formk.core.FormSubmissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onEmailChanged(newEmail: String) {
        _state.update { it.copy(email = it.email.dirty(newEmail)) }
    }

    fun onPasswordChanged(newPassword: String) {
        _state.update { it.copy(password = it.password.dirty(newPassword)) }
    }

    fun onSubmit() {
        _state.update {
            it.copy(
                email = it.email.markDirty(),
                password = it.password.markDirty()
            )
        }

        if (_state.value.isNotValid) return

        _state.update { it.copy(status = FormSubmissionStatus.InProgress) }
        
        // Simulating success
        _state.update { it.copy(status = FormSubmissionStatus.Success) }
    }
}
