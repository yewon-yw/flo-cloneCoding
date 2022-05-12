package com.example.practice

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UserTable")
data class User(
    var email:String,
    val password:String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
