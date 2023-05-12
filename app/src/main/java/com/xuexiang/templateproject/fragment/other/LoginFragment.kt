/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
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
package com.xuexiang.templateproject.fragment.other

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.activity.MainActivity
import com.xuexiang.templateproject.core.BaseFragment
import com.xuexiang.templateproject.databinding.FragmentLoginBinding
import com.xuexiang.templateproject.utils.RandomUtils.getRandomNumbersAndLetters
import com.xuexiang.templateproject.utils.SettingUtils
import com.xuexiang.templateproject.utils.SettingUtils.isAgreePrivacy
import com.xuexiang.templateproject.utils.TokenUtils.handleLoginSuccess
import com.xuexiang.templateproject.utils.Utils.gotoProtocol
import com.xuexiang.templateproject.utils.Utils.showPrivacyDialog
import com.xuexiang.templateproject.utils.sdkinit.UMengInit
import com.xuexiang.xaop.annotation.SingleClick
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xpage.enums.CoreAnim
import com.xuexiang.xui.utils.*
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.dialog.materialdialog.DialogAction
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog
import com.xuexiang.xutil.app.ActivityUtils

/**
 * 登录页面
 *
 * @author xuexiang
 * @since 2019-11-17 22:15
 */
@Page(anim = CoreAnim.none)
class LoginFragment : BaseFragment<FragmentLoginBinding?>(), View.OnClickListener {

    private var mJumpView: View? = null
    private var mCountDownHelper: CountDownButtonHelper? = null
    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initTitle(): TitleBar? {
        val titleBar = super.initTitle();
        titleBar?.run {
            setImmersive(true)
            setBackgroundColor(Color.TRANSPARENT)
            setTitle("")
            setLeftImageDrawable(ResUtils.getVectorDrawable(context, R.drawable.ic_login_close))
            setActionTextColor(ThemeUtils.resolveColor(context, R.attr.colorAccent))
            mJumpView = addAction(object : TitleBar.TextAction(R.string.title_jump_login) {
                override fun performAction(view: View) {
                    onLoginSuccess()
                }
            })
        }
        return titleBar
    }

    override fun initViews() {
        mCountDownHelper = CountDownButtonHelper(binding?.btnGetVerifyCode, 60)
        //隐私政策弹窗
        if (!isAgreePrivacy) {
            showPrivacyDialog(requireContext()) { dialog: MaterialDialog, _: DialogAction? ->
                dialog.dismiss()
                handleSubmitPrivacy()
            }
        }
        val isAgreePrivacy = isAgreePrivacy
        binding?.cbProtocol?.isChecked = isAgreePrivacy
        refreshButton(isAgreePrivacy)
        binding?.cbProtocol?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            SettingUtils.isAgreePrivacy = isChecked
            refreshButton(isChecked)
        }
    }

    override fun initListeners() {
        binding?.btnGetVerifyCode?.setOnClickListener(this)
        binding?.btnLogin?.setOnClickListener(this)
        binding?.tvOtherLogin?.setOnClickListener(this)
        binding?.tvForgetPassword?.setOnClickListener(this)
        binding?.tvUserProtocol?.setOnClickListener(this)
        binding?.tvPrivacyProtocol?.setOnClickListener(this)
    }

    private fun refreshButton(isChecked: Boolean) {
        ViewUtils.setEnabled(binding?.btnLogin, isChecked)
        ViewUtils.setEnabled(mJumpView, isChecked)
    }

    private fun handleSubmitPrivacy() {
        isAgreePrivacy = true
        UMengInit.init()
        // 应用市场不让默认勾选
//        ViewUtils.setChecked(cbProtocol, true);
    }

    @SingleClick
    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.btn_get_verify_code) {
            if (binding?.etPhoneNumber!!.validate()) {
                getVerifyCode(binding?.etPhoneNumber!!.editValue)
            }
        } else if (id == R.id.btn_login) {
            if (binding?.etPhoneNumber!!.validate()) {
                if (binding?.etVerifyCode!!.validate()) {
                    loginByVerifyCode(
                        binding?.etPhoneNumber!!.editValue,
                        binding?.etVerifyCode!!.editValue
                    )
                }
            }
        } else if (id == R.id.tv_other_login) {
            XToastUtils.info("其他登录方式")
        } else if (id == R.id.tv_forget_password) {
            XToastUtils.info("忘记密码")
        } else if (id == R.id.tv_user_protocol) {
            gotoProtocol(this, isPrivacy = false, isImmersive = true)
        } else if (id == R.id.tv_privacy_protocol) {
            gotoProtocol(this, isPrivacy = true, isImmersive = true)
        }
    }

    /**
     * 获取验证码
     */
    private fun getVerifyCode(phoneNumber: String) {
        // TODO: 2020/8/29 这里只是界面演示而已
        XToastUtils.warning("只是演示，验证码请随便输")
        mCountDownHelper?.start()
    }

    /**
     * 根据验证码登录
     *
     * @param phoneNumber 手机号
     * @param verifyCode  验证码
     */
    private fun loginByVerifyCode(phoneNumber: String, verifyCode: String) {
        // TODO: 2020/8/29 这里只是界面演示而已
        onLoginSuccess()
    }

    /**
     * 登录成功的处理
     */
    private fun onLoginSuccess() {
        val token = getRandomNumbersAndLetters(16)
        if (handleLoginSuccess(token)) {
            popToBack()
            ActivityUtils.startActivity(MainActivity::class.java)
        }
    }

    override fun onDestroyView() {
        mCountDownHelper?.recycle()
        super.onDestroyView()
    }
}