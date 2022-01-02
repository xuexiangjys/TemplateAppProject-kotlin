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

import com.xuexiang.templateproject.utils.XToastUtils
import com.xuexiang.xhttp2.XHttp
import com.xuexiang.xhttp2.XHttpSDK
import com.xuexiang.xhttp2.callback.DownloadProgressCallBack
import com.xuexiang.xhttp2.callback.SimpleCallBack
import com.xuexiang.xhttp2.exception.ApiException
import com.xuexiang.xupdate.proxy.IUpdateHttpService
import com.xuexiang.xupdate.proxy.IUpdateHttpService.DownloadCallback
import com.xuexiang.xutil.file.FileUtils
import com.xuexiang.xutil.net.JsonUtil

/**
 * XHttp2实现的请求更新
 *
 * @author xuexiang
 * @since 2018/8/12 上午11:46
 */
class XHttpUpdateHttpServiceImpl : IUpdateHttpService {
    override fun asyncGet(
        url: String,
        params: Map<String, Any>,
        callBack: IUpdateHttpService.Callback
    ) {
        XHttp.get(url)
            .params(params)
            .keepJson(true)
            .execute(object : SimpleCallBack<String>() {
                @Throws(Throwable::class)
                override fun onSuccess(response: String) {
                    callBack.onSuccess(response)
                }

                override fun onError(e: ApiException) {
                    callBack.onError(e)
                }
            })
    }

    override fun asyncPost(
        url: String,
        params: Map<String, Any>,
        callBack: IUpdateHttpService.Callback
    ) {
        XHttp.post(url)
            .upJson(JsonUtil.toJson(params))
            .keepJson(true)
            .execute(object : SimpleCallBack<String>() {
                @Throws(Throwable::class)
                override fun onSuccess(response: String) {
                    callBack.onSuccess(response)
                }

                override fun onError(e: ApiException) {
                    callBack.onError(e)
                }
            })
    }

    override fun download(url: String, path: String, fileName: String, callback: DownloadCallback) {
        XHttpSDK.addRequest(url, XHttp.downLoad(url)
            .savePath(path)
            .saveName(fileName)
            .isUseBaseUrl(false)
            .execute(object : DownloadProgressCallBack<String?>() {
                override fun onStart() {
                    callback.onStart()
                }

                override fun onError(e: ApiException) {
                    callback.onError(e)
                }

                override fun update(downLoadSize: Long, totalSize: Long, done: Boolean) {
                    callback.onProgress(downLoadSize / totalSize.toFloat(), totalSize)
                }

                override fun onComplete(path: String) {
                    callback.onSuccess(FileUtils.getFileByPath(path))
                }
            })
        )
    }

    override fun cancelDownload(url: String) {
        XToastUtils.info("已取消更新")
        XHttpSDK.cancelRequest(url)
    }
}