package com.example.practice

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
// User 데이터 클래스 name 추가
// Json으로 request 값을 보낼때엔 SerializedName(value = "key명") 작성해줘야 함
@Entity(tableName = "UserTable")
data class User(
    @SerializedName(value = "email") var email:String,
    @SerializedName(value = "password") var password:String,
    @SerializedName(value = "name") var name:String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
