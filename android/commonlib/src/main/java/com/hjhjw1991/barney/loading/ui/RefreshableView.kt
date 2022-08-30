package com.hjhjw1991.barney.loading.ui

import android.content.Context
import android.util.AttributeSet
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.hjhjw1991.barney.util.Logger

/**
 * 下拉刷新列表
 * @author huangjun.barney
 * @since 2021/12/7
 */
interface IRefreshable<T> {
    fun refresh(t: List<T>)
}

open class BarneyPullRefreshLayout(private val ctx: Context, attrs: AttributeSet?) :
    SwipeRefreshLayout(ctx, attrs), IRefreshable<BarneyItem> {
    override fun refresh(t: List<BarneyItem>) {
        Logger.log("updating $t")
    }
}

open class BarneyItem(private val value: String) {
    override fun toString(): String = value
}