package com.homework.project.ui.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.project.Graph
import com.homework.project.data.Ids
import com.homework.project.data.entity.User
import com.homework.project.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userRepository: UserRepository = Graph.userRepository,
    private val userId: Ids = Ids
) : ViewModel() {
    private val _state = MutableStateFlow(UserProFileViewState())

    val state: StateFlow<UserProFileViewState>
        get() = _state

    init {
        viewModelScope.launch {
            _state.value = UserProFileViewState(
                user = userRepository.getUser(userId.Id)
            )
        }
    }
}

data class UserProFileViewState(
    val user: User? = null
)