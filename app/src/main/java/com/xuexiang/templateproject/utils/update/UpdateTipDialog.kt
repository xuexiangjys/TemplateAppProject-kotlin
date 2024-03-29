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

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.xuexiang.templateproject.utils.Utils.goWeb
import com.xuexiang.xui.widget.dialog.DialogLoader
import com.xuexiang.xupdate.XUpdate

/**
 * 版本更新提示弹窗
 *
 * @author xuexiang
 * @since 2019-06-15 00:06
 */
class UpdateTipDialog : AppCompatActivity(), DialogInterface.OnDismissListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var content = intent.getStringExtra(KEY_CONTENT)
        if (TextUtils.isEmpty(content)) {
            content = "应用下载速度太慢了，是否考虑切换" + DOWNLOAD_TYPE_NAME + "下载？"
        }
        DialogLoader.getInstance()
            .showConfirmDialog(this, content, "是", { dialog: DialogInterface, which: Int ->
                dialog.dismiss()
                goWeb(this@UpdateTipDialog, DOWNLOAD_URL)
            }, "否")
            .setOnDismissListener(this)
    }

    override fun onDismiss(dialog: DialogInterface) {
        finish()
    }

    companion object {
        const val KEY_CONTENT = "com.xuexiang.templateproject.utils.update.KEY_CONTENT"

        // TODO: 2021/5/11 填写你应用下载类型名
        const val DOWNLOAD_TYPE_NAME = "蒲公英"

        // TODO: 2021/5/11 填写你应用下载页面的链接
        private const val DOWNLOAD_URL = "这里填写你应用下载页面的链接"

        /**
         * 显示版本更新重试提示弹窗
         *
         * @param content
         */
        @JvmStatic
        fun show(content: String?) {
            val intent = Intent(XUpdate.getContext(), UpdateTipDialog::class.java)
            intent.putExtra(KEY_CONTENT, content)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            XUpdate.getContext().startActivity(intent)
        }
    }
}