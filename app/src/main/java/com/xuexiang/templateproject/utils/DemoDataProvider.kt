/*
 * Copyright (C) 2020 xuexiangjys(xuexiangjys@163.com)
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
package com.xuexiang.templateproject.utils

import android.content.Context
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.adapter.entity.NewInfo
import com.xuexiang.xaop.annotation.MemoryCache
import com.xuexiang.xui.adapter.simple.AdapterItem
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem
import java.util.*

/**
 * 演示数据
 *
 * @author xuexiang
 * @since 2018/11/23 下午5:52
 */
object DemoDataProvider {

    var titles = arrayOf(
        "伪装者:胡歌演绎'痞子特工'",
        "无心法师:生死离别!月牙遭虐杀",
        "花千骨:尊上沦为花千骨",
        "综艺饭:胖轩偷看夏天洗澡掀波澜",
        "碟中谍4:阿汤哥高塔命悬一线,超越不可能"
    )
    var urls = arrayOf( //640*360 360/640=0.5625
        "http://photocdn.sohu.com/tvmobilemvms/20150907/144160323071011277.jpg",  //伪装者:胡歌演绎"痞子特工"
        "http://photocdn.sohu.com/tvmobilemvms/20150907/144158380433341332.jpg",  //无心法师:生死离别!月牙遭虐杀
        "http://photocdn.sohu.com/tvmobilemvms/20150907/144160286644953923.jpg",  //花千骨:尊上沦为花千骨
        "http://photocdn.sohu.com/tvmobilemvms/20150902/144115156939164801.jpg",  //综艺饭:胖轩偷看夏天洗澡掀波澜
        "http://photocdn.sohu.com/tvmobilemvms/20150907/144159406950245847.jpg"
    )

    @JvmStatic
    @get:MemoryCache
    val bannerList: List<BannerItem>
        get() {
            val list: MutableList<BannerItem> = ArrayList()
            for (i in urls.indices) {
                val item = BannerItem()
                item.imgUrl = urls[i]
                item.title = titles[i]
                list.add(item)
            }
            return list
        }

    /**
     * 用于占位的空信息
     *
     * @return
     */
    @JvmStatic
    @get:MemoryCache
    val demoNewInfos: List<NewInfo>
        get() {
            val list: MutableList<NewInfo> = ArrayList()
            list.add(
                NewInfo("公众号", "X-Library系列文章视频介绍")
                    .setSummary("获取更多咨询，欢迎点击关注公众号:【我的Android开源之旅】，里面有一整套X-Library系列文章视频介绍！\n")
                    .setDetailUrl("http://mp.weixin.qq.com/mp/homepage?__biz=Mzg2NjA3NDIyMA==&hid=5&sn=bdee5aafe9cc2e0a618d055117c84139&scene=18#wechat_redirect")
                    .setImageUrl("https://p6-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/463930705a844f638433d1b26273a7cf~tplv-k3u1fbpfcp-watermark.image")
            )
            list.add(
                NewInfo("Android UI", "XUI 一个简洁而优雅的Android原生UI框架，解放你的双手")
                    .setSummary("涵盖绝大部分的UI组件：TextView、Button、EditText、ImageView、Spinner、Picker、Dialog、PopupWindow、ProgressBar、LoadingView、StateLayout、FlowLayout、Switch、Actionbar、TabBar、Banner、GuideView、BadgeView、MarqueeView、WebView、SearchView等一系列的组件和丰富多彩的样式主题。\n")
                    .setDetailUrl("https://juejin.im/post/5c3ed1dae51d4543805ea48d")
                    .setImageUrl("https://img-blog.csdnimg.cn/2019011614245559.png")
            )
            list.add(
                NewInfo("Android", "XUpdate 一个轻量级、高可用性的Android版本更新框架")
                    .setSummary("XUpdate 一个轻量级、高可用性的Android版本更新框架。本框架借鉴了AppUpdate中的部分思想和UI界面，将版本更新中的各部分环节抽离出来，形成了如下几个部分：")
                    .setDetailUrl("https://juejin.im/post/5b480b79e51d45190905ef44")
                    .setImageUrl("https://img-blog.csdnimg.cn/20201101003155717.png")
            )
            list.add(
                NewInfo("Android/HTTP", "XHttp2 一个功能强悍的网络请求库，使用RxJava2 + Retrofit2 + OKHttp进行组装")
                    .setSummary("一个功能强悍的网络请求库，使用RxJava2 + Retrofit2 + OKHttp组合进行封装。还不赶紧点击使用说明文档，体验一下吧！")
                    .setDetailUrl("https://juejin.im/post/5b6b9b49e51d4576b828978d")
                    .setImageUrl("https://img-blog.csdnimg.cn/20201101003155717.png")
            )
            list.add(
                NewInfo("源码", "Android源码分析--Android系统启动")
                    .setSummary("其实Android系统的启动最主要的内容无非是init、Zygote、SystemServer这三个进程的启动，他们一起构成的铁三角是Android系统的基础。")
                    .setDetailUrl("https://juejin.im/post/5c6fc0cdf265da2dda694f05")
                    .setImageUrl("https://img-blog.csdnimg.cn/20201101003155717.png")
            )
            return list
        }

    fun getGridItems(context: Context): List<AdapterItem> {
        return getGridItems(context, R.array.grid_titles_entry, R.array.grid_icons_entry)
    }

    private fun getGridItems(
        context: Context,
        titleArrayId: Int,
        iconArrayId: Int
    ): List<AdapterItem> {
        val list: MutableList<AdapterItem> = ArrayList()
        val titles = ResUtils.getStringArray(titleArrayId)
        val icons = ResUtils.getDrawableArray(context, iconArrayId)
        for (i in titles.indices) {
            list.add(AdapterItem(titles[i], icons[i]))
        }
        return list
    }

    /**
     * 用于占位的空信息
     *
     * @return
     */
    @JvmStatic
    @get:MemoryCache
    val emptyNewInfo: List<NewInfo>
        get() {
            val list: MutableList<NewInfo> = ArrayList()
            for (i in 0..4) {
                list.add(NewInfo())
            }
            return list
        }
}