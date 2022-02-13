package com.homework.project.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.project.Graph
import com.homework.project.data.entity.User
import com.homework.project.data.repository.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userRepository: UserRepository = Graph.userRepository,
    private var _user: User? = null
): ViewModel() {

    init {
        viewModelScope.launch {
            if (userRepository.findUser("user") == null)
                createDummyUser()
        }
    }

    private suspend fun createDummyUser() {
        userRepository.addUser(
            User(
                userName = "user",
                password = "password1",
                firstname = "John",
                lastname = "Doe"
            )
        )
    }

    suspend fun validateUser(userName: String, password: String) : Boolean {
        _user = userRepository.findUser(userName) ?: return false
        return password == _user!!.password
    }

    fun getUserId() : Long = _user!!.userId
}
