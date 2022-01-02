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
package com.xuexiang.templateproject.core

import android.content.Context
import android.view.View
import android.widget.TextView
import com.xuexiang.templateproject.R
import com.xuexiang.xui.adapter.listview.BaseListAdapter
import com.xuexiang.xutil.common.StringUtils

/**
 * 主副标题显示适配器
 *
 * @author xuexiang
 * @since 2018/12/19 上午12:19
 */
class SimpleListAdapter(context: Context?, data: List<Map<String?, String?>?>?) :
    BaseListAdapter<Map<String?, String?>, SimpleListAdapter.ViewHolder>(context, data) {
    override fun newViewHolder(convertView: View): ViewHolder {
        val holder = ViewHolder()
        holder.mTvTitle = convertView.findViewById(R.id.tv_title)
        holder.mTvSubTitle = convertView.findViewById(R.id.tv_sub_title)
        return holder
    }

    override fun getLayoutId(): Int {
        return R.layout.adapter_item_simple_list_2
    }

    override fun convert(holder: ViewHolder, item: Map<String?, String?>, position: Int) {
        holder.mTvTitle!!.text =
            item[KEY_TITLE]
        if (!StringUtils.isEmpty(item[KEY_SUB_TITLE])) {
            holder.mTvSubTitle!!.text =
                item[KEY_SUB_TITLE]
            holder.mTvSubTitle!!.visibility = View.VISIBLE
        } else {
            holder.mTvSubTitle!!.visibility = View.GONE
        }
    }

    class ViewHolder {
        /**
         * 标题
         */
        var mTvTitle: TextView? = null

        /**
         * 副标题
         */
        var mTvSubTitle: TextView? = null
    }

    companion object {
        const val KEY_TITLE = "key_title"
        const val KEY_SUB_TITLE = "key_sub_title"
    }
}