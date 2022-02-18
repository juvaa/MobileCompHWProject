package com.homework.project.data.repository

import com.homework.project.data.entity.User
import com.homework.project.data.room.UserDao

class UserRepository(
    private val userDao: UserDao
) {
    suspend fun addUser(user: User) = userDao.insert(user)

    suspend fun findUser(userName: String) = userDao.userByName(userName)

    suspend fun getUser(userId: Long) = userDao.userById(userId)
}