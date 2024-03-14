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

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.view.*
import android.webkit.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.just.agentweb.action.PermissionInterceptor
import com.just.agentweb.core.AgentWeb
import com.just.agentweb.core.client.DefaultWebClient
import com.just.agentweb.core.client.MiddlewareWebChromeBase
import com.just.agentweb.core.client.MiddlewareWebClientBase
import com.just.agentweb.core.client.WebListenerManager
import com.just.agentweb.core.web.AbsAgentWebSettings
import com.just.agentweb.core.web.AgentWebConfig
import com.just.agentweb.core.web.IAgentWebSettings
import com.just.agentweb.download.AgentWebDownloader.Extra
import com.just.agentweb.download.DefaultDownloadImpl
import com.just.agentweb.download.DownloadListenerAdapter
import com.just.agentweb.download.DownloadingService
import com.just.agentweb.widget.IWebLayout
import com.xuexiang.templateproject.MyApp
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.core.BaseFragment
import com.xuexiang.templateproject.databinding.FragmentAgentwebBinding
import com.xuexiang.xaop.annotation.SingleClick
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xpage.base.XPageActivity
import com.xuexiang.xpage.base.XPageFragment
import com.xuexiang.xpage.core.PageOption
import com.xuexiang.xui.utils.XToastUtils
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xutil.common.logger.Logger
import com.xuexiang.xutil.net.JsonUtil
import java.util.*

/**
 * 使用XPageFragment
 *
 * @author xuexiang
 * @since 2019-05-26 18:15
 */
@Page(params = [AgentWebFragment.KEY_URL])
class XPageWebViewFragment : BaseFragment<FragmentAgentwebBinding>(), View.OnClickListener {
    private var mAgentWeb: AgentWeb? = null
    private var mPopupMenu: PopupMenu? = null
    private var mDownloadingService: DownloadingService? = null

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentAgentwebBinding {
        return FragmentAgentwebBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initTitle(): TitleBar? {
        return null
    }

    /**
     * 初始化控件
     */
    override fun initViews() {
        mAgentWeb = AgentWeb.with(this) //传入AgentWeb的父控件。
            .setAgentWebParent(
                (rootView as LinearLayout),
                -1,
                LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            ) //设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
            .useDefaultIndicator(-1, 3) //设置 IAgentWebSettings。
            .setAgentWebWebSettings(settings) //WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
            .setWebViewClient(mWebViewClient) //WebChromeClient
            .setWebChromeClient(mWebChromeClient) //设置WebChromeClient中间件，支持多个WebChromeClient，AgentWeb 3.0.0 加入。
            .useMiddlewareWebChrome(middlewareWebChrome) //设置WebViewClient中间件，支持多个WebViewClient， AgentWeb 3.0.0 加入。
            .useMiddlewareWebClient(middlewareWebClient) //权限拦截 2.0.0 加入。
            .setPermissionInterceptor(mPermissionInterceptor) //严格模式 Android 4.2.2 以下会放弃注入对象 ，使用AgentWebView没影响。
            .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK) //自定义UI  AgentWeb3.0.0 加入。
            .setAgentWebUIController(UIController(requireActivity())) //参数1是错误显示的布局，参数2点击刷新控件ID -1表示点击整个布局都刷新， AgentWeb 3.0.0 加入。
            .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
            .setWebLayout(webLayout) //打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
            .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW) //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
            .interceptUnkownUrl() //创建AgentWeb。
            .createAgentWeb()
            .ready() //设置 WebSettings。
            //WebView载入该url地址的页面并显示。
            .go(url)
        if (MyApp.isDebug) {
            AgentWebConfig.debug()
        }
        pageNavigator(View.GONE)
        // 得到 AgentWeb 最底层的控件
        addBackgroundChild(mAgentWeb?.webCreator!!.webParentLayout)

        // AgentWeb 没有把WebView的功能全面覆盖 ，所以某些设置 AgentWeb 没有提供，请从WebView方面入手设置。
        mAgentWeb?.webCreator?.webView?.overScrollMode = WebView.OVER_SCROLL_NEVER
    }

    private val webLayout: IWebLayout<*, *>
        get() = WebLayout(activity)

    private fun addBackgroundChild(frameLayout: FrameLayout) {
        val textView = TextView(frameLayout.context)
        textView.text = "技术由 AgentWeb 提供"
        textView.textSize = 16f
        textView.setTextColor(Color.parseColor("#727779"))
        frameLayout.setBackgroundColor(Color.parseColor("#272b2d"))
        val params = FrameLayout.LayoutParams(-2, -2)
        params.gravity = Gravity.CENTER_HORIZONTAL
        val scale = frameLayout.context.resources.displayMetrics.density
        params.topMargin = (15 * scale + 0.5f).toInt()
        frameLayout.addView(textView, 0, params)
    }

    override fun initListeners() {
        binding?.includeTitle?.let {
            it.ivBack.setOnClickListener(this)
            it.ivFinish.setOnClickListener(this)
            it.ivMore.setOnClickListener(this)
        }
    }

    private fun pageNavigator(tag: Int) {
        //返回的导航按钮
        binding?.includeTitle?.let {
            it.ivBack.visibility = tag
            it.viewLine.visibility = tag
        }
    }

    @SingleClick
    override fun onClick(view: View) {
        val id = view.id
        if (id == R.id.iv_back) {
            // true表示AgentWeb处理了该事件
            if (!mAgentWeb!!.back()) {
                popToBack()
            }
        } else if (id == R.id.iv_finish) {
            popToBack()
        } else if (id == R.id.iv_more) {
            showPoPup(view)
        }
    }
    //=====================下载============================//
    /**
     * 更新于 AgentWeb 4.0.0，下载监听
     */
    protected var mDownloadListenerAdapter: DownloadListenerAdapter =
        object : DownloadListenerAdapter() {
            /**
             *
             * @param url                下载链接
             * @param userAgent          UserAgent
             * @param contentDisposition ContentDisposition
             * @param mimeType           资源的媒体类型
             * @param contentLength      文件长度
             * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
             * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
             */
            override fun onStart(
                url: String,
                userAgent: String,
                contentDisposition: String,
                mimeType: String,
                contentLength: Long,
                extra: Extra
            ): Boolean {
                Logger.i("onStart:$url")
                // 是否开启断点续传
                extra.setOpenBreakPointDownload(true) //下载通知的icon
                    .setIcon(R.drawable.ic_file_download_black_24dp) // 连接的超时时间
                    .setConnectTimeOut(6000) // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                    .setBlockMaxTime(10 * 60 * 1000) // 下载的超时时间
                    .setDownloadTimeOut(Long.MAX_VALUE) // 串行下载更节省资源哦
                    .setParallelDownload(false) // false 关闭进度通知
                    .setEnableIndicator(true) // 自定义请求头
                    .addHeader("Cookie", "xx") // 下载完成自动打开
                    .setAutoOpen(true).isForceDownload = true
                return false
            }

            /**
             *
             * 不需要暂停或者停止下载该方法可以不必实现
             * @param url
             * @param downloadingService  用户可以通过 DownloadingService#shutdownNow 终止下载
             */
            override fun onBindService(url: String, downloadingService: DownloadingService) {
                super.onBindService(url, downloadingService)
                mDownloadingService = downloadingService
                Logger.i("onBindService:$url  DownloadingService:$downloadingService")
            }

            /**
             * 回调onUnbindService方法，让用户释放掉 DownloadingService。
             * @param url
             * @param downloadingService
             */
            override fun onUnbindService(url: String, downloadingService: DownloadingService) {
                super.onUnbindService(url, downloadingService)
                mDownloadingService = null
                Logger.i("onUnbindService:$url")
            }

            /**
             *
             * @param url  下载链接
             * @param loaded  已经下载的长度
             * @param length    文件的总大小
             * @param usedTime   耗时 ，单位ms
             * 注意该方法回调在子线程 ，线程名 AsyncTask #XX 或者 AgentWeb # XX
             */
            override fun onProgress(url: String, loaded: Long, length: Long, usedTime: Long) {
                val mProgress = (loaded / length.toFloat() * 100).toInt()
                Logger.i("onProgress:$mProgress")
                super.onProgress(url, loaded, length, usedTime)
            }

            /**
             *
             * @param path 文件的绝对路径
             * @param url  下载地址
             * @param throwable    如果异常，返回给用户异常
             * @return true 表示用户处理了下载完成后续的事件 ，false 默认交给AgentWeb 处理
             */
            override fun onResult(path: String, url: String, throwable: Throwable): Boolean {
                //下载成功
                if (null == throwable) {
                    //do you work
                } else { //下载失败
                }
                // true  不会发出下载完成的通知 , 或者打开文件
                return false
            }
        }
    /**
     * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
     * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
     * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
     * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
     * @param webView
     * @param downloadListener
     * @return WebListenerManager
     */
    /**
     * 下载服务设置
     *
     * @return IAgentWebSettings
     */
    val settings: IAgentWebSettings<*>
        get() = object : AbsAgentWebSettings() {
            private val mAgentWeb: AgentWeb? = null
            override fun bindAgentWebSupport(agentWeb: AgentWeb) {
                this.mAgentWeb = agentWeb
            }
            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            override fun setDownloader(
                webView: WebView,
                downloadListener: DownloadListener
            ): WebListenerManager {
                return super.setDownloader(
                    webView,
                    DefaultDownloadImpl
                        .create(
                            activity!!,
                            webView,
                            mDownloadListenerAdapter,
                            mDownloadListenerAdapter,
                            mAgentWeb.permissionInterceptor
                        )
                )
            }
        }
    //===================WebChromeClient 和 WebViewClient===========================//
    /**
     * 页面空白，请检查scheme是否加上， scheme://host:port/path?query&query 。
     *
     * @return mUrl
     */
    val url: String
        get() {
            var target = ""
            val bundle = arguments
            if (bundle != null) {
                target = bundle.getString(AgentWebFragment.KEY_URL).toString()
            }
            if (TextUtils.isEmpty(target)) {
                target = "https://github.com/xuexiangjys"
            }
            return target
        }

    /**
     * 和浏览器相关，包括和JS的交互
     */
    protected var mWebChromeClient: WebChromeClient = object : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            //网页加载进度
        }

        override fun onReceivedTitle(view: WebView, title: String) {
            var titleTemp = title
            super.onReceivedTitle(view, titleTemp)
            if (!TextUtils.isEmpty(titleTemp)) {
                if (titleTemp.length > 10) {
                    titleTemp = titleTemp.substring(0, 10) + "..."
                }
                binding?.includeTitle?.toolbarTitle?.text = titleTemp
            }
        }
    }

    /**
     * 和网页url加载相关，统计加载时间
     */
    protected var mWebViewClient: WebViewClient = object : WebViewClient() {
        private val mTimer = HashMap<String, Long?>()
        override fun onReceivedError(
            view: WebView,
            request: WebResourceRequest,
            error: WebResourceError
        ) {
            super.onReceivedError(view, request, error)
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            return shouldOverrideUrlLoading(view, request.url.toString() + "")
        }

        override fun shouldInterceptRequest(
            view: WebView,
            request: WebResourceRequest
        ): WebResourceResponse? {
            return super.shouldInterceptRequest(view, request)
        }

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            //intent:// scheme的处理 如果返回false ， 则交给 DefaultWebClient 处理 ， 默认会打开该Activity  ， 如果Activity不存在则跳到应用市场上去.  true 表示拦截
            //例如优酷视频播放 ，intent://play?...package=com.youku.phone;end;
            //优酷想唤起自己应用播放该视频 ， 下面拦截地址返回 true  则会在应用内 H5 播放 ，禁止优酷唤起播放该视频， 如果返回 false ， DefaultWebClient  会根据intent 协议处理 该地址 ， 首先匹配该应用存不存在 ，如果存在 ， 唤起该应用播放 ， 如果不存在 ， 则跳到应用市场下载该应用 .
            return url.startsWith("intent://") && url.contains("com.youku.phone")
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            mTimer[url] = System.currentTimeMillis()
            if (url == url) {
                pageNavigator(View.GONE)
            } else {
                pageNavigator(View.VISIBLE)
            }
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            if (mTimer[url] != null) {
                val overTime = System.currentTimeMillis()
                val startTime = mTimer[url]
                //统计页面的使用时长
                Logger.i(" page mUrl:" + url + "  used time:" + (overTime - startTime!!))
            }
        }

        override fun onReceivedHttpError(
            view: WebView,
            request: WebResourceRequest,
            errorResponse: WebResourceResponse
        ) {
            super.onReceivedHttpError(view, request, errorResponse)
        }

        override fun onReceivedError(
            view: WebView,
            errorCode: Int,
            description: String,
            failingUrl: String
        ) {
            super.onReceivedError(view, errorCode, description, failingUrl)
        }
    }
    //=====================菜单========================//
    /**
     * 显示更多菜单
     *
     * @param view 菜单依附在该View下面
     */
    private fun showPoPup(view: View) {
        if (mPopupMenu == null) {
            mPopupMenu = PopupMenu(requireContext(), view)
            mPopupMenu?.inflate(R.menu.menu_toolbar_web)
            mPopupMenu?.setOnMenuItemClickListener(mOnMenuItemClickListener)
        }
        mPopupMenu?.show()
    }

    /**
     * 菜单事件
     */
    private val mOnMenuItemClickListener = PopupMenu.OnMenuItemClickListener { item ->
        when (item.itemId) {
            R.id.refresh -> {
                mAgentWeb?.urlLoader?.reload() // 刷新
                true
            }
            R.id.copy -> {
                mAgentWeb?.let {
                    toCopy(context, it.webCreator.webView.url)
                }
                true
            }
            R.id.default_browser -> {
                mAgentWeb?.let {
                    openBrowser(it.webCreator.webView.url)
                }
                true
            }
            R.id.share -> {
                mAgentWeb?.let {
                    shareWebUrl(it.webCreator.webView.url)
                }
                true
            }
            else -> false
        }
    }

    /**
     * 打开浏览器
     *
     * @param targetUrl 外部浏览器打开的地址
     */
    private fun openBrowser(targetUrl: String) {
        if (TextUtils.isEmpty(targetUrl) || targetUrl.startsWith("file://")) {
            XToastUtils.toast("$targetUrl 该链接无法使用浏览器打开。")
            return
        }
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val uri = Uri.parse(targetUrl)
        intent.data = uri
        startActivity(intent)
    }

    /**
     * 分享网页链接
     *
     * @param url 网页链接
     */
    private fun shareWebUrl(url: String) {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.putExtra(Intent.EXTRA_TEXT, url)
        shareIntent.type = "text/plain"
        //设置分享列表的标题，并且每次都显示分享列表
        startActivity(Intent.createChooser(shareIntent, "分享到"))
    }

    /**
     * 复制字符串
     *
     * @param context
     * @param text
     */
    private fun toCopy(context: Context?, text: String) {
        val manager =
            context?.applicationContext?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        manager.setPrimaryClip(ClipData.newPlainText(null, text))
    }

    //===================生命周期管理===========================//
    override fun onResume() {
        if (mAgentWeb != null) {
            mAgentWeb!!.webLifeCycle.onResume() //恢复
        }
        super.onResume()
    }

    override fun onPause() {
        if (mAgentWeb != null) {
            mAgentWeb!!.webLifeCycle.onPause() //暂停应用内所有WebView ， 调用mWebView.resumeTimers();/mAgentWeb.getWebLifeCycle().onResume(); 恢复。
        }
        super.onPause()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return mAgentWeb != null && mAgentWeb!!.handleKeyEvent(keyCode, event)
    }

    override fun onDestroyView() {
        if (mAgentWeb != null) {
            mAgentWeb!!.destroy()
        }
        super.onDestroyView()
    }
    //===================中间键===========================//// 拦截 url，不执行 DefaultWebClient#shouldOverrideUrlLoading
    // 执行 DefaultWebClient#shouldOverrideUrlLoading
    // do you work
    /**
     *
     * @param view
     * @param url
     * @return
     */
    /**
     * MiddlewareWebClientBase 是 AgentWeb 3.0.0 提供一个强大的功能，
     * 如果用户需要使用 AgentWeb 提供的功能， 不想重写 WebClientView方
     * 法覆盖AgentWeb提供的功能，那么 MiddlewareWebClientBase 是一个
     * 不错的选择 。
     *
     * @return
     */
    protected val middlewareWebClient: MiddlewareWebClientBase
        protected get() = object : MiddlewareWebViewClient() {
            /**
             *
             * @param view
             * @param url
             * @return
             */
            /**
             *
             * @param view
             * @param url
             * @return
             */
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                // 拦截 url，不执行 DefaultWebClient#shouldOverrideUrlLoading
                if (url.startsWith("agentweb")) {
                    return true
                }
                // 执行 DefaultWebClient#shouldOverrideUrlLoading
                return super.shouldOverrideUrlLoading(view, url)
                // do you work
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return super.shouldOverrideUrlLoading(view, request)
            }
        }
    protected val middlewareWebChrome: MiddlewareWebChromeBase
        protected get() = object : MiddlewareChromeClient() {}

    /**
     * 权限申请拦截器
     */
    protected var mPermissionInterceptor = PermissionInterceptor { url, permissions, action ->

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        Logger.i("mUrl:" + url + "  permission:" + JsonUtil.toJson(permissions) + " action:" + action)
        false
    }

    companion object {
        /**
         * 打开网页
         *
         * @param xPageActivity
         * @param url
         * @return
         */
        fun openUrl(xPageActivity: XPageActivity?, url: String?): Fragment {
            return PageOption.to(XPageWebViewFragment::class.java)
                .putString(AgentWebFragment.KEY_URL, url)
                .open(xPageActivity!!)
        }

        /**
         * 打开网页
         *
         * @param fragment
         * @param url
         * @return
         */
        fun openUrl(fragment: XPageFragment?, url: String?): Fragment {
            return PageOption.to(XPageWebViewFragment::class.java)
                .setNewActivity(true)
                .putString(AgentWebFragment.KEY_URL, url)
                .open(fragment!!)
        }
    }
}