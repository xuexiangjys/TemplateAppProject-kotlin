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
import kotlin.jvm.JvmOverloads
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader
import com.xuexiang.xui.widget.dialog.MiniLoadingDialog
import com.xuexiang.xhttp2.subsciber.impl.OnProgressCancelListener
import android.content.DialogInterface

/**
 * 默认进度加载
 *
 * @author xuexiang
 * @since 2019-11-18 23:07
 */
class MiniLoadingDialogLoader @JvmOverloads constructor(
    context: Context?,
    msg: String? = "请求中..."
) : IProgressLoader {
    /**
     * 进度loading弹窗
     */
    private val mDialog: MiniLoadingDialog?

    /**
     * 进度框取消监听
     */
    private var mOnProgressCancelListener: OnProgressCancelListener? = null

    override fun isLoading(): Boolean {
        return mDialog != null && mDialog.isShowing
    }

    override fun updateMessage(msg: String) {
        mDialog?.updateMessage(msg)
    }

    override fun showLoading() {
        if (mDialog != null && !mDialog.isShowing) {
            mDialog.show()
        }
    }

    override fun dismissLoading() {
        if (mDialog != null && mDialog.isShowing) {
            mDialog.dismiss()
        }
    }

    override fun setCancelable(flag: Boolean) {
        mDialog?.setCancelable(flag)
        if (flag) {
            mDialog?.setOnCancelListener {
                mOnProgressCancelListener?.onCancelProgress()
            }
        }
    }

    override fun setOnProgressCancelListener(listener: OnProgressCancelListener) {
        mOnProgressCancelListener = listener
    }

    init {
        mDialog = MiniLoadingDialog(context, msg)
    }
}