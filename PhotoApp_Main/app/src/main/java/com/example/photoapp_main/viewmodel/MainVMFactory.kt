package com.example.photoapp_main.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainVMFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainVM::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainVM(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
