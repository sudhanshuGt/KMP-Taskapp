package dev.sudhanshu.taskmanager


// iosMain
import platform.LocalAuthentication.LAContext
import platform.LocalAuthentication.LAPolicy
import platform.Foundation.NSError
import platform.Foundation.NSLocalizedDescription
import kotlinx.cinterop.alloc
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.ptr

actual class BiometricAuthenticator {
    actual fun authenticate(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        val context = LAContext()
        val errorPtr = nativeHeap.alloc<NSError?>().ptr

        // Checking if Face ID/Touch ID is available
        if (context.canEvaluatePolicy(LAPolicy.LAPolicyDeviceOwnerAuthenticationWithBiometrics, errorPtr)) {
            context.evaluatePolicy(LAPolicy.LAPolicyDeviceOwnerAuthenticationWithBiometrics, "Authenticate using Face ID") { success, error ->
                if (success) {
                    onSuccess()
                } else {
                    onFailure(error?.localizedDescription ?: "Authentication failed")
                }
            }
        } else {
            val errorDescription = errorPtr.pointed?.localizedDescription ?: "Face ID/Touch ID not available"
            onFailure(errorDescription)
        }
    }
}

