package com.hjhjw1991.barney.byh

import android.app.Application
import android.content.Context
import com.hjhjw1991.barney.serviceprovider.ServiceManager

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // 初始化SPI
        ServiceManager.init(ServiceManager_Proxy.mServices)
    }
}