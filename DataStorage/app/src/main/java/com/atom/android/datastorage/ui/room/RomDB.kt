package com.atom.android.datastorage.ui.room

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

import com.facebook.stetho.inspector.protocol.module.Database


@androidx.room.Database(entities = [Student::class], version = 1)
abstract class RomDB : RoomDatabase() {
    abstract fun studentDao(): StudentDAO?

    companion object {
        private const val DATABASE_VERSION = 1
        private var instance: RomDB? = null
        const val DATABASE_NAME = "student"
        fun getInstance(context: Context): RomDB? {
            return instance ?: Room.databaseBuilder(
                context,
                RomDB::class.java, DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
        }
    }
}