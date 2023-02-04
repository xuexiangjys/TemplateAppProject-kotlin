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
package com.xuexiang.templateproject.core.http.callback

import com.xuexiang.templateproject.core.BaseFragment
import com.xuexiang.xhttp2.callback.ProgressLoadingCallBack
import com.xuexiang.xhttp2.exception.ApiException
import com.xuexiang.xhttp2.model.XHttpRequest
import com.xuexiang.xhttp2.subsciber.impl.IProgressLoader
import com.xuexiang.xui.utils.XToastUtils
import com.xuexiang.xutil.common.StringUtils
import com.xuexiang.xutil.common.logger.Logger

/**
 * 带错误toast提示和加载进度条的网络请求回调
 *
 * @author xuexiang
 * @since 2019-11-18 23:16
 */
abstract class TipProgressLoadingCallBack<T> : ProgressLoadingCallBack<T> {
    /**
     * 记录一下请求的url,确定出错的请求是哪个请求
     */
    private var mUrl: String? = null

    constructor(fragment: BaseFragment<*>) : super(fragment.getProgressLoader()) {}
    constructor(iProgressLoader: IProgressLoader?) : super(iProgressLoader) {}
    constructor(req: XHttpRequest, iProgressLoader: IProgressLoader?) : this(
        req.url,
        iProgressLoader
    ) {
    }

    constructor(url: String?, iProgressLoader: IProgressLoader?) : super(iProgressLoader) {
        mUrl = url
    }

    override fun onError(e: ApiException) {
        super.onError(e)
        XToastUtils.error(e)
        if (!StringUtils.isEmpty(mUrl)) {
            Logger.e("网络请求的url:$mUrl", e)
        } else {
            Logger.e(e)
        }
    }
}