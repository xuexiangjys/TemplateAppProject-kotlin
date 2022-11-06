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

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.ColorInt
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.core.webview.AgentWebActivity
import com.xuexiang.templateproject.core.webview.AgentWebFragment
import com.xuexiang.templateproject.fragment.other.ServiceProtocolFragment
import com.xuexiang.xpage.base.XPageFragment
import com.xuexiang.xpage.core.PageOption
import com.xuexiang.xui.utils.ColorUtils
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.widget.dialog.DialogLoader
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog.SingleButtonCallback
import com.xuexiang.xutil.XUtil

/**
 * 工具类
 *
 * @author xuexiang
 * @since 2020-02-23 15:12
 */
object Utils {
    /**
     * 这里填写你的应用隐私政策网页地址
     */
    private const val PRIVACY_URL =
        "https://gitee.com/xuexiangjys/TemplateAppProject/raw/master/LICENSE"

    /**
     * 显示隐私政策的提示
     *
     * @param context
     * @param submitListener 同意的监听
     * @return
     */
    @JvmStatic
    fun showPrivacyDialog(context: Context, submitListener: SingleButtonCallback?): Dialog {
        val dialog =
            MaterialDialog.Builder(context).title(R.string.title_reminder).autoDismiss(false)
                .cancelable(false)
                .positiveText(R.string.lab_agree)
                .onPositive { dialog1: MaterialDialog, which: DialogAction? ->
                    if (submitListener != null) {
                        submitListener.onClick(dialog1, which!!)
                    } else {
                        dialog1.dismiss()
                    }
                }
                .negativeText(R.string.lab_disagree).onNegative { dialog, which ->
                    dialog.dismiss()
                    DialogLoader.getInstance().showConfirmDialog(
                        context,
                        ResUtils.getString(R.string.title_reminder),
                        String.format(
                            ResUtils.getString(R.string.content_privacy_explain_again),
                            ResUtils.getString(R.string.app_name)
                        ),
                        ResUtils.getString(R.string.lab_look_again),
                        { dialog, _ ->
                            dialog.dismiss()
                            showPrivacyDialog(context, submitListener)
                        },
                        ResUtils.getString(R.string.lab_still_disagree)
                    ) { dialog, _ ->
                        dialog.dismiss()
                        DialogLoader.getInstance().showConfirmDialog(
                            context,
                            ResUtils.getString(R.string.content_think_about_it_again),
                            ResUtils.getString(R.string.lab_look_again),
                            { dialog, _ ->
                                dialog.dismiss()
                                showPrivacyDialog(context, submitListener)
                            },
                            ResUtils.getString(R.string.lab_exit_app)
                        ) { dialog, _ ->
                            dialog.dismiss()
                            XUtil.exitApp()
                        }
                    }
                }.build()
        dialog.setContent(getPrivacyContent(context))
        //开始响应点击事件
        dialog.contentView?.movementMethod = LinkMovementMethod.getInstance()
        dialog.show()
        return dialog
    }

    /**
     * @return 隐私政策说明
     */
    private fun getPrivacyContent(context: Context): SpannableStringBuilder {
        val stringBuilder = SpannableStringBuilder()
            .append("    欢迎来到").append(ResUtils.getString(R.string.app_name)).append("!\n")
            .append("    我们深知个人信息对你的重要性，也感谢你对我们的信任。\n")
            .append("    为了更好地保护你的权益，同时遵守相关监管的要求，我们将通过")
        stringBuilder.append(getPrivacyLink(context, PRIVACY_URL))
            .append("向你说明我们会如何收集、存储、保护、使用及对外提供你的信息，并说明你享有的权利。\n")
            .append("    更多详情，敬请查阅")
            .append(getPrivacyLink(context, PRIVACY_URL))
            .append("全文。")
        return stringBuilder
    }

    /**
     * @param context 隐私政策的链接
     * @return
     */
    private fun getPrivacyLink(context: Context, privacyUrl: String): SpannableString {
        val privacyName = String.format(
            ResUtils.getString(R.string.lab_privacy_name),
            ResUtils.getString(R.string.app_name)
        )
        val spannableString = SpannableString(privacyName)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                goWeb(context, privacyUrl)
            }
        }, 0, privacyName.length, Spanned.SPAN_MARK_MARK)
        return spannableString
    }

    /**
     * 请求浏览器
     *
     * @param url
     */
    @JvmStatic
    fun goWeb(context: Context, url: String?) {
        val intent = Intent(context, AgentWebActivity::class.java)
        intent.putExtra(AgentWebFragment.KEY_URL, url)
        context.startActivity(intent)
    }

    /**
     * 打开用户协议和隐私协议
     *
     * @param fragment
     * @param isPrivacy   是否是隐私协议
     * @param isImmersive 是否沉浸式
     */
    @JvmStatic
    fun gotoProtocol(fragment: XPageFragment?, isPrivacy: Boolean, isImmersive: Boolean) {
        PageOption.to(ServiceProtocolFragment::class.java)
            .putString(
                ServiceProtocolFragment.KEY_PROTOCOL_TITLE,
                if (isPrivacy) ResUtils.getString(R.string.title_privacy_protocol) else ResUtils.getString(
                    R.string.title_user_protocol
                )
            )
            .putBoolean(ServiceProtocolFragment.KEY_IS_IMMERSIVE, isImmersive)
            .open(fragment!!)
    }

    /**
     * 是否是深色的颜色
     *
     * @param color
     * @return
     */
    @JvmStatic
    fun isColorDark(@ColorInt color: Int): Boolean {
        return ColorUtils.isColorDark(color, 0.382)
    }
}