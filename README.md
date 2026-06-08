# Formk Kotlin Multiplatform (KMP)

[![Maven Central](https://img.shields.io/maven-central/v/io.github.fadibouteraa/formk-core.svg?label=Maven%20Central)](https://central.sonatype.com/artifact/io.github.fadibouteraa/formk-core)
[![Kotlin](https://img.shields.io/badge/kotlin-multiplatform-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

## 🌟 Introduction
**Formk** is a pure Kotlin, UI-agnostic library for standardizing form validation across your Kotlin Multiplatform (KMP) projects (Android, iOS via Compose Multiplatform, Desktop, Web). Strongly inspired by the famous `formz` package in the Flutter ecosystem.

### Demo
<video src="demo/formk-sample.mp4" controls="controls" style="max-width: 100%;"></video>

---

## 📥 Installation

Available on **Maven Central**. Add the dependency to your Kotlin Multiplatform `build.gradle.kts`:

```kotlin
sourceSets {
    commonMain.dependencies {
        implementation("io.github.fadibouteraa:formk-core:1.0.0")
    }
}
```

---

## 🛠️ Design Philosophy

The Kotlin ecosystem often sees developers mixing UI logic (Jetpack Compose states) with business validation rules, leading to spaghetti code that is hard to test and impossible to share across platforms.

**The Formk Approach:**
1. **100% Pure Kotlin**: Does not depend on `android.content.Context` or `androidx.compose`. Inherently ready for any KMP target.
2. **Immutable States**: Unidirectional Data Flow (UDF) is strictly respected. Every time an input changes, a new immutable instance is created.
3. **Clean Architecture Ready**: Validation rules (Regex, length, required) live in the Domain/Presentation logic layers, while UI representation stays completely separated.

---

## 📦 Core Components

### 1. `FormkInput<T, E>`
The foundational class. It encapsulates the field's `value`, whether it has been touched (`isPure`), and exposes its `error`.

```kotlin
abstract class FormkInput<T, E>(
    val value: T,
    val isPure: Boolean = true
) {
    abstract fun validator(value: T): E?

    open val isValid: Boolean get() = validator(value) == null
    val isNotValid: Boolean get() = !isValid
    open val error: E? get() = validator(value)

    val displayError: E? get() = if (isPure) null else error
}
```

### 2. Custom Inputs
While you can use `DynamicInput`, you can also create highly specialized inputs for your domain by extending `FormkInput`. Here is an example of a custom `PasswordInput`:

```kotlin
enum class PasswordValidationError { Empty, TooShort }

class PasswordInput private constructor(
    value: String,
    isPure: Boolean
) : FormkInput<String, PasswordValidationError>(value, isPure) {

    constructor() : this("", isPure = true)

    fun dirty(newValue: String) = PasswordInput(newValue, isPure = false)
    fun markDirty() = PasswordInput(value, isPure = false)

    override fun validator(value: String): PasswordValidationError? = when {
        value.isBlank() -> PasswordValidationError.Empty
        value.length < 6 -> PasswordValidationError.TooShort
        else -> null
    }
}
```

### 3. `DynamicInput`
Built-in into `formk-core`, it provides dynamic, configuration-driven validation using `FieldValidationConfig`. 

```kotlin
val emailConfig = FieldValidationConfig(
    pattern = "^[A-Za-z0-9+_.-]+@(.+)$",
    required = true
)
val input = DynamicInput(emailConfig).dirty("test@example.com")
println(input.isValid) // true
```

### 4. `FormkMixin`
An interface applied to your screen's `UiState`. It automatically computes the global validity of the entire form by aggregating all inputs.

```kotlin
interface FormkMixin {
    val inputs: List<FormkInput<*, *>>
    val isValid: Boolean get() = inputs.all { it.isValid }
}
```

### 5. `FormSubmissionStatus`
A sealed class representing the lifecycle of the form submission, essential for displaying loaders or global error messages (`Initial`, `InProgress`, `Success`, `Failure`).

---

## 🚀 Usage Example

### Step 1: Declare the UiState
Use the provided `DynamicInput` or create your own custom inputs extending `FormkInput`.

```kotlin
data class LoginState(
    val email: DynamicInput = DynamicInput(FieldValidationConfig(required = true)),
    val password: DynamicInput = DynamicInput(FieldValidationConfig(minLength = 6)),
    val status: FormSubmissionStatus = FormSubmissionStatus.Initial
) : FormkMixin {
    override val inputs = listOf(email, password)
}
```

### Step 2: Handle Actions in ViewModel
Handle user input and state mutations easily.

```kotlin
class LoginViewModel {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onEmailChanged(newEmail: String) {
        _state.update { it.copy(email = it.email.dirty(newEmail)) }
    }

    fun onSubmit() {
        // Mark all fields as dirty to trigger UI errors on untouched fields
        _state.update { 
            it.copy(
                email = it.email.markDirty(),
                password = it.password.markDirty()
            )
        }

        // Block submission if any field is invalid
        if (_state.value.isNotValid) return

        // Process submission
        _state.update { it.copy(status = FormSubmissionStatus.InProgress) }
        // ... API Call ...
    }
}
```

### Step 3: UI Integration (Jetpack Compose)
Perfect synchronization without messy `if/else` checks.

```kotlin
TextField(
    value = state.email.value,
    isError = state.email.displayError != null, // Only shows error if field is dirty (touched)
    enabled = !state.status.isInProgress,
    onValueChange = { viewModel.onEmailChanged(it) }
)

Button(
    enabled = state.isValid && !state.status.isInProgress,
    onClick = { viewModel.onSubmit() }
) {
    if (state.status.isInProgress) CircularProgressIndicator()
    else Text("Login")
}
```
