package com.example.practice

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Song::class], version = 1)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object{
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if(instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database" // 다른 데이터베이스와 이름이 같으면 꼬임
                    ).allowMainThreadQueries().build() // 원래는 메인스레드에 일을 넘기면 비효율적이나 실습에선 일단 이렇게 사용
                }
            }

            return instance
        }
    }
}