package com.hjhjw1991.barney.byh.service

import android.content.Context
import com.hjhjw1991.barney.serviceprovider.annotation.ServiceInterface

@ServiceInterface
interface IDemoService {
    fun sayHello(context: Context)
}

@ServiceInterface
interface IAnotherService {
    fun hahaha(context: Context)
}