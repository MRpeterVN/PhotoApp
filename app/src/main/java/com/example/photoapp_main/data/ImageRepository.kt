package com.example.photoapp_main.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageRepository(private val imageDao: ImageDao) {
    fun insertUser(image: Image) {
        CoroutineScope(Dispatchers.IO).launch {
            imageDao.insert(image)
        }
    }

    fun getAllUsers(): LiveData<List<Image>> {
        return imageDao.getAllUsers()
    }
}
