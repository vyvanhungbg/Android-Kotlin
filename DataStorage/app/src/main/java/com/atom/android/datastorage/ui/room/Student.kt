package com.atom.android.datastorage.ui.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Student(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") val name: String?,
)