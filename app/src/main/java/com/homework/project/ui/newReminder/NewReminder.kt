package com.homework.project.ui.newReminder

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
import com.homework.project.data.Ids
import com.homework.project.data.entity.Reminder
import com.homework.project.util.ReminderIcons
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NewReminder(
    onBackPress: () -> Unit,
    userId: Ids = Ids,
    viewModel: NewReminderViewModel = viewModel()
) {
    val message = rememberSaveable { mutableStateOf("") }
    val reminderTime = rememberSaveable { mutableStateOf("") }
    val reminderIcon = rememberSaveable { mutableStateOf(ReminderIcons.DEFAULT) }
    val coroutineScope = rememberCoroutineScope()

    val icon = when (reminderIcon.value) {
        ReminderIcons.DEFAULT -> Icons.Filled.Event
        ReminderIcons.CAKE -> Icons.Filled.Cake
        ReminderIcons.GROUPS -> Icons.Filled.Groups
        ReminderIcons.SCHOOL -> Icons.Filled.School
    }

    var expanded by remember { mutableStateOf(false) }
    val dropdownIcon = if (expanded) {
        Icons.Filled.ArrowDropUp
    } else {
        Icons.Filled.ArrowDropDown
    }

    val parseError = rememberSaveable { mutableStateOf(false) }
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
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
                    text = "New Reminder"
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier.padding(16.dp)
            ) {
                OutlinedTextField(
                    value = message.value,
                    onValueChange = { message.value = it },
                    label = { Text(text = "Reminder message")},
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
                    label = { Text(text = "Reminder time (${df.toLocalizedPattern()})")},
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
                                viewModel.saveReminder(
                                    Reminder(
                                        message = message.value,
                                        creator_id = userId.Id,
                                        creation_time = Date().time,
                                        reminder_time = checkedTime!!.time,
                                        location_x = null,
                                        location_y = null,
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