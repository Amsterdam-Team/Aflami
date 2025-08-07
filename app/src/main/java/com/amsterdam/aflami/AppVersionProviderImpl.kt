package com.amsterdam.aflami

import com.amsterdam.domain.utils.AppVersionProvider
import javax.inject.Inject

class AppVersionProviderImpl @Inject constructor() : AppVersionProvider {
    override fun getAppVersion(): String {
        return BuildConfig.VERSION_NAME
    }
}