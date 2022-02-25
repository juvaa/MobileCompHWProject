package com.homework.project.ui.editReminder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.insets.systemBarsPadding
import com.homework.project.R
import com.homework.project.data.entity.Reminder
import com.homework.project.util.ReminderIcons
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun EditReminder(
    onBackPress: () -> Unit,
    viewModel: EditReminderViewModel = viewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val viewState by viewModel.state.collectAsState()
    val reminder = viewState.reminder

    var expanded by remember { mutableStateOf(false) }
    val dropdownIcon = if (expanded) {
        Icons.Filled.ArrowDropUp
    } else {
        Icons.Filled.ArrowDropDown
    }

    val parseError = rememberSaveable { mutableStateOf(false) }
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    var checkedTime: Date?

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.secondary
            ) {
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
                val reminderTime = rememberSaveable { mutableStateOf(df.format(reminder.reminder_time)) }
                val reminderIcon = rememberSaveable { mutableStateOf(reminder.reminder_icon) }

                val icon = when (reminderIcon.value) {
                    ReminderIcons.DEFAULT -> Icons.Filled.Event
                    ReminderIcons.CAKE -> Icons.Filled.Cake
                    ReminderIcons.GROUPS -> Icons.Filled.Groups
                    ReminderIcons.SCHOOL -> Icons.Filled.School
                }

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
                        isError = parseError.value,
                        value = reminderTime.value,
                        onValueChange = {
                            reminderTime.value = it
                            parseError.value = false
                        },
                        label = { Text(text = "Reminder time (format ${df.toLocalizedPattern()})")},
                        placeholder = { Text(text = df.toLocalizedPattern())},
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                if (parseError.value) error("Invalid date format")
                            }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = reminderIcon.value.toString(),
                            onValueChange = { },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Icon") },
                            readOnly = true,
                            trailingIcon = {
                                Icon(
                                    imageVector = dropdownIcon,
                                    contentDescription = null,
                                    modifier = Modifier.clickable { expanded = !expanded }
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null
                                )
                            }
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            ReminderIcons.values().forEach { dropDownOption ->
                                DropdownMenuItem(
                                    onClick = {
                                        reminderIcon.value = dropDownOption
                                        expanded = false
                                    }
                                ) {
                                    val menuItemIcon = when (dropDownOption) {
                                        ReminderIcons.DEFAULT -> Icons.Filled.Event
                                        ReminderIcons.CAKE -> Icons.Filled.Cake
                                        ReminderIcons.GROUPS -> Icons.Filled.Groups
                                        ReminderIcons.SCHOOL -> Icons.Filled.School
                                    }
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Icon(
                                            imageVector = menuItemIcon,
                                            contentDescription = null,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                        Text(
                                            text = dropDownOption.name,
                                            modifier = Modifier.padding(8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        enabled = true,
                        onClick = {
                            checkedTime = parseDate(reminderTime.value , df, parseError)

                            if (!parseError.value) {
                                coroutineScope.launch {
                                    viewModel.updateReminder(
                                        Reminder(
                                            message = message.value,
                                            creator_id = reminder.creator_id,
                                            creation_time = reminder.creation_time,
                                            reminder_time = checkedTime!!.time,
                                            reminder_seen = reminder.reminder_seen,
                                            location_x = reminder.location_x,
                                            location_y = reminder.location_y,
                                            reminder_icon = reminderIcon.value
                                        )
                                    )
                                }
                                onBackPress()
                            }
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

fun parseDate(
    dateString: String,
    df: DateFormat,
    parseError: MutableState<Boolean>
) : Date? {
    return try {
        val date: Date? = df.parse(dateString)
        parseError.value = date == null
        date
    } catch (e: ParseException) {
        parseError.value = true
        null
    }
}