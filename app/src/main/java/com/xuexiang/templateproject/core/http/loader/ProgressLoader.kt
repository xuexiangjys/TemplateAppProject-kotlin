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
package com.xuexiang.templateproject.core.http.loader

import android.content.Context
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader

/**
 * 创建进度加载者
 *
 * @author xuexiang
 * @since 2019-07-02 12:51
 */
object ProgressLoader {

    private var sIProgressLoaderFactory: IProgressLoaderFactory = MiniProgressLoaderFactory()

    fun setIProgressLoaderFactory(sIProgressLoaderFactory: IProgressLoaderFactory) {
        ProgressLoader.sIProgressLoaderFactory = sIProgressLoaderFactory
    }

    /**
     * 创建进度加载者
     *
     * @param context
     * @return
     */
    fun create(context: Context?): IProgressLoader? {
        return sIProgressLoaderFactory.create(context)
    }

    /**
     * 创建进度加载者
     *
     * @param context
     * @param message 默认提示信息
     * @return
     */
    fun create(context: Context?, message: String?): IProgressLoader? {
        return sIProgressLoaderFactory.create(context, message)
    }
}