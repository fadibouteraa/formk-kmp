# Formk Kotlin Multiplatform (KMP)

## 🌟 Introduction
**Formk** is a pure Kotlin, UI-agnostic library for standardizing form validation across your Kotlin Multiplatform (KMP) projects (Android, iOS via Compose Multiplatform, Desktop, Web). Strongly inspired by the famous `formz` package in the Flutter ecosystem.

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

### 2. `DynamicInput`
Built-in into `formk-core`, it provides dynamic, configuration-driven validation using `FieldValidationConfig`. 

```kotlin
val emailConfig = FieldValidationConfig(
    pattern = "^[A-Za-z0-9+_.-]+@(.+)$",
    required = true
)
val input = DynamicInput(emailConfig).dirty("test@example.com")
println(input.isValid) // true
```

### 3. `FormkMixin`
An interface applied to your screen's `UiState`. It automatically computes the global validity of the entire form by aggregating all inputs.

```kotlin
interface FormkMixin {
    val inputs: List<FormkInput<*, *>>
    val isValid: Boolean get() = inputs.all { it.isValid }
}
```

### 4. `FormSubmissionStatus`
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
