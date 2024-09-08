package dev.sudhanshu.taskmanager

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform