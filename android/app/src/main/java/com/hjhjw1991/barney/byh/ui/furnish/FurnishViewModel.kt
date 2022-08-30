package com.hjhjw1991.barney.byh.ui.furnish

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FurnishViewModel : ViewModel() {

    private val _url = MutableLiveData<String>().apply {
        value = "http://www.zhufaner.com/"
    }
    val url: LiveData<String> = _url
}