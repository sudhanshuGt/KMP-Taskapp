package dev.sudhanshu.taskmanager.view.auth


import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

import dev.sudhanshu.taskmanager.data_source.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AuthState())
    val state: StateFlow<AuthState> = _state


    val userName: Flow<String> = authRepository.getUserEmail()

    init {
        checkLoginState()
    }

    fun checkLoginState() {
        CoroutineScope(Dispatchers.Main).launch {
            val loggedIn = authRepository.isLoggedIn()
            _state.value = _state.value.copy(isLoggedIn = loggedIn)
        }
    }

    fun onEmailChanged(newEmail: String) {
        _state.value = _state.value.copy(email = newEmail)
    }

    fun onPasswordChanged(newPassword: String) {
        _state.value = _state.value.copy(password = newPassword)
    }

    fun onConfirmPasswordChanged(newConfirmPassword: String) {
        _state.value = _state.value.copy(confirmPassword = newConfirmPassword)
    }

    fun login() {
        CoroutineScope(Dispatchers.Main).launch {
            _state.value = _state.value.copy(isLoading = true)
            val success = authRepository.loginUser(_state.value.email, _state.value.password)
            if (success) {
                _state.value = _state.value.copy(isLoading = false)
                _state.value = _state.value.copy(success = true)
                _state.value = _state.value.copy(isLoggedIn = true, error = null)
            } else {
                _state.value = _state.value.copy(error = "Username or Password incorrect", isLoading = false)
            }
        }
    }

    fun register() {
        CoroutineScope(Dispatchers.Main).launch {
            _state.value = _state.value.copy(isLoading = true)
            val success = authRepository.registerUser(_state.value.email, _state.value.password)
            if (success) {
                _state.value = _state.value.copy(isLoading = false)
                _state.value = _state.value.copy(success = true)
                _state.value = _state.value.copy(isLoggedIn = true, error = null)
            } else {
                _state.value = _state.value.copy(error = "Something went wrong!", isLoading = false)
            }
        }
    }



    fun logout() {
        CoroutineScope(Dispatchers.Main).launch {
            authRepository.logout()
            _state.value = _state.value.copy(isLoggedIn = false)
        }
    }
}


