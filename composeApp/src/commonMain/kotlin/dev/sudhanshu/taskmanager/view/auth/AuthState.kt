package dev.sudhanshu.taskmanager.view.auth


data class AuthState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null
)

