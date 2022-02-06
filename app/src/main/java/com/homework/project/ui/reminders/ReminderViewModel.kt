package com.homework.project.ui.reminders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.project.data.entity.Reminder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class ReminderViewModel : ViewModel() {
    private val _state = MutableStateFlow(ReminderViewState())

    val state: StateFlow<ReminderViewState>
        get() = _state

    init {
        val list = mutableListOf<Reminder>()
        for (x in 1..6) {
            list.add(
                Reminder(
                    reminderId = x.toLong(),
                    reminderTitle = "$x reminder",
                    reminderMessage = "This is a reminder",
                    reminderDate = Date()
                )
            )
        }
        viewModelScope.launch {
            _state.value = ReminderViewState(
                reminders = list
            )
        }
    }

}

data class ReminderViewState(
    val reminders: List<Reminder> = emptyList()
)