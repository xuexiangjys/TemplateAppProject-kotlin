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
package com.xuexiang.templateproject.fragment.news

import android.view.LayoutInflater
import android.view.ViewGroup
import com.xuexiang.templateproject.core.BaseFragment
import com.xuexiang.templateproject.databinding.FragmentGridItemBinding
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xrouter.annotation.AutoWired
import com.xuexiang.xrouter.launcher.XRouter

/**
 * @author xuexiang
 * @since 2021/6/30 1:21 AM
 */
@Page
class GridItemFragment : BaseFragment<FragmentGridItemBinding?>() {
    /**
     * 自动注入参数，不能是private
     */
    @JvmField
    @AutoWired(name = KEY_TITLE_NAME)
    var title: String? = null

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentGridItemBinding {
        return FragmentGridItemBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initArgs() {
        // 自动注入参数必须在initArgs里进行注入
        XRouter.getInstance().inject(this)
    }

    override fun getPageTitle(): String? {
        return title
    }

    override fun initViews() {}

    companion object {
        const val KEY_TITLE_NAME = "title_name"
    }
}