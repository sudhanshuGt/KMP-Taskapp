package dev.sudhanshu.taskmanager


import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.getSystemService

actual class HapticFeedback(private val context: Context) {
    actual fun triggerHaptic() {
        val vibrator = context.getSystemService<Vibrator>()
        vibrator?.let {
            if (vibrator.hasVibrator()) {
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        }
    }
}
