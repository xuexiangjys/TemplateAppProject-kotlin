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
package com.xuexiang.templateproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import kotlin.jvm.JvmOverloads
import android.widget.ProgressBar
import com.scwang.smartrefresh.layout.api.RefreshFooter
import android.widget.FrameLayout
import com.scwang.smartrefresh.layout.util.DensityUtil
import com.scwang.smartrefresh.layout.constant.SpinnerStyle
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.constant.RefreshState
import com.scwang.smartrefresh.layout.api.RefreshKernel

/**
 * Material风格的上拉加载
 *
 * @author xuexiang
 * @since 2019-08-03 11:14
 */
class MaterialFooter @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    ProgressBar(context, attrs), RefreshFooter {
    private fun initView() {
        visibility = GONE
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        setPadding(0, DensityUtil.dp2px(10f), 0, DensityUtil.dp2px(10f))
        layoutParams = params
    }

    override fun setNoMoreData(noMoreData: Boolean): Boolean {
        return false
    }

    override fun getView(): View {
        return this
    }

    override fun getSpinnerStyle(): SpinnerStyle {
        //指定为平移，不能null
        return SpinnerStyle.Translate
    }

    override fun onStartAnimator(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {
        visibility = VISIBLE
    }

    override fun onFinish(refreshLayout: RefreshLayout, success: Boolean): Int {
        visibility = GONE
        return 100
    }

    override fun onStateChanged(
        refreshLayout: RefreshLayout,
        oldState: RefreshState,
        newState: RefreshState
    ) {
    }

    override fun setPrimaryColors(vararg colors: Int) {}
    override fun onInitialized(kernel: RefreshKernel, height: Int, maxDragHeight: Int) {}
    override fun onMoving(
        isDragging: Boolean,
        percent: Float,
        offset: Int,
        height: Int,
        maxDragHeight: Int
    ) {
    }

    override fun onReleased(refreshLayout: RefreshLayout, height: Int, maxDragHeight: Int) {}
    override fun onHorizontalDrag(percentX: Float, offsetX: Int, offsetMax: Int) {}
    override fun isSupportHorizontalDrag(): Boolean {
        return false
    }

    init {
        initView()
    }
}