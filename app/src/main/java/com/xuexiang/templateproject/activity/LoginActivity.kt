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
package com.xuexiang.templateproject.activity

import android.os.Bundle
import android.view.KeyEvent
import androidx.viewbinding.ViewBinding
import com.xuexiang.templateproject.core.BaseActivity
import com.xuexiang.templateproject.fragment.other.LoginFragment
import com.xuexiang.xui.utils.KeyboardUtils
import com.xuexiang.xui.utils.StatusBarUtils
import com.xuexiang.xutil.display.Colors

/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:21
 */
class LoginActivity : BaseActivity<ViewBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openPage(LoginFragment::class.java, intent.extras)
    }

    override val isSupportSlideBack = false

    override fun initStatusBarStyle() {
        StatusBarUtils.initStatusBarStyle(this, false, Colors.WHITE)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event)
    }
}