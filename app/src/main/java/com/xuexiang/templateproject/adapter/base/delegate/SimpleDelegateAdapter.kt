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
package com.xuexiang.templateproject.adapter.base.delegate

import com.alibaba.android.vlayout.LayoutHelper

/**
 * 简易DelegateAdapter适配器
 *
 * @author xuexiang
 * @since 2020/3/20 12:55 AM
 */
abstract class SimpleDelegateAdapter<T> : BaseDelegateAdapter<T> {
    private var mLayoutId: Int
    private var mLayoutHelper: LayoutHelper

    constructor(layoutId: Int, layoutHelper: LayoutHelper) : super() {
        mLayoutId = layoutId
        mLayoutHelper = layoutHelper
    }

    constructor(layoutId: Int, layoutHelper: LayoutHelper, list: Collection<T>?) : super(list) {
        mLayoutId = layoutId
        mLayoutHelper = layoutHelper
    }

    constructor(layoutId: Int, layoutHelper: LayoutHelper, data: Array<T>?) : super(data) {
        mLayoutId = layoutId
        mLayoutHelper = layoutHelper
    }

    override fun getItemLayoutId(viewType: Int): Int {
        return mLayoutId
    }

    override fun onCreateLayoutHelper(): LayoutHelper {
        return mLayoutHelper
    }
}