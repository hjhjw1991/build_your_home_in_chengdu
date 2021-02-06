package com.hjhjw1991.barney.byh.ui

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

/**
 * @author huangjun.barney
 * @since 2021/2/6
 */

open class BarneyWebView(
    ctx: Context,
    attrs: AttributeSet?,
    defStyle: Int
    ): WebView(ctx,attrs,defStyle) {
    @JvmOverloads
    constructor(ctx: Context, attrs: AttributeSet?): this(ctx, attrs, 0)
}