package io.cyan.formk.sample.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.cyan.formk.core.FormSubmissionStatus
import io.cyan.formk.sample.LoginViewModel

@Composable
fun LoginScreen(viewModel: LoginViewModel) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Formk Login Sample", style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.height(32.dp))

        // Email Input
        OutlinedTextField(
            value = state.email.value,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = { Text("Email") },
            isError = state.email.displayError != null,
            enabled = !state.status.isInProgress,
            modifier = Modifier.fillMaxWidth()
        )
        if (state.email.displayError != null) {
            Text(
                text = "Invalid email format or empty",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.align(Alignment.Start)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Password Input
        OutlinedTextField(
            value = state.password.value,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("Password") },
            isError = state.password.displayError != null,
            enabled = !state.status.isInProgress,
            modifier = Modifier.fillMaxWidth()
        )
        if (state.password.displayError != null) {
            Text(
                text = "Password is too short (min 6 chars)",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Submit Button
        Button(
            onClick = { viewModel.onSubmit() },
            enabled = state.isValid && !state.status.isInProgress,
            modifier = Modifier.fillMaxWidth().height(48.dp)
        ) {
            if (state.status.isInProgress) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.colors.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Login")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Global Status Display (Explicitly shown for the demo video)
        Text(
            text = "Status: ${state.status::class.simpleName}",
            color = when (state.status) {
                is FormSubmissionStatus.Success -> MaterialTheme.colors.primary
                is FormSubmissionStatus.Failure -> MaterialTheme.colors.error
                is FormSubmissionStatus.InProgress -> MaterialTheme.colors.secondary
                else -> MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
            },
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(top = 16.dp)
        )
        
        if (state.status is FormSubmissionStatus.Failure) {
            Text(
                text = "Message: ${(state.status as FormSubmissionStatus.Failure).message}",
                color = MaterialTheme.colors.error,
                style = MaterialTheme.typography.body2
            )
        }
    }
}
