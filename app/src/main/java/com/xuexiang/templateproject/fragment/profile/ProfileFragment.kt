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
package com.xuexiang.templateproject.fragment.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.core.BaseFragment
import com.xuexiang.templateproject.databinding.FragmentProfileBinding
import com.xuexiang.templateproject.fragment.other.AboutFragment
import com.xuexiang.templateproject.fragment.other.SettingsFragment
import com.xuexiang.xaop.annotation.SingleClick
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xpage.enums.CoreAnim
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView
import com.xuexiang.xui.widget.textview.supertextview.SuperTextView.OnSuperTextViewClickListener

/**
 * @author xuexiang
 * @since 2019-10-30 00:18
 */
@Page(anim = CoreAnim.none)
class ProfileFragment : BaseFragment<FragmentProfileBinding?>(), OnSuperTextViewClickListener {

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    /**
     * @return 返回为 null意为不需要导航栏
     */
    override fun initTitle(): TitleBar? {
        return null
    }

    /**
     * 初始化控件
     */
    override fun initViews() {}
    override fun initListeners() {
        binding!!.menuSettings.setOnSuperTextViewClickListener(this)
        binding!!.menuAbout.setOnSuperTextViewClickListener(this)
    }

    @SingleClick
    override fun onClick(view: SuperTextView) {
        val id = view.id
        if (id == R.id.menu_settings) {
            openNewPage(SettingsFragment::class.java)
        } else if (id == R.id.menu_about) {
            openNewPage(AboutFragment::class.java)
        }
    }
}