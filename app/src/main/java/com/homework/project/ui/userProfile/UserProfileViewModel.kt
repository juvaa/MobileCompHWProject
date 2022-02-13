package com.homework.project.ui.userProfile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.project.Graph
import com.homework.project.data.UserId
import com.homework.project.data.entity.User
import com.homework.project.data.repository.UserRepository
import kotlinx.coroutines.launch

class UserProfileViewModel(
    private val userRepository: UserRepository = Graph.userRepository,
    private val userId: UserId = UserId
) : ViewModel() {

    fun getUser() : User? {
        var user: User? = null
        viewModelScope.launch { user = userRepository.getUser(userId.Id) }
        return user
    }
}