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
package com.xuexiang.templateproject.utils

import android.content.Context
import com.umeng.analytics.MobclickAgent
import com.xuexiang.templateproject.activity.LoginActivity
import com.xuexiang.xui.utils.XToastUtils
import com.xuexiang.xutil.app.ActivityUtils
import com.xuexiang.xutil.common.StringUtils

/**
 * Token管理工具
 *
 * @author xuexiang
 * @since 2019-11-17 22:37
 */
class TokenUtils private constructor() {
    companion object {
        private var sToken: String? = null
        private const val KEY_TOKEN = "com.xuexiang.templateproject.utils.KEY_TOKEN"
        private const val KEY_PROFILE_CHANNEL = "github"

        /**
         * 初始化Token信息
         */
        @JvmStatic
        fun init(context: Context) {
            MMKVUtils.init(context)
            sToken = MMKVUtils.getString(KEY_TOKEN, "")
        }

        fun clearToken() {
            sToken = null
            MMKVUtils.remove(KEY_TOKEN)
        }

        var token: String?
            get() = sToken
            set(token) {
                sToken = token
                MMKVUtils.put(KEY_TOKEN, token)
            }

        @JvmStatic
        fun hasToken(): Boolean {
            return MMKVUtils.containsKey(KEY_TOKEN)
        }

        /**
         * 处理登录成功的事件
         *
         * @param token 账户信息
         */
        @JvmStatic
        fun handleLoginSuccess(token: String?): Boolean {
            return if (!StringUtils.isEmpty(token)) {
                XToastUtils.success("登录成功！")
                MobclickAgent.onProfileSignIn(KEY_PROFILE_CHANNEL, token)
                Companion.token = token
                true
            } else {
                XToastUtils.error("登录失败！")
                false
            }
        }

        /**
         * 处理登出的事件
         */
        @JvmStatic
        fun handleLogoutSuccess() {
            MobclickAgent.onProfileSignOff()
            //登出时，清除账号信息
            clearToken()
            XToastUtils.success("登出成功！")
            SettingUtils.isAgreePrivacy = false
            //跳转到登录页
            ActivityUtils.startActivity(LoginActivity::class.java)
        }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}