package com.homework.project.ui.editReminder

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.systemBarsPadding
import com.homework.project.R
import com.homework.project.data.Ids
import com.homework.project.data.entity.Reminder
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun EditReminder(
    onBackPress: () -> Unit,
    ids: Ids = Ids,
    viewModel: EditReminderViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val viewState by viewModel.state.collectAsState()
    val reminder = viewState.reminder

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar {
                IconButton(
                    onClick = onBackPress
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.ArrowBack)
                    )
                }
                Text(
                    text = "Edit Reminder"
                )
            }
            if (reminder != null) {
                val message = rememberSaveable { mutableStateOf(reminder.message) }
                val reminderTime = rememberSaveable { mutableStateOf(reminder.reminder_time.toString()) }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier.padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = message.value,
                        onValueChange = { message.value = it },
                        label = { Text(text = "Reminder message") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = reminderTime.value,
                        onValueChange = { reminderTime.value = it },
                        label = { Text(text = "Reminder time (epoch time now: ${Date().time})") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        enabled = true,
                        onClick = {
                            coroutineScope.launch {
                                viewModel.updateReminder(
                                    Reminder(
                                        message = message.value,
                                        creator_id = reminder.creator_id,
                                        creation_time = reminder.creation_time,
                                        reminder_time = reminderTime.value.toLong(),
                                        reminder_seen = reminder.reminder_seen,
                                        location_x = reminder.location_x,
                                        location_y = reminder.location_y,
                                    )
                                )
                            }
                            onBackPress()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(55.dp)
                    ) {
                        Text("Save reminder")
                    }
                }
            } else {
                Snackbar {
                    Text("Error while trying to load reminder")
                }
            }
        }
    }
}