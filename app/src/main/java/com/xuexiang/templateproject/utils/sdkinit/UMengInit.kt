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
package com.xuexiang.templateproject.utils.sdkinit

import android.app.Application
import android.content.Context
import com.xuexiang.templateproject.utils.SettingUtils.Companion.isAgreePrivacy
import kotlin.jvm.JvmOverloads
import com.xuexiang.xui.XUI
import com.xuexiang.templateproject.utils.sdkinit.UMengInit
import com.xuexiang.templateproject.MyApp
import com.umeng.commonsdk.UMConfigure
import com.xuexiang.templateproject.utils.SettingUtils
import com.umeng.analytics.MobclickAgent
import com.meituan.android.walle.WalleChannelReader
import com.xuexiang.templateproject.BuildConfig
import java.lang.UnsupportedOperationException

/**
 * UMeng 统计 SDK初始化
 *
 * @author xuexiang
 * @since 2019-06-18 15:49
 */
class UMengInit private constructor() {
    companion object {
        private const val DEFAULT_CHANNEL_ID = "github"
        /**
         * 初始化SDK,合规指南【先进行预初始化，如果用户隐私同意后可以初始化UmengSDK进行信息上报】
         */
        /**
         * 初始化SDK,合规指南【先进行预初始化，如果用户隐私同意后可以初始化UmengSDK进行信息上报】
         */
        @JvmOverloads
        fun init(context: Context = XUI.getContext()) {
            val appContext = context.applicationContext
            if (appContext is Application) {
                initApplication(appContext)
            }
        }

        /**
         * 初始化SDK,合规指南【先进行预初始化，如果用户隐私同意后可以初始化UmengSDK进行信息上报】
         */
        fun initApplication(application: Application?) {
            // 运营统计数据调试运行时不初始化
            if (MyApp.isDebug) {
                return
            }
            UMConfigure.setLogEnabled(false)
            UMConfigure.preInit(application, BuildConfig.APP_ID_UMENG, getChannel(application))
            // 用户同意了隐私协议
            if (isAgreePrivacy) {
                realInit(application)
            }
        }

        /**
         * 真实的初始化UmengSDK【进行设备信息的统计上报，必须在获得用户隐私同意后方可调用】
         */
        private fun realInit(application: Application?) {
            // 运营统计数据调试运行时不初始化
            if (MyApp.isDebug) {
                return
            }
            //初始化组件化基础库, 注意: 即使您已经在AndroidManifest.xml中配置过appkey和channel值，也需要在App代码中调用初始化接口（如需要使用AndroidManifest.xml中配置好的appkey和channel值，UMConfigure.init调用中appkey和channel参数请置为null）。
            //第二个参数是appkey，最后一个参数是pushSecret
            //这里BuildConfig.APP_ID_UMENG是根据local.properties中定义的APP_ID_UMENG生成的，只是运行看效果的话，可以不初始化该SDK
            UMConfigure.init(
                application,
                BuildConfig.APP_ID_UMENG,
                getChannel(application),
                UMConfigure.DEVICE_TYPE_PHONE,
                ""
            )
            //统计SDK是否支持采集在子进程中打点的自定义事件，默认不支持
            //支持多进程打点
            UMConfigure.setProcessEvent(true)
            MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        }

        /**
         * 获取渠道信息
         *
         * @param context
         * @return
         */
        fun getChannel(context: Context?): String? {
            return WalleChannelReader.getChannel(context!!, DEFAULT_CHANNEL_ID)
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}