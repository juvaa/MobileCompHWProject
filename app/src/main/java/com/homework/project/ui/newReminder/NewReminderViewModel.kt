package com.homework.project.ui.newReminder

import com.homework.project.Graph
import com.homework.project.data.Ids
import com.homework.project.data.entity.Reminder
import com.homework.project.data.repository.ReminderRepository
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NewReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val userId: Ids = Ids
) : ViewModel() {
    private val _state = MutableStateFlow(NewReminderViewState())

    val stateNew: StateFlow<NewReminderViewState>
    get() = _state

    suspend fun saveReminder(reminder: Reminder): Long {
        return reminderRepository.addReminder(reminder)
    }

    init {

    }

}

data class NewReminderViewState(
    val reminders: List<Reminder> = emptyList()
)
