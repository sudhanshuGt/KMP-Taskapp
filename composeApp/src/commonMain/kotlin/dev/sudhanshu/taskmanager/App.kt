package dev.sudhanshu.taskmanager


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.sudhanshu.taskmanager.view.auth.AuthViewModel
import dev.sudhanshu.taskmanager.view.auth.SignInScreen
import dev.sudhanshu.taskmanager.view.auth.SignUpScreen
import dev.sudhanshu.taskmanager.view.start.Start
import dev.sudhanshu.taskmanager.view.navigation.graphs.RootNavGraph


import io.github.aakira.napier.Napier


import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import dev.sudhanshu.taskmanager.view.task.TaskViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url

@Composable
fun App(
    viewModel: AuthViewModel,
    biometricAuth: BiometricAuthenticator,
    taskViewModel: TaskViewModel,
    hapticFeedback: HapticFeedback
) {
    val state = viewModel.state.collectAsState()
    val navHost = rememberNavController()

    var isBiometricAuthenticated by remember { mutableStateOf(false) }
    var authenticationFailed by remember { mutableStateOf(false) }
    var isAuthenticating by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        if (state.value.isLoggedIn) {
            biometricAuth.authenticate(
                onSuccess = {
                    isBiometricAuthenticated = true
                    isAuthenticating = false
                },
                onFailure = {
                    authenticationFailed = true
                    isAuthenticating = false
                }
            )
        } else {
            isAuthenticating = false
        }
    }

    MaterialTheme {
        if (isAuthenticating) {
            AuthenticationLoadingScreen()
        } else if (authenticationFailed) {
            AuthenticationFailedScreen(onRetryClick = {
                isAuthenticating = true
                biometricAuth.authenticate(
                    onSuccess = {
                        isBiometricAuthenticated = true
                        isAuthenticating = false
                    },
                    onFailure = {
                        authenticationFailed = true
                        isAuthenticating = false
                    }
                )
            })
        } else if (isBiometricAuthenticated || !state.value.isLoggedIn) {
            NavHost(navHost, startDestination = "start") {
                composable("start") {
                    Start(onSignInClick = {
                        navHost.navigate("login")
                    }, onSignUpClick = {
                        navHost.navigate("signup")
                    })
                }

                composable("login") {
                    SignInScreen(viewModel, navHost)
                }

                composable("signup") {
                    SignUpScreen(viewModel, navHost)
                }

                composable("home") {
                    RootNavGraph(viewModel, navHost, taskViewModel, hapticFeedback)
                }
            }
        } else {
            AuthenticationFailedScreen(onRetryClick = {
                isAuthenticating = true
                biometricAuth.authenticate(
                    onSuccess = {
                        isBiometricAuthenticated = true
                        isAuthenticating = false
                    },
                    onFailure = {
                        authenticationFailed = true
                        isAuthenticating = false
                    }
                )
            })
        }
    }
}

@Composable
fun AuthenticationLoadingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Authenticating...")
    }
}

@Composable
fun AuthenticationFailedScreen(onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        KamelImage(
            resource = asyncPainterResource(data = Url("https://cdn-icons-png.flaticon.com/128/7266/7266696.png")),
            contentDescription = "Illustration",
            onLoading = { progress ->
                CircularProgressIndicator(
                    progress = { progress },
                )
            },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Authentication is required to use the app")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetryClick) {
            Text(text = "Authenticate Again")
        }
    }
}
