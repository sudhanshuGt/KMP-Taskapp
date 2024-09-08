package dev.sudhanshu.taskmanager

import platform.UIKit.UIImpactFeedbackGenerator
import platform.UIKit.UIImpactFeedbackStyleMedium

actual class HapticFeedback {
    actual fun triggerHaptic() {
        val feedbackGenerator = UIImpactFeedbackGenerator(UIImpactFeedbackStyleMedium)
        feedbackGenerator.prepare()
        feedbackGenerator.impactOccurred()
    }
}
