package com.example.photoapp_main.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert
    suspend fun insert(user: Image)

    @Query("SELECT * FROM image_table ORDER BY id DESC")
    fun getAllUsers(): LiveData<List<Image>>

    @Delete
    suspend fun delete(user: Image)
}
