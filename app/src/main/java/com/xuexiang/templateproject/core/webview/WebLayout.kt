/*
 * Copyright (C) 2019 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.xuexiang.templateproject.core.webview

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.WebView
import com.just.agentweb.widget.IWebLayout
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.xuexiang.templateproject.R

/**
 * 定义支持下来回弹的WebView
 *
 * @author xuexiang
 * @since 2019/1/5 上午2:01
 */
class WebLayout(activity: Activity?) : IWebLayout<WebView?, ViewGroup?> {
    private val mSmartRefreshLayout: SmartRefreshLayout
    private val mWebView: WebView
    override fun getLayout(): ViewGroup {
        return mSmartRefreshLayout
    }

    override fun getWebView(): WebView {
        return mWebView
    }

    init {
        mSmartRefreshLayout = LayoutInflater.from(activity)
            .inflate(R.layout.fragment_pulldown_web, null) as SmartRefreshLayout
        mWebView = mSmartRefreshLayout.findViewById(R.id.webView)
    }
}