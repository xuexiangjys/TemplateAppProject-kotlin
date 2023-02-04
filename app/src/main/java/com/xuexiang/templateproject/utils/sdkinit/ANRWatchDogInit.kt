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
package com.xuexiang.templateproject.utils.sdkinit

import com.github.anrwatchdog.ANRError
import com.github.anrwatchdog.ANRWatchDog
import com.github.anrwatchdog.ANRWatchDog.ANRListener
import com.xuexiang.xutil.common.logger.Logger

/**
 * ANR看门狗监听器初始化
 *
 * @author xuexiang
 * @since 2020-02-18 15:08
 */
object ANRWatchDogInit {
    private const val TAG = "ANRWatchDog"

    /**
     * ANR看门狗
     */
    private var mANRWatchDog: ANRWatchDog? = null

    /**
     * ANR监听触发的时间
     */
    private const val ANR_DURATION = 4000

    /**
     * ANR静默处理【就是不处理，直接记录一下日志】
     */
    private val SILENT_LISTENER = ANRListener { error: ANRError? -> Logger.eTag(TAG, error) }

    /**
     * ANR自定义处理【可以是记录日志用于上传】
     */
    private val CUSTOM_LISTENER = ANRListener { error: ANRError ->
        Logger.eTag(TAG, "Detected Application Not Responding!", error)
        throw error
    }

    fun init() {
        //这里设置监听的间隔为2秒
        mANRWatchDog = ANRWatchDog(2000)
        mANRWatchDog?.setANRInterceptor { duration: Long ->
            val ret = ANR_DURATION - duration
            if (ret > 0) {
                Logger.wTag(
                    TAG,
                    "Intercepted ANR that is too short ($duration ms), postponing for $ret ms."
                )
            }
            ret
        }?.setANRListener(SILENT_LISTENER)?.start()
    }
}