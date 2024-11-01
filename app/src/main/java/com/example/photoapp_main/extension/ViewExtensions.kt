package com.example.photoapp_main.extension

import android.os.SystemClock
import android.view.View

private var lastClickTime: Long = 0

fun View.setOnSafeClickListener(debounceTime: Long = 1000, onClick: (View) -> Unit) {
    setOnClickListener {
        if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return@setOnClickListener
        lastClickTime = SystemClock.elapsedRealtime()
        onClick(it)
    }
}
