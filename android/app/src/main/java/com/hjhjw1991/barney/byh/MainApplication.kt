package com.hjhjw1991.barney.byh

import android.app.Application
import android.content.Context
import com.hjhjw1991.barney.byh.util.showToast
import com.hjhjw1991.barney.serviceprovider.ServiceManager
import com.hjhjw1991.barney.serviceprovider.annotation.ServiceImpl
import com.hjhjw1991.barney.serviceprovider.annotation.ServiceInterface

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        ServiceManager.bindService(IDemoService::class.java, DemoService())
    }
}

@ServiceInterface
interface IDemoService {
    fun sayHello(context: Context)
}

@ServiceImpl([IDemoService::class])
class DemoService: IDemoService {
    override fun sayHello(context: Context) {
        context.showToast("hello spi")
    }
}