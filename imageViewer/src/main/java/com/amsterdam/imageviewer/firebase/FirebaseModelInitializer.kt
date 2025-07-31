package com.amsterdam.imageviewer.firebase

import android.content.Context
import androidx.startup.Initializer


internal class FirebaseModelInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        FirebaseNsfwModelManager.initialize(context.applicationContext)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}