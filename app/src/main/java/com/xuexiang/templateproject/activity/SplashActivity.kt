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

import android.view.KeyEvent
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.utils.SettingUtils.isAgreePrivacy
import com.xuexiang.templateproject.utils.TokenUtils.hasToken
import com.xuexiang.templateproject.utils.Utils.showPrivacyDialog
import com.xuexiang.xui.utils.KeyboardUtils
import com.xuexiang.xui.widget.activity.BaseSplashActivity
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xutil.app.ActivityUtils
import me.jessyan.autosize.internal.CancelAdapt

/**
 * 启动页【无需适配屏幕大小】
 *
 * @author xuexiang
 * @since 2019-06-30 17:32
 */
class SplashActivity : BaseSplashActivity(), CancelAdapt {
    override fun getSplashDurationMillis(): Long {
        return 500
    }

    /**
     * activity启动后的初始化
     */
    override fun onCreateActivity() {
        initSplashView(R.drawable.xui_config_bg_splash)
        startSplash(false)
    }

    /**
     * 启动页结束后的动作
     */
    override fun onSplashFinished() {
        if (isAgreePrivacy) {
            loginOrGoMainPage()
        } else {
            showPrivacyDialog(this) { dialog: MaterialDialog, which: DialogAction? ->
                dialog.dismiss()
                isAgreePrivacy = true
                loginOrGoMainPage()
            }
        }
    }

    private fun loginOrGoMainPage() {
        if (hasToken()) {
            ActivityUtils.startActivity(MainActivity::class.java)
        } else {
            ActivityUtils.startActivity(LoginActivity::class.java)
        }
        finish()
    }

    /**
     * 菜单、返回键响应
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return KeyboardUtils.onDisableBackKeyDown(keyCode) && super.onKeyDown(keyCode, event)
    }
}