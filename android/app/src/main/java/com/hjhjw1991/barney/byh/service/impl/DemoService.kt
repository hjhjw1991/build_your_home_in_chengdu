package com.hjhjw1991.barney.byh.service.impl

import android.content.Context
import com.hjhjw1991.barney.byh.service.IAnotherService
import com.hjhjw1991.barney.byh.service.IDemoService
import com.hjhjw1991.barney.serviceprovider.annotation.ServiceImpl
import com.hjhjw1991.barney.util.showToast

@ServiceImpl([IDemoService::class])
class DemoService: IDemoService {
    override fun sayHello(context: Context) {
        context.showToast("hello spi")
    }
}

@ServiceImpl([IAnotherService::class])
class AnotherService: IAnotherService {
    override fun hahaha(context: Context) {
        context.showToast("hahaha this is $context")
    }
}