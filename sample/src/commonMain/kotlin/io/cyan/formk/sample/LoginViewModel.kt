package io.cyan.formk.sample

import io.cyan.formk.core.FormSubmissionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state

    fun onEmailChanged(newEmail: String) {
        _state.update { it.copy(email = it.email.dirty(newEmail)) }
    }

    fun onPasswordChanged(newPassword: String) {
        _state.update { it.copy(password = it.password.dirty(newPassword)) }
    }

    fun onSubmit() {
        // 1. Mark fields as dirty to trigger visual errors on untouched fields
        _state.update {
            it.copy(
                email = it.email.markDirty(),
                password = it.password.markDirty()
            )
        }

        // 2. Block submission if invalid
        if (_state.value.isNotValid) return

        // 3. Process Network Request
        _state.update { it.copy(status = FormSubmissionStatus.InProgress) }
        
        scope.launch {
            // Simulate API call for 2 seconds
            delay(2000)
            
            // Simulating success
            _state.update { it.copy(status = FormSubmissionStatus.Success) }
        }
    }
}
