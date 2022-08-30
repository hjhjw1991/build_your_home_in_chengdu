package com.hjhjw1991.barney.byh.service

import android.content.Context
import com.hjhjw1991.barney.serviceprovider.annotation.ServiceInterface

@ServiceInterface
interface IDemoService {
    // define api
    fun sayHello(context: Context)
}

@ServiceInterface
interface IKeyMomentLog {
    class KeyMoment {
        var level: String = "default"
        var frame: String = ""
    }

    fun recordKeyMoment(moment: KeyMoment)
}
