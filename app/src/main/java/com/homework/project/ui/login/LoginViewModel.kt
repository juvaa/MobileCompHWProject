package com.homework.project.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.project.Graph
import com.homework.project.data.entity.User
import com.homework.project.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository = Graph.userRepository
): ViewModel() {
    init {
        viewModelScope.launch {
            createDummyUser()
        }
    }

    private suspend fun createDummyUser() {
        userRepository.addUser(
            User(
                userName = "user",
                password = "password1"
            )
        )
    }

    suspend fun validateUser(userName: String, password: String) : Boolean {
        val user = userRepository.findUser(userName) ?: return false
        return password == user.password
    }
}
