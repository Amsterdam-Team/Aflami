package com.example.localdatasource.roomDataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [], version = 1)
abstract class AflamiDatabase : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "AflamiDatabase"

        @Volatile
        private var instance: AflamiDatabase? = null

        fun getInstance(context: Context): AflamiDatabase {
            return instance ?: synchronized(this) {
                buildDatabase(context).also {
                    instance = it
                }
            }
        }

        private fun buildDatabase(context: Context): AflamiDatabase {
            return Room.databaseBuilder(context, AflamiDatabase::class.java, DATABASE_NAME)
                .createFromAsset("database/$DATABASE_NAME.db")
                .build()
        }
    }
}
