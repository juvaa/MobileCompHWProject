package com.homework.project.ui.reminders

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.project.Graph
import com.homework.project.R
import com.homework.project.data.Ids
import com.homework.project.data.entity.Reminder
import com.homework.project.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

class ReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val ids: Ids = Ids
) : ViewModel() {
    private val _state = MutableStateFlow(ReminderViewState())
    private val _showAll = MutableStateFlow(false)

    val state: StateFlow<ReminderViewState>
        get() = _state

    init {
        createNotificationChannel(context = Graph.appContext)
        viewModelScope.launch {
            reminderRepository.getReminders(ids.Id).collect { reminders ->
                if (_showAll.value) {
                    _state.value = ReminderViewState(reminders)
                } else {
                    val pastReminders = mutableListOf<Reminder>()
                    for (reminder in reminders) {
                        if (reminder.reminder_time == null && reminder.location_x == null && reminder.location_y == null) {
                            pastReminders.add(reminder)
                        } else if (reminder.reminder_time == null && reminder.location_x != null && reminder.location_y != null) { // TODO: Add location check here
                            pastReminders.add(reminder)
                        } else if (reminder.reminder_time!! < Date().time) {
                            pastReminders.add(reminder)
                        }
                    }
                    _state.value = ReminderViewState(pastReminders)
                }
            }
        }
    }

    fun changeShowAllState(state: Boolean) {
        _showAll.value = state
    }

    suspend fun removeReminder(reminder: Reminder) {
        reminderRepository.deleteReminder(reminder)
    }

    fun saveReminderReference(reminder: Reminder) {
        ids.ReminderCreationTime = reminder.creation_time
    }

}

private fun createNotificationChannel(context: Context) {
    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val name = context.getString(R.string.reminder_channel_name)
        val descriptionText = context.getString(R.string.reminder_channel_desc)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(
            context.getString(R.string.reminder_channel_id), name, importance
        ).apply {
            description = descriptionText
        }
        // register the channel with the system
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

data class ReminderViewState(
    val reminders: List<Reminder> = emptyList(),
    val showAll: Boolean = false
)