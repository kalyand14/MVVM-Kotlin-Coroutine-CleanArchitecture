package com.android.basics.features.todo.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.android.basics.features.todo.domain.model.User
import java.util.*


@Entity(tableName = "user", indices = [Index(value = ["userName", "password"], unique = true)])
data class UserTbl(
    @PrimaryKey @ColumnInfo(name = "userId") var userId: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "userName") var userName: String = "",
    @ColumnInfo(name = "password") var password: String = ""
) {
    fun toUser() = User(
        userId,
        userName,
        password
    )
}