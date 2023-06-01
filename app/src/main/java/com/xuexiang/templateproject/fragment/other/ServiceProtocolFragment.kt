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
import android.view.ViewGroup
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.core.BaseFragment
import com.xuexiang.templateproject.databinding.FragmentServiceProtocolBinding
import com.xuexiang.xaop.annotation.MemoryCache
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xrouter.annotation.AutoWired
import com.xuexiang.xrouter.launcher.XRouter
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xutil.resource.ResUtils
import com.xuexiang.xutil.resource.ResourceUtils

/**
 * 服务协议【本地加载】
 *
 * @author xuexiang
 * @since 2021/5/18 1:35 AM
 */
@Page
class ServiceProtocolFragment : BaseFragment<FragmentServiceProtocolBinding>() {
    @JvmField
    @AutoWired(name = KEY_PROTOCOL_TITLE)
    var title: String? = null

    @JvmField
    @AutoWired(name = KEY_IS_IMMERSIVE)
    var isImmersive = false
    override fun initArgs() {
        XRouter.getInstance().inject(this)
    }

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentServiceProtocolBinding {
        return FragmentServiceProtocolBinding.inflate(inflater, container, attachToRoot)
    }

    override fun initTitle(): TitleBar? {
        return super.initTitle()?.setTitle(title)?.setImmersive(isImmersive)
    }

    /**
     * 初始化控件
     */
    override fun initViews() {
        if (title == ResUtils.getString(R.string.title_user_protocol)) {
            binding?.tvProtocolText?.text = accountProtocol
        } else {
            binding?.tvProtocolText?.text = privacyProtocol
        }
    }

    @get:MemoryCache("account_protocol")
    private val accountProtocol: String
        get() = ResourceUtils.readStringFromAssert(ACCOUNT_PROTOCOL_ASSET_PATH)

    @get:MemoryCache("privacy_protocol")
    private val privacyProtocol: String
        get() = ResourceUtils.readStringFromAssert(PRIVACY_PROTOCOL_ASSET_PATH)

    companion object {
        const val KEY_PROTOCOL_TITLE = "key_protocol_title"
        const val KEY_IS_IMMERSIVE = "key_is_immersive"

        /**
         * 用户协议asset本地保存路径
         */
        private const val ACCOUNT_PROTOCOL_ASSET_PATH = "protocol/account_protocol.txt"

        /**
         * 隐私政策asset本地保存路径
         */
        private const val PRIVACY_PROTOCOL_ASSET_PATH = "protocol/privacy_protocol.txt"
    }
}