package com.hjhjw1991.barney.byh.ui

import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.util.AttributeSet
import android.view.MotionEvent
import android.webkit.*
import com.hjhjw1991.barney.byh.util.showToast

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
    private val _webViewClient = BaseWebViewClient()
    private val _webChromeClient = BaseWebChromeClient()

    init {
        webViewClient = _webViewClient
        webChromeClient = _webChromeClient
    }

    fun addWebViewClient(client: WebViewClient) {
        _webViewClient.addClient(client)
    }

    fun addWebChromeClient(client: WebChromeClient) {
        _webChromeClient.addClient(client)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }
}

open class BaseWebViewClient: WebViewClient() {
    var delegates = mutableListOf<WebViewClient>()

    fun addClient(client: WebViewClient) {
        delegates.add(client)
    }

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

open class BaseWebChromeClient: WebChromeClient() {
    var delegates = mutableListOf<WebChromeClient>()

    fun addClient(client: WebChromeClient) {
        delegates.add(client)
    }

    override fun onJsAlert(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        view?.context?.showToast("alert: $message")
        return super.onJsAlert(view, url, message, result)
    }

    override fun onJsConfirm(
        view: WebView?,
        url: String?,
        message: String?,
        result: JsResult?
    ): Boolean {
        view?.context?.showToast("confirm: $message")
        var ret = false
        delegates.forEach { ret = ret || it.onJsConfirm(view, url, message, result) }
        return ret
    }

    override fun onJsPrompt(
        view: WebView?,
        url: String?,
        message: String?,
        defaultValue: String?,
        result: JsPromptResult?
    ): Boolean {
        view?.context?.showToast("prompt: $message")
        var ret = false
        delegates.forEach { ret = ret || it.onJsPrompt(view, url, message, defaultValue, result) }
        return ret
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        var ret = false
        delegates.forEach { ret = ret || it.onConsoleMessage(consoleMessage) }
        return ret
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        delegates.forEach { it.onProgressChanged(view, newProgress) }
    }
}

interface IPullRefreshable {
    fun onPull()
    fun onRelease()
    fun onRefresh()
}