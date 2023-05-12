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
package com.xuexiang.templateproject.core

import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.umeng.analytics.MobclickAgent
import com.xuexiang.xaop.annotation.SingleClick
import com.xuexiang.xpage.base.XPageContainerListFragment
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.actionbar.TitleUtils
import java.util.*

/**
 * 修改列表样式为主副标题显示
 *
 * @author xuexiang
 * @since 2018/11/22 上午11:26
 */
abstract class BaseContainerFragment : XPageContainerListFragment() {
    override fun initPage() {
        initTitle()
        initViews()
        initListeners()
    }

    private fun initTitle(): TitleBar {
        return TitleUtils.addTitleBarDynamic(
            toolbarContainer,
            pageTitle
        ) { popToBack() }
    }

    override fun initData() {
        mSimpleData = initSimpleData(mSimpleData)
        val data: MutableList<Map<String?, String?>?> = ArrayList()
        for (content in mSimpleData) {
            val item: MutableMap<String?, String?> = HashMap()
            val index = content.indexOf("\n")
            if (index > 0) {
                item[SimpleListAdapter.KEY_TITLE] = content.subSequence(0, index).toString()
                item[SimpleListAdapter.KEY_SUB_TITLE] =
                    content.subSequence(index + 1, content.length).toString()
            } else {
                item[SimpleListAdapter.KEY_TITLE] = content
                item[SimpleListAdapter.KEY_SUB_TITLE] = ""
            }
            data.add(item)
        }
        listView.adapter = SimpleListAdapter(context, data)
        initSimply()
    }

    override fun onItemClick(adapterView: AdapterView<*>?, view: View, position: Int, id: Long) {
        onItemClick(view, position)
    }

    @SingleClick
    private fun onItemClick(view: View, position: Int) {
        onItemClick(position)
    }

    override fun onDestroyView() {
        listView.onItemClickListener = null
        super.onDestroyView()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        //屏幕旋转时刷新一下title
        super.onConfigurationChanged(newConfig)
        val root = rootView as ViewGroup
        if (root.getChildAt(0) is TitleBar) {
            root.removeViewAt(0)
            initTitle()
        }
    }

    override fun onResume() {
        super.onResume()
        MobclickAgent.onPageStart(pageName)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPageEnd(pageName)
    }
}