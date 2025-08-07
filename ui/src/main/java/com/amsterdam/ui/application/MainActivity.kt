package com.amsterdam.ui.application

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.amsterdam.viewmodel.application.ApplicationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ApplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { !viewModel.state.value.isThemeLoaded }

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AflamiApp()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        if (isFinishing && !isChangingConfigurations){
            if (viewModel.state.value.isDarkTheme){
                switchIcon(LauncherIcon.DARK, this)
            }else {
                switchIcon(LauncherIcon.LIGHT, this)
            }
        }
    }

    enum class LauncherIcon(
        val aliasName: String,
    ) {
        LIGHT("com.amsterdam.ui.application.MainActivity"),
        DARK("com.amsterdam.aflami.DarkLauncher"),
    }

    private fun switchIcon(
        newIcon: LauncherIcon,
        context: Context,
    ) {
        val packageManager = context.packageManager
        val packageName = context.packageName
        LauncherIcon.entries.forEach {
            val componentName = ComponentName(packageName, it.aliasName)
            if (newIcon == it) {
                packageManager.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP,
                )
            } else {
                packageManager.setComponentEnabledSetting(
                    componentName,
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                    PackageManager.DONT_KILL_APP,
                )
            }
        }
    }
}