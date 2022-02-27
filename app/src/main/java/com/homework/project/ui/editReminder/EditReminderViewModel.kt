package com.homework.project.ui.editReminder

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.homework.project.Graph
import com.homework.project.R
import com.homework.project.data.Ids
import com.homework.project.data.entity.Reminder
import com.homework.project.data.repository.ReminderRepository
import com.homework.project.util.ReminderNotificationWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class EditReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val ids: Ids = Ids
) : ViewModel() {
    private val _state = MutableStateFlow(EditReminderViewState())

    val state: StateFlow<EditReminderViewState>
        get() = _state

    init {
        viewModelScope.launch {
            _state.value = EditReminderViewState(
                reminder = reminderRepository.getReminder(
                    creatorId = ids.Id,
                    creationTime = ids.ReminderCreationTime
                )
            )
        }
    }

    suspend fun updateReminder(reminder: Reminder) {
        cancelPreviousNotification(reminder)
        setOneTimeNotification(reminder)
        reminderRepository.updateReminder(reminder)
    }
}

private fun cancelPreviousNotification(reminder: Reminder) {
    val reminderId = reminder.creation_time.toString() + reminder.creator_id.toString()
    val workManager = WorkManager.getInstance(Graph.appContext)

    workManager.cancelAllWorkByTag(reminderId)
}

private fun setOneTimeNotification(reminder: Reminder) {
    val reminderId = reminder.creation_time.toString() + reminder.creator_id.toString()
    val workDelay = reminder.reminder_time!! - Date().time
    val workManager = WorkManager.getInstance(Graph.appContext)
    val constraints = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
        .build()

    val notificationWorker = OneTimeWorkRequestBuilder<ReminderNotificationWorker>()
        .addTag(reminderId)
        .setInitialDelay(workDelay, TimeUnit.MILLISECONDS)
        .setConstraints(constraints)
        .build()

    workManager.enqueue(notificationWorker)

    workManager.getWorkInfoByIdLiveData(notificationWorker.id)
        .observeForever { workInfo ->
            if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                createReminderNotification(reminder)
            }
        }
}

private fun createReminderNotification(reminder: Reminder) {
    val notificationId = 1
    val builder = NotificationCompat.Builder(Graph.appContext, Graph.appContext.getString(R.string.reminder_channel_id))
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle("New reminder")
        .setContentText(reminder.message)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
    with(NotificationManagerCompat.from(Graph.appContext)) {
        notify(notificationId, builder.build())
    }
}

data class EditReminderViewState(
    val reminder: Reminder? = null
)