package com.example.photoapp_main.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ImageDao {
    @Insert
    suspend fun insert(image: Image)

    @Query("SELECT * FROM image_table ORDER BY id DESC")
    fun getAllImage(): LiveData<List<Image>>

    @Query("DELETE FROM image_table WHERE uri = :uri")
    suspend fun delete(uri: String)
}
