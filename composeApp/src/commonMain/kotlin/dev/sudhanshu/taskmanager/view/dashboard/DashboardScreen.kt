package dev.sudhanshu.taskmanager.view.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.sudhanshu.taskmanager.view.task.TaskViewModel



import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.taskapp.database.Task


@Composable
fun DashboardScreen(taskViewModel: TaskViewModel) {
    LaunchedEffect(Unit) {
        taskViewModel.getTasksForCurrentDate()
        taskViewModel.getCompletedTasks()
        taskViewModel.getPendingTasks()
    }

    val todayTasks by taskViewModel.todayTask.collectAsState()
    val pendingTasks by taskViewModel.pendingTask.collectAsState()
    val completedTasks by taskViewModel.completedTask.collectAsState()

    Scaffold(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Only showing the section if there are tasks
            if (todayTasks.isNotEmpty()) {
                item {
                    TaskSection(
                        title = "Today's Tasks",
                        tasks = todayTasks
                    )
                }
            }
            if (pendingTasks.isNotEmpty()) {
                item {
                    TaskSection(
                        title = "Pending Tasks",
                        tasks = pendingTasks
                    )
                }
            }
            if (completedTasks.isNotEmpty()) {
                item {
                    TaskSection(
                        title = "Completed Tasks",
                        tasks = completedTasks
                    )
                }
            }
        }
    }
}

@Composable
fun TaskSection(title: String, tasks: List<Task>) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {

        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            items(tasks) { task ->
                TaskItem(task = task)
            }
        }
    }
}



@Composable
fun TaskItem(task: Task) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (task.status.toInt() == 0) Color(0xFFE3FCEF) else Color(0xFFFFEBEE)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = task.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            task.description?.let {
                Text(
                    text = it,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                 TaskStatusTag(status = task.status.toInt())

                Spacer(modifier = Modifier.weight(1f))

                task.dueDate?.let {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Due Date",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = task.dueDate,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun TaskStatusTag(status: Int) {
    val backgroundColor = if (status == 0) Color(0xFF4CAF50) else Color(0xFFF44336)
    val text = if (status == 0) "Completed" else "Pending"

    Box(
        modifier = Modifier
            .background(color = backgroundColor, shape = CircleShape)
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}



