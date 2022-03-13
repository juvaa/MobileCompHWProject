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
import androidx.navigation.NavController
import com.google.accompanist.insets.systemBarsPadding
import com.google.android.gms.maps.model.LatLng
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
    viewModel: EditReminderViewModel = viewModel(),
    navController: NavController
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
    val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
    var checkedTime: Date?

    val latlng = navController
        .currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<LatLng>("location_data")
        ?.value // TODO: Possibly pass the current location similarly to the map view

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
                val reminderTime = rememberSaveable {
                    mutableStateOf( if (reminder.reminder_time != null) df.format(reminder.reminder_time) else "")
                }
                val reminderIcon = rememberSaveable { mutableStateOf(reminder.reminder_icon) }
                val reminderNotification = rememberSaveable { mutableStateOf(reminder.reminder_notification) }

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
                        label = { Text(text = "Reminder time (${df.toLocalizedPattern()})")},
                        placeholder = { Text(text = df.toLocalizedPattern())},
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                if (parseError.value) error("Invalid date format")
                            }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Location",
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        if (reminder.location_y != null && reminder.location_x != null) { // TODO: Redo the UI so it makes more sense
                            val textString = String.format("Lat: %1$.5f\nLng: %2$.5f", reminder.location_y, reminder.location_x)
                            Text(
                                text = textString,
                            )
                        }
                        if (latlng == null) {
                            TextButton(
                                onClick = {
                                    if (reminder.location_y != null && reminder.location_x != null) {
                                        navController
                                            .currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("latitude", reminder.location_y)
                                        navController
                                            .currentBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("longitude", reminder.location_x)
                                    }
                                    navController.navigate("locationSelection")
                                },
                                modifier = Modifier
                                    .height(55.dp)
                                    .padding(end = 16.dp)
                            ) {
                                Text(text = "Select location")
                            }
                        } else {
                            val textString = String.format("Lat: %1$.5f\nLng: %2$.5f", latlng.latitude, latlng.longitude)
                            Text(
                                text = textString,
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        }
                    }
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
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Notifications",
                            modifier = Modifier.padding(start = 16.dp)
                        )
                        Switch(
                            checked = reminderNotification.value,
                            onCheckedChange = { reminderNotification.value = it },
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        enabled = true,
                        onClick = {
                            if (reminderTime.value.isNotEmpty()) {
                                checkedTime = parseDate(reminderTime.value, df, parseError)
                            } else {
                                parseError.value = false
                                checkedTime = null
                            }

                            if (!parseError.value) {
                                coroutineScope.launch {
                                    viewModel.updateReminder(
                                        Reminder(
                                            message = message.value,
                                            creator_id = reminder.creator_id,
                                            creation_time = reminder.creation_time,
                                            reminder_time = checkedTime?.time,
                                            reminder_seen = reminder.reminder_seen,
                                            location_y = latlng?.latitude ?: reminder.location_y, // TODO: Do this differently so it is possible to have null location
                                            location_x = latlng?.longitude ?: reminder.location_x,
                                            reminder_icon = reminderIcon.value,
                                            reminder_notification = reminderNotification.value
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