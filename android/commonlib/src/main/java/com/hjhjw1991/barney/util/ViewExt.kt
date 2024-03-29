package com.hjhjw1991.barney.util

import android.view.View
import android.view.ViewGroup

/**
 * @author huangjun.barney
 * @since 2021/2/6
 */

fun View.removeSelfFromParent() {
    (this.parent as? ViewGroup)?.removeView(this)
}

fun CharSequence.isNotNullOrEmpty(): Boolean = !isNullOrEmpty()