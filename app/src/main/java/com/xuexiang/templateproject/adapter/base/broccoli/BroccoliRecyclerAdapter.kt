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
package com.xuexiang.templateproject.adapter.base.broccoli

import android.view.View
import com.xuexiang.xui.adapter.recyclerview.BaseRecyclerAdapter
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import com.xuexiang.xui.adapter.recyclerview.XRecyclerAdapter
import me.samlss.broccoli.Broccoli
import java.util.*

/**
 * 使用Broccoli占位的基础适配器
 *
 * @author XUE
 * @since 2019/4/8 16:33
 */
abstract class BroccoliRecyclerAdapter<T>(collection: Collection<T>?) :
    BaseRecyclerAdapter<T>(collection) {
    /**
     * 是否已经加载成功
     */
    private var mHasLoad = false
    private val mBroccoliMap: MutableMap<View, Broccoli> = HashMap()
    override fun bindData(holder: RecyclerViewHolder, position: Int, item: T) {
        var broccoli = mBroccoliMap[holder.itemView]
        if (broccoli == null) {
            broccoli = Broccoli()
            mBroccoliMap[holder.itemView] = broccoli
        }
        if (mHasLoad) {
            broccoli.removeAllPlaceholders()
            onBindData(holder, item, position)
        } else {
            onBindBroccoli(holder, broccoli)
            broccoli.show()
        }
    }

    /**
     * 绑定控件
     *
     * @param holder
     * @param model
     * @param position
     */
    protected abstract fun onBindData(holder: RecyclerViewHolder?, model: T, position: Int)

    /**
     * 绑定占位控件
     *
     * @param broccoli
     */
    protected abstract fun onBindBroccoli(holder: RecyclerViewHolder?, broccoli: Broccoli?)
    override fun refresh(collection: Collection<T>): XRecyclerAdapter<*, *> {
        mHasLoad = true
        return super.refresh(collection)
    }

    /**
     * 资源释放，防止内存泄漏
     */
    fun recycle() {
        for (broccoli in mBroccoliMap.values) {
            broccoli.removeAllPlaceholders()
        }
        mBroccoliMap.clear()
        clear()
    }
}