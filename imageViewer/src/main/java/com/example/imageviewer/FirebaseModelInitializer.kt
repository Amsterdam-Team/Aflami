package com.example.imageviewer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.example.imageviewer.firebase.FirebaseModelManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseModelInitializer : Initializer<Unit> {
    override fun create(context: Context) {

        CoroutineScope(Dispatchers.IO).launch {
            try {
                FirebaseModelManager.getModelFileInstance()
            } catch (e: Exception) {
                Log.e("FirebaseModelInitializer", "Failed to download model on startup.", e)
            }
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
} 