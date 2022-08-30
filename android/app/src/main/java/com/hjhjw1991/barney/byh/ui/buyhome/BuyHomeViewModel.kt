package com.hjhjw1991.barney.byh.ui.buyhome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BuyHomeViewModel : ViewModel() {

    private val _url = MutableLiveData<String>().apply {
        value = "https://cd.yaohaoapp.com/list/surplus"
    }
    val url: LiveData<String> = _url
}