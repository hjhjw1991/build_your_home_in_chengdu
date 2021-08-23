package com.hjhjw1991.barney.byh.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.AttributeSet
import android.webkit.*

/**
 * Hybrid容器, 提供跨端页面访问和基础封装
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

    init {
        webViewClient = DefaultWebViewClient()
        webChromeClient = DefaultWebChromeClient()
    }
}

open class DefaultWebViewClient: WebViewClient() {
    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        super.onReceivedSslError(view, handler, error)
    }

    override fun shouldInterceptRequest(
        view: WebView?,
        request: WebResourceRequest?
    ): WebResourceResponse? {
        return super.shouldInterceptRequest(view, request)
    }
}

open class DefaultWebChromeClient: WebChromeClient() {
    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        return super.onJsAlert(view, url, message, result)
    }

    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        return super.onJsConfirm(view, url, message, result)
    }

    override fun onJsPrompt(
        view: WebView?,
        url: String?,
        message: String?,
        defaultValue: String?,
        result: JsPromptResult?
    ): Boolean {
        return super.onJsPrompt(view, url, message, defaultValue, result)
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        return super.onConsoleMessage(consoleMessage)
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
    }
}