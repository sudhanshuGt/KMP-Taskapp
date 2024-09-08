package dev.sudhanshu.taskmanager

expect class BiometricAuthenticator {
    fun authenticate(onSuccess: () -> Unit, onFailure: (String) -> Unit)
}