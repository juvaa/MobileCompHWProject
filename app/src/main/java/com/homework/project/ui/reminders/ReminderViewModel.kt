package com.homework.project.ui.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.project.Graph
import com.homework.project.data.Ids
import com.homework.project.data.entity.Reminder
import com.homework.project.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val ids: Ids = Ids
) : ViewModel() {
    private val _state = MutableStateFlow(ReminderViewState())

    val state: StateFlow<ReminderViewState>
        get() = _state

    init {
        viewModelScope.launch {
            reminderRepository.getReminders(ids.Id).collect { reminders ->
                _state.value = ReminderViewState(reminders)
            }
        }
    }

    suspend fun removeReminder(reminder: Reminder) {
        reminderRepository.deleteReminder(reminder)
    }

    fun saveReminderReference(reminder: Reminder) {
        ids.ReminderCreationTime = reminder.creation_time
    }

}

data class ReminderViewState(
    val reminders: List<Reminder> = emptyList()
)