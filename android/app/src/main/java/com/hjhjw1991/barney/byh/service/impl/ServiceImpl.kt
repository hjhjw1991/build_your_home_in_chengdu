package com.hjhjw1991.barney.byh.service.impl

import android.content.Context
import com.hjhjw1991.barney.byh.service.IKeyMomentLog
import com.hjhjw1991.barney.byh.service.IDemoService
import com.hjhjw1991.barney.serviceprovider.annotation.ServiceImpl
import com.hjhjw1991.barney.util.Logger
import com.hjhjw1991.barney.util.showToast

@ServiceImpl([IDemoService::class])
class DemoService: IDemoService {
    // implement it anywhere
    override fun sayHello(context: Context) {
        context.showToast("hello world")
    }
}

@ServiceImpl([IKeyMomentLog::class])
class KeyMomentLog: IKeyMomentLog {
    override fun recordKeyMoment(moment: IKeyMomentLog.KeyMoment) {
        Logger.log(moment.level, moment.frame)
    }
}

abstract class NothingListener {

}

class FragmentTest(var param: String?, var par2: Context): NothingListener()