package com.hjhjw1991.barney.util

import android.view.View
import android.view.ViewGroup

/**
 * View Extension
 * @author huangjun.barney
 * @since 2021/2/6
 */

fun View?.removeSelfFromParent() {
    if (this == null) return
    (this.parent as? ViewGroup)?.removeView(this)
}

fun CharSequence.isNotNullOrEmpty(): Boolean = !isNullOrEmpty()