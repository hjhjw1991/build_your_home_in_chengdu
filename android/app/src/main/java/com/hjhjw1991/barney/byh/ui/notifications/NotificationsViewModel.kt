package com.hjhjw1991.barney.byh.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hjhjw1991.barney.loading.ui.BarneyItem

class NotificationsViewModel : ViewModel() {

    private val _notices = MutableLiveData<List<BarneyItem>>().apply {
        value = mutableListOf(BarneyItem("This is notifications Fragment"))
    }
    val notices: LiveData<List<BarneyItem>> = _notices

    fun requestData(): LiveData<List<BarneyItem>> {
        return if (expired()) {
            _notices.apply {
                (value as MutableList).add(BarneyItem("updated data"))
            }
        } else {
            _notices
        }
    }

    private fun expired(): Boolean = true
}