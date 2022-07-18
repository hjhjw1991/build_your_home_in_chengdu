package com.hjhjw1991.barney.loading.ui

import android.content.Context
import android.util.AttributeSet
import android.view.View
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

object PullRefreshHelper {
    class Builder(val context: Context) {
        private var content: View? = null
        private var indicator: ((ctx: Context) -> Unit)? = null

        fun content(v: View) {
            content = v
        }
        fun indicator(idct: (ctx: Context) -> Unit) {
            indicator = idct
        }
        fun build(): BarneyPullRefreshLayout {
            return BarneyPullRefreshLayout(context, null).apply {
                // todo set content and indicator
            }
        }
    }
}