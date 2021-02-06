package com.hjhjw1991.barney.byh.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hjhjw1991.barney.byh.ui.BarneyWebView

class HomeViewModel : ViewModel() {

    private val _url = MutableLiveData<String>().apply {
        value = "https://cd.yaohaoapp.com/list/surplus"
    }
    val url: LiveData<String> = _url
}