package com.hjhjw1991.barney.byh.util

import android.content.Context
import android.content.res.Resources.NotFoundException
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * @author huangjun.barney
 * @since 2021/2/6
 */
fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Context.getString(@StringRes id: Int): String? {
    return try {
        resources.getString(id)
    } catch (e: NotFoundException) {
        null
    }
}