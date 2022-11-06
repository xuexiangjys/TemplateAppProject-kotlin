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
package com.xuexiang.templateproject.core.webview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.xuexiang.templateproject.R
import com.xuexiang.xrouter.facade.Postcard
import com.xuexiang.xrouter.facade.callback.NavCallback
import com.xuexiang.xrouter.launcher.XRouter
import com.xuexiang.xui.utils.XToastUtils
import com.xuexiang.xui.widget.slideback.SlideBack

/**
 * 壳浏览器
 *
 * @author xuexiang
 * @since 2019/1/5 上午12:15
 */
class AgentWebActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agent_web)
        SlideBack.with(this)
            .haveScroll(true)
            .callBack { finish() }
            .register()
        val uri = intent.data
        if (uri != null) {
            XRouter.getInstance().build(uri).navigation(this, object : NavCallback() {
                override fun onArrival(postcard: Postcard) {
                    finish()
                }

                override fun onLost(postcard: Postcard) {
                    loadUrl(uri.toString())
                }
            })
        } else {
            val url = intent.getStringExtra(AgentWebFragment.KEY_URL)
            loadUrl(url)
        }
    }

    private fun loadUrl(url: String?) {
        if (url != null) {
            openFragment(url)
        } else {
            XToastUtils.error("数据出错！")
            finish()
        }
    }

    private var mAgentWebFragment: AgentWebFragment? = null
    private fun openFragment(url: String) {
        val ft = supportFragmentManager.beginTransaction()
        ft.add(
            R.id.container_frame_layout,
            AgentWebFragment.getInstance(url).also { mAgentWebFragment = it })
        ft.commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val agentWebFragment = mAgentWebFragment
        return if (agentWebFragment != null) {
            if ((agentWebFragment as FragmentKeyDown).onFragmentKeyDown(keyCode, event)) {
                true
            } else {
                super.onKeyDown(keyCode, event)
            }
        } else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        SlideBack.unregister(this)
        super.onDestroy()
    }

    companion object {
        /**
         * 请求浏览器
         *
         * @param url
         */
        fun goWeb(context: Context?, url: String?) {
            val intent = Intent(context, AgentWebActivity::class.java)
            intent.putExtra(AgentWebFragment.KEY_URL, url)
            context?.startActivity(intent)
        }
    }
}