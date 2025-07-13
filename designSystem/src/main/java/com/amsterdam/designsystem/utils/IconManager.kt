package com.amsterdam.designsystem.utils

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

enum class LauncherIcon(val aliasName: String) {
    LIGHT("com.amsterdam.ui.application.MainActivity"),
    DARK("com.amsterdam.aflami.DarkLauncher")
}

@Composable
fun SwitchLauncherIcon(newIcon: LauncherIcon) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> {
                    switchIcon(newIcon, context)
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}


private fun switchIcon(newIcon: LauncherIcon, context: Context) {
    val packageManager = context.packageManager
    val packageName = context.packageName

    LauncherIcon.entries.forEach {
        val componentName = ComponentName(packageName, it.aliasName)
        if (newIcon == it){
            packageManager.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        } else {
            packageManager.setComponentEnabledSetting(
                componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP
            )
        }
    }
}
