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
package com.xuexiang.templateproject.utils.update

import com.xuexiang.templateproject.utils.update.UpdateTipDialog.Companion.show
import com.xuexiang.xui.utils.XToastUtils
import kotlin.jvm.JvmOverloads
import com.xuexiang.xupdate.listener.OnUpdateFailureListener
import com.xuexiang.xupdate.entity.UpdateError

/**
 * 自定义版本更新提示
 *
 * @author xuexiang
 * @since 2019/4/15 上午12:01
 */
class CustomUpdateFailureListener @JvmOverloads constructor(
    /**
     * 是否需要错误提示
     */
    private val mNeedErrorTip: Boolean = true
) : OnUpdateFailureListener {
    /**
     * 更新失败
     *
     * @param error 错误
     */
    override fun onFailure(error: UpdateError) {
        if (mNeedErrorTip) {
            XToastUtils.error(error.detailMsg)
        }
        if (error.code == UpdateError.ERROR.DOWNLOAD_FAILED) {
            show("应用下载失败，是否考虑切换" + UpdateTipDialog.DOWNLOAD_TYPE_NAME + "下载？")
        }
    }
}