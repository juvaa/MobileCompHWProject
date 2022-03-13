package com.homework.project.ui.maps

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

class ReminderMapViewModel (
    private val reminderRepository: ReminderRepository = Graph.reminderRepository,
    private val ids: Ids = Ids
) : ViewModel() {
    private val _state = MutableStateFlow(ReminderMapViewState())

    val state: StateFlow<ReminderMapViewState>
        get() = _state

    init {
        viewModelScope.launch {
            reminderRepository.getReminders(ids.Id).collect { reminders ->
                    val pastReminders = mutableListOf<Reminder>()
                    for (reminder in reminders) {
                        if (reminder.location_y != null) {
                            pastReminders.add(reminder)
                        }
                    }
                    _state.value = ReminderMapViewState(pastReminders)
                }
            }
        }
    }

data class ReminderMapViewState(
    val reminders: List<Reminder> = emptyList(),
)