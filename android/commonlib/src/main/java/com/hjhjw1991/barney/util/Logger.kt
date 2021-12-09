package com.hjhjw1991.barney.util

import android.util.Log

/**
 * @author huangjun.barney
 * @since 2021/2/6
 */

interface ILogger {
    fun log(str: String?)
    fun log(tag: String, str: String?)
}

object Logger: ILogger {
    const val TAG = "BuildYourHome"

    private val localLogger = object: ILogger {
        override fun log(str: String?) {
            log(TAG, str.orEmpty())
        }

        override fun log(tag: String, str: String?) {
            Log.d(tag, str.orEmpty())
        }
    }

    private var _logger: ILogger = localLogger

    override fun log(str: String?) {
        _logger.log(str)
    }

    override fun log(tag: String, str: String?) {
        _logger.log(tag, str)
    }

    fun setLogger(log: ILogger) {
        _logger = log
    }
}