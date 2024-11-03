package com.example.photoapp_main.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImageRepository(private val imageDao: ImageDao) {
    fun insertImage(image: Image) {
        CoroutineScope(Dispatchers.IO).launch {
            imageDao.insert(image)
        }
    }

    fun getAllImage(): LiveData<List<Image>> {
        return imageDao.getAllImage()
    }

    fun deleteImage(uri: String) {
        CoroutineScope(Dispatchers.IO).launch {
            imageDao.delete(uri)
        }
    }
}
