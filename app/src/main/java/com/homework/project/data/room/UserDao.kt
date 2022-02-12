package com.homework.project.data.room

import androidx.room.*
import com.homework.project.data.entity.User

@Dao
abstract class UserDao {
    @Query("""SELECT * FROM users WHERE id = :userId""")
    abstract fun user(userId: Long): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: User): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun update(entity: User)

    @Delete
    abstract suspend fun delete(entity: User): Int
}