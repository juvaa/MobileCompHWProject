package com.homework.project.ui.editReminder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.project.Graph
import com.homework.project.data.Ids
import com.homework.project.data.entity.Reminder
import com.homework.project.data.repository.ReminderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
        reminderRepository.updateReminder(reminder)
    }
}

data class EditReminderViewState(
    val reminder: Reminder? = null
)