package io.cyan.formk.sample

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.cyan.formk.sample.ui.LoginScreen

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Formk Sample App"
    ) {
        val viewModel = LoginViewModel()
        LoginScreen(viewModel = viewModel)
    }
}
