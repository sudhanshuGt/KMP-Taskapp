import com.android.build.gradle.internal.ide.kmp.KotlinAndroidSourceSetMarker.Companion.android
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform") version "2.0.0"
    kotlin("plugin.compose") version "2.0.0" // Explicitly apply Compose plugin
    id("com.android.application")
    id("org.jetbrains.compose")
    id("com.squareup.sqldelight") version "1.5.5" // Ensure correct version
}

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "ComposeApp"
                isStatic = true
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(libs.androidx.material)
                implementation(compose.material3)
                implementation(compose.ui)
                implementation("io.ktor:ktor-client-core:2.3.4")
                implementation("androidx.legacy:legacy-support-core-utils:1.0.0")
                implementation(compose.components.resources)
                implementation(libs.sqldelight.runtime)
                implementation(libs.coroutines.extensions)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kamel.image)
                implementation(libs.navigation.compose)
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")

                //moko
                implementation(libs.moko.mvvm.core)
                implementation(libs.moko.mvvm.compose)

                // koin
                implementation(libs.koin.core)
                implementation(libs.koin.compose)

                // bio metric
                implementation("androidx.biometric:biometric:1.2.0-alpha05")

                // logger
                implementation("io.github.aakira:napier:2.7.1")

                // calender
                implementation("network.chaintech:kmp-date-time-picker:1.0.2")

                implementation(libs.androidx.core.i18n)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose.v170)
                implementation(compose.preview)
                implementation(libs.android.driver)
                implementation(libs.androidx.lifecycle.runtime.compose.v284)
                implementation(libs.ktor.client.android)
                implementation(libs.navigation.compose.v260)
                implementation(libs.androidx.core.ktx)

                // koin android support
                implementation(libs.koin.android)
                implementation(libs.androidx.core.i18n)


            }
        }

        val iosMain by getting {
            dependencies {
                implementation(libs.native.driver)

            }
        }
    }

    targets.all {
        compilations.all {
            kotlinOptions {
                freeCompilerArgs += "-Xexpect-actual-classes"
            }
        }
    }

    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

android {
    namespace = "dev.sudhanshu.taskmanager"
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "dev.sudhanshu.taskmanager"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    dependencies {
        debugImplementation(compose.uiTooling)
    }
}





sqldelight {
    database("AppDatabase") {
        packageName = "dev.sudhanshu.taskmanager.database"
        sourceFolders = listOf("sqldelight")
    }
}
