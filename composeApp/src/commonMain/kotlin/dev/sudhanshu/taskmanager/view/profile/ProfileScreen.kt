package dev.sudhanshu.taskmanager.view.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.sudhanshu.taskmanager.view.auth.AuthViewModel


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import dev.sudhanshu.taskmanager.view.task.TaskViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    rootNavController: NavHostController,
    taskViewModel: TaskViewModel
) {
    val userName by viewModel.userName.collectAsState(initial = "Loading...")

    // Collect tasks from TaskViewModel
    val totalTasks by taskViewModel.tasks.collectAsState()
    val completedTasks = totalTasks.count { it.status.toInt() == 0 }
    val pendingTasks = totalTasks.count { it.status.toInt() == 1 }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Profile Image
            KamelImage(
                resource = asyncPainterResource(data = Url("https://cdn-icons-png.flaticon.com/128/3177/3177440.png")),
                contentDescription = "Profile Image",
                onLoading = { progress ->
                    CircularProgressIndicator(progress = { progress })
                },
                modifier = Modifier
                    .height(150.dp)
                    .width(150.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary),
                contentScale = ContentScale.Crop
            )

            // Spacer between the image and the username
            Spacer(modifier = Modifier.height(16.dp))

            // Username
            Text(
                text = userName.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Task Statistics Row (Total, Completed, Pending)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TaskStatItem(statName = "Total Tasks", statValue = totalTasks.size)
                TaskStatItem(statName = "Completed", statValue = completedTasks)
                TaskStatItem(statName = "Pending", statValue = pendingTasks)
            }

            // Spacer to push the button to the bottom
            Spacer(modifier = Modifier.weight(1f))

            // Logout Button
            Button(onClick = {
                viewModel.logout()
                rootNavController.navigate("start") {
                    popUpTo("start") {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }) {
                if (viewModel.state.value.isLoggedIn) {
                    Text(text = "Logout")
                }
            }
        }
    }
}

@Composable
fun TaskStatItem(statName: String, statValue: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = statValue.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = statName,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )
    }
}

