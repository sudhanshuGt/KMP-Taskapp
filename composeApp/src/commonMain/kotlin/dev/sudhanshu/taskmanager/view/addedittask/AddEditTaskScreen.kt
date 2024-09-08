package dev.sudhanshu.taskmanager.view.addedittask



import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch
import androidx.compose.ui.text.font.FontWeight
import network.chaintech.ui.datepicker.WheelDatePickerView
import network.chaintech.utils.DateTimePickerView
import network.chaintech.utils.WheelPickerDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.TextStyle
import dev.sudhanshu.taskmanager.HapticFeedback
import dev.sudhanshu.taskmanager.view.task.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    viewModel: TaskViewModel,
    navController: NavHostController,
    hapticFeedback: HapticFeedback
) {
    val coroutineScope = rememberCoroutineScope()
    val task by viewModel.editingTask.collectAsState()
    val taskId = task?.id

    val currentDate by remember { mutableStateOf(viewModel.getCurrentDate()) }

    // UI states
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var dueDate by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(1) }
    var isTaskCompleted by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    // Fetching task details if editing
    LaunchedEffect(taskId) {
        taskId?.let {
            val task = viewModel.getTaskById(it)
            task?.let {
                title = TextFieldValue(task.title)
                description = TextFieldValue(task.description ?: "")
                dueDate = task.dueDate ?: ""
                status = task.status.toInt()
                isTaskCompleted = status == 0
                selectedDate = task.dueDate ?: ""
            }
        }
    }

    // Disabled inputs if the task is completed
    val isEditable = !isTaskCompleted

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (taskId == null) "Add Task" else "Edit Task") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditable,
                    isError = showError && title.text.isEmpty() // Show error if title is empty
                )
                if (showError && title.text.isEmpty()) {
                    Text(
                        text = "Title cannot be empty",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditable,
                    isError = showError && description.text.isEmpty() // Show error if description is empty
                )
                if (showError && description.text.isEmpty()) {
                    Text(
                        text = "Description cannot be empty",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Due Date Button
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditable
                ) {
                    Text(text = if (dueDate.isEmpty()) "Select Due Date" else "Due Date: $dueDate")
                }

                if (showError && selectedDate.isEmpty()) {
                    Text(
                        text = "Please select a due date",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (task != null && task!!.dueDate == currentDate) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Mark as Completed")
                        Spacer(modifier = Modifier.weight(1f))
                        Switch(
                            checked = isTaskCompleted,
                            onCheckedChange = {
                                isTaskCompleted = it
                                status = if (it) 0 else 1
                                if (taskId != null) {
                                    viewModel.updateTask(
                                        id = taskId,
                                        title = title.text,
                                        description = description.text,
                                        dueDate = selectedDate,
                                        status = status
                                    )
                                    hapticFeedback.triggerHaptic()
                                }
                            },
                            enabled = isEditable
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (title.text.isEmpty() || description.text.isEmpty() || selectedDate.isEmpty()) {
                            showError = true // Showing error if either field is empty
                        } else {
                            coroutineScope.launch {
                                if (taskId == null) {
                                    // Adding new task
                                    viewModel.addTask(
                                        title = title.text,
                                        description = description.text,
                                        dueDate = selectedDate,
                                        status = status
                                    )
                                    hapticFeedback.triggerHaptic()
                                } else {
                                    // Updating existing task
                                    viewModel.updateTask(
                                        id = taskId,
                                        title = title.text,
                                        description = description.text,
                                        dueDate = selectedDate,
                                        status = status
                                    )
                                    hapticFeedback.triggerHaptic()
                                }
                                navController.popBackStack() // Navigating back after saving task
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditable
                ) {
                    Text(text = if (taskId == null) "Add Task" else "Save Task")
                }
            }
        }
    )

    // Date Picker Dialog to select due date
    if (showDatePicker) {
        WheelDatePickerView(
            modifier = Modifier
                .padding(top = 18.dp, bottom = 10.dp)
                .fillMaxWidth(),
            showDatePicker = showDatePicker,
            title = "DUE DATE",
            doneLabel = "Done",
            titleStyle = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
            ),
            doneLabelStyle = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight(600),
                color = Color(0xFF007AFF),
            ),
            dateTextColor = Color(0xff007AFF),
            selectorProperties = WheelPickerDefaults.selectorProperties(
                borderColor = Color.LightGray,
            ),
            rowCount = 5,
            height = 180.dp,
            dateTextStyle = TextStyle(
                fontWeight = FontWeight(600),
            ),
            dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
            onDoneClick = {
                selectedDate = it.toString()
                dueDate = selectedDate
                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}


