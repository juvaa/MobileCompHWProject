package com.homework.project.ui.reminders

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.homework.project.Graph
import com.homework.project.R
import com.homework.project.data.LocationModel
import com.homework.project.data.entity.Reminder
import com.homework.project.util.ReminderIcons
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun Reminder(
    navController: NavController
) {
    val viewModel: ReminderViewModel = viewModel()
    val viewState by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    val longitude = remember{ mutableStateOf(0.0)}
    val latitude = remember{ mutableStateOf(0.0)}

    val locationObserver = Observer<LocationModel> {
        longitude.value = it.longitude
        latitude.value = it.latitude
    }

    viewModel.getLocationData().observe(LocalLifecycleOwner.current, locationObserver)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val textString = String.format("Current location Lat: %1$.5f, Lng: %2$.5f", latitude.value, longitude.value)
        TextButton(
            onClick = { navController.navigate("reminderMap") },
            modifier = Modifier
                .height(55.dp)
                .padding(end = 16.dp)
        ) {
            Text(text = textString)
        }
        ReminderList(
            list = viewState.reminders,
            viewModel = viewModel,
            coroutineScope = coroutineScope,
            navController = navController
        )
    }
}

@Composable
private fun ReminderList(
    list: List<Reminder>,
    viewModel: ReminderViewModel,
    coroutineScope: CoroutineScope,
    navController: NavController
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 102.dp),
        verticalArrangement = Arrangement.Center
    ) {
        items(list) { item ->
            ReminderListItem(
                reminder = item,
                onClick = { editReminder(item, navController, viewModel) },
                modifier = Modifier.fillParentMaxWidth(),
                viewModel = viewModel,
                coroutineScope = coroutineScope
            )
        }
    }
}

@Composable
private fun ReminderListItem(
    reminder: Reminder,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReminderViewModel,
    coroutineScope: CoroutineScope
) {
    val openDeleteDialog = remember { mutableStateOf(false)}
    if (openDeleteDialog.value) {
        AlertDialog(
            onDismissRequest = { openDeleteDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        removeReminder(reminder, viewModel, coroutineScope)
                        openDeleteDialog.value = false
                    },
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDeleteDialog.value = false
                    }
                ) {
                    Text("Cancel")
                }
            },
            title = {
                Text("Delete reminder?")
            },
            text = {
                Text("Are you sure you want to delete this reminder?")
            }
        )
    }

    ConstraintLayout(modifier = modifier.clickable { onClick() }) {
        val (divider, icon, reminderMessage, date, notifications, deleteIcon) = createRefs()
        val reminderIcon = when (reminder.reminder_icon) {
            ReminderIcons.DEFAULT -> Icons.Filled.Event
            ReminderIcons.CAKE -> Icons.Filled.Cake
            ReminderIcons.GROUPS -> Icons.Filled.Groups
            ReminderIcons.SCHOOL -> Icons.Filled.School
        }

        Divider(
            Modifier.constrainAs(divider) {
                top.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
                width = Dimension.fillToConstraints
            }
        )

        // icon
        Icon(
            imageVector = reminderIcon,
            contentDescription = null,
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(icon) {
                    start.linkTo(parent.start)
                    centerVerticallyTo(parent)
                }
        )

        // message
        Text(
            text = reminder.message,
            maxLines = 1,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.constrainAs(reminderMessage) {
                linkTo(
                    start = icon.end,
                    end = notifications.start,
                    startMargin = 8.dp,
                    endMargin = 8.dp,
                    bias = 0f
                )
                top.linkTo(parent.top, 8.dp)
                bottom.linkTo(date.top, 4.dp)
                width = Dimension.preferredWrapContent
            }
        )

        // date
        if (reminder.reminder_time != null) {
            Text(
                text = reminder.reminder_time.toDateString(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.constrainAs(date) {
                    linkTo(
                        start = icon.end,
                        end = notifications.start,
                        startMargin = 8.dp,
                        endMargin = 8.dp,
                        bias = 0f
                    )
                    top.linkTo(reminderMessage.bottom, 4.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    width = Dimension.preferredWrapContent
                }
            )
        } else if (reminder.location_x != null && reminder.location_y != null) {
            val textString = String.format("Lat: %1$.5f, Lng: %2$.5f", reminder.location_y, reminder.location_x)
            Text(
                text = textString,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.caption,
                modifier = Modifier.constrainAs(date) {
                    linkTo(
                        start = icon.end,
                        end = notifications.start,
                        startMargin = 8.dp,
                        endMargin = 8.dp,
                        bias = 0f
                    )
                    top.linkTo(reminderMessage.bottom, 4.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    width = Dimension.preferredWrapContent
                }
            )
        }

        // notifications button
        IconButton(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(notifications) {
                    top.linkTo(parent.top, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    end.linkTo(deleteIcon.start)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Notifications,
                contentDescription = stringResource(R.string.Notifications),
            )
        }
        // delete button
        IconButton(
            onClick = { openDeleteDialog.value = true },
            modifier = Modifier
                .size(50.dp)
                .padding(6.dp)
                .constrainAs(deleteIcon) {
                    top.linkTo(parent.top, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    end.linkTo(parent.end)
                }
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.Delete),
            )
        }
    }
}

private fun removeReminder(
    reminder: Reminder,
    viewModel: ReminderViewModel,
    coroutineScope: CoroutineScope
) {
    coroutineScope.launch { viewModel.removeReminder(reminder) }
}

private fun editReminder(
    reminder: Reminder,
    navController: NavController,
    viewModel: ReminderViewModel
) {
    viewModel.saveReminderReference(reminder)
    navController.navigate(route = "editReminder")
}

private fun Date.formatToString(): String {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()).format(this)
}

private fun Long.toDateString(): String {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault()).format(Date(this))
}