package com.example.imageviewer

import android.content.Context
import android.util.Log
import androidx.startup.Initializer
import com.example.imageviewer.firebase.FirebaseModelManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File

class FirebaseModelInitializer : Initializer<Unit> {
    override fun create(context: Context) {

        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {
            try {
                Log.d("firebaseImg","try to create FirebaseModelInitializer")
                FirebaseModelManager.ensureModelDownloaded()
            } catch (e: Exception) {
                Log.d("firebaseImg","unable to create FirebaseModelInitializer")
            }
        }.invokeOnCompletion {
            scope.cancel()
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
} 