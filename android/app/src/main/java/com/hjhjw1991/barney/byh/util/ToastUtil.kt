package com.hjhjw1991.barney.byh.util

import android.content.Context
import android.widget.Toast

/**
 * @author huangjun.barney
 * @since 2021/2/6
 */
object ToastUtil {
    fun show(ctx: Context, str: String) {
        Toast.makeText(ctx.applicationContext, str, Toast.LENGTH_SHORT).show()
    }
}