package com.hjhjw1991.barney.byh.ui.message

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hjhjw1991.barney.loading.ui.BarneyItem

class MessageViewModel : ViewModel() {
    private var messageId = 0

    private val _notices = MutableLiveData<List<BarneyItem>>().apply {
        value = mutableListOf(BarneyItem("This is notifications Fragment"))
    }
    var notices: MutableLiveData<List<BarneyItem>> = _notices

    fun requestData(callback: MessageRequestListener? = null) {
        if (expired()) {
            // todo request server for new messages
            _notices.apply {
                // 消息倒序排列
                (value as MutableList).add(0, BarneyItem("updated data $messageId"))
            }
            messageId += 1
            notices.value = _notices.value
            callback?.onSuccess()
        }
    }

    private fun expired(): Boolean = true

    interface  MessageRequestListener {
        fun onSuccess()
        fun onFailure()
    }
}