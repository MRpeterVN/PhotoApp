package com.example.photoapp_main.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class Image(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val uri: String
)

