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

import com.xuexiang.templateproject.utils.update.UpdateTipDialog
import com.xuexiang.xupdate.entity.UpdateEntity
import com.xuexiang.xupdate.proxy.impl.DefaultUpdateDownloader
import com.xuexiang.xupdate.service.OnFileDownloadListener
import com.xuexiang.xutil.app.ActivityUtils

/**
 * 重写DefaultUpdateDownloader，在取消下载时，弹出提示
 *
 * @author xuexiang
 * @since 2019-06-14 23:47
 */
class CustomUpdateDownloader : DefaultUpdateDownloader() {
    private var mIsStartDownload = false
    override fun startDownload(
        updateEntity: UpdateEntity,
        downloadListener: OnFileDownloadListener?
    ) {
        super.startDownload(updateEntity, downloadListener)
        mIsStartDownload = true
    }

    override fun cancelDownload() {
        super.cancelDownload()
        if (mIsStartDownload) {
            mIsStartDownload = false
            ActivityUtils.startActivity(UpdateTipDialog::class.java)
        }
    }
}