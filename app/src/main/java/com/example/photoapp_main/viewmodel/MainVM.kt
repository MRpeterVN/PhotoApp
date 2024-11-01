package com.example.photoapp_main.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.photoapp_main.data.DatabaseBuilder
import com.example.photoapp_main.data.Image
import com.example.photoapp_main.data.ImageRepository

class MainVM(context: Context) : ViewModel() {
    private val imageDao = DatabaseBuilder.getInstance(context).imageDao()
    private val repository = ImageRepository(imageDao)
    val iamge: LiveData<List<Image>> = repository.getAllUsers()
    fun addImage(image: Image) {
        repository.insertUser(image)
    }

}