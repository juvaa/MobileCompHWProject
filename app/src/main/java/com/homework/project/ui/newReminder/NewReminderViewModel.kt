package com.homework.project.ui.newReminder

import androidx.lifecycle.viewModelScope
import com.homework.project.Graph
import com.homework.project.data.UserId
import com.homework.project.data.entity.Reminder
import com.homework.project.data.repository.ReminderRepository
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NewReminderViewModel(
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val userId: UserId = UserId
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
