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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.core.BaseFragment
import com.xuexiang.templateproject.core.webview.AgentWebActivity
import com.xuexiang.templateproject.databinding.FragmentAboutBinding
import com.xuexiang.templateproject.utils.Utils.Companion.gotoProtocol
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xui.widget.grouplist.XUIGroupListView
import com.xuexiang.xutil.app.AppUtils
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author xuexiang
 * @since 2019-10-30 00:02
 */
@Page(name = "关于")
class AboutFragment : BaseFragment<FragmentAboutBinding?>() {

    override fun initViews() {
        binding?.tvVersion?.text = String.format("版本号：%s", AppUtils.getAppVersionName())
        XUIGroupListView.newSection(context)
            .addItemView(binding?.aboutList?.createItemView(resources.getString(R.string.about_item_homepage))) {
                AgentWebActivity.goWeb(
                    context, getString(R.string.url_project_github)
                )
            }
            .addItemView(binding?.aboutList?.createItemView(resources.getString(R.string.about_item_author_github))) {
                AgentWebActivity.goWeb(
                    context, getString(R.string.url_author_github)
                )
            }
            .addItemView(binding?.aboutList?.createItemView(resources.getString(R.string.about_item_donation_link))) {
                AgentWebActivity.goWeb(
                    context, getString(R.string.url_donation_link)
                )
            }
            .addItemView(binding?.aboutList?.createItemView(resources.getString(R.string.about_item_add_qq_group))) {
                AgentWebActivity.goWeb(
                    context, getString(R.string.url_add_qq_group)
                )
            }
            .addItemView(binding?.aboutList?.createItemView(resources.getString(R.string.title_user_protocol))) {
                gotoProtocol(
                    this,
                    isPrivacy = false,
                    isImmersive = false
                )
            }
            .addItemView(binding!!.aboutList.createItemView(resources.getString(R.string.title_privacy_protocol))) {
                gotoProtocol(
                    this,
                    true,
                    false
                )
            }
            .addTo(binding!!.aboutList)
        val dateFormat = SimpleDateFormat("yyyy", Locale.CHINA)
        val currentYear = dateFormat.format(Date())
        binding!!.tvCopyright.text =
            String.format(resources.getString(R.string.about_copyright), currentYear)
    }

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup
    ): FragmentAboutBinding {
        return FragmentAboutBinding.inflate(inflater, container, false)
    }
}