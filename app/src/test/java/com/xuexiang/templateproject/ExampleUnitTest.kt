package com.xuexiang.templateproject

import com.xuexiang.templateproject.core.http.entity.TipInfo
import com.xuexiang.xhttp2.model.ApiResult
import com.xuexiang.xutil.net.JsonUtil
import org.junit.Assert
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, (2 + 2).toLong())
        val info = TipInfo()
        info.title = "微信公众号"
        info.content = "获取更多资讯，欢迎关注我的微信公众号：【我的Android开源之旅】"
        val list: MutableList<TipInfo> = ArrayList()
        for (i in 0..4) {
            list.add(info)
        }
        val result = ApiResult<List<TipInfo>>()
        result.data = list
        println(JsonUtil.toJson(result))
    }
}