package com.android.basics.features.todo.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.android.basics.features.todo.data.source.local.entity.UserTbl

@Dao
interface UserDao {

    @Query("INSERT INTO user (userId, userName, password) VALUES (:userId, :userName, :password)")
    fun insert(
        userId: String?,
        userName: String?,
        password: String?
    )

    @Query("SELECT * from user WHERE userName =:userName AND password=:passWord")
    fun getUser(userName: String?, passWord: String?): UserTbl?

    @Query("DELETE FROM user")
    fun deleteAllUsers()

}