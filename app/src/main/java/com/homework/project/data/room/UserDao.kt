package com.homework.project.data.room

import androidx.room.*
import com.homework.project.data.entity.User

@Dao
abstract class UserDao {
    @Query("""SELECT * FROM users WHERE id = :userId""")
    abstract suspend fun userById(userId: Long): User

    @Query("""SELECT * FROM users WHERE user_name = :userName""")
    abstract suspend fun userByName(userName: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: User): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: User)

    @Delete
    abstract suspend fun delete(entity: User): Int
}