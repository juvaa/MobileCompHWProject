package com.homework.project.ui.newReminder

import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.homework.project.Graph
import com.homework.project.data.Ids
import com.homework.project.data.entity.Reminder
import com.homework.project.data.repository.ReminderRepository
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.homework.project.R
import com.homework.project.util.ReminderNotificationWorker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import java.util.concurrent.TimeUnit

class NewReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val userId: Ids = Ids
) : ViewModel() {
    private val _state = MutableStateFlow(NewReminderViewState())

    val stateNew: StateFlow<NewReminderViewState>
    get() = _state

    suspend fun saveReminder(reminder: Reminder): Long {
        if (reminder.reminder_notification) {
            setOneTimeNotification(reminder)
        }
        return reminderRepository.addReminder(reminder)
    }

    init {

    }

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

data class NewReminderViewState(
    val reminders: List<Reminder> = emptyList()
)
