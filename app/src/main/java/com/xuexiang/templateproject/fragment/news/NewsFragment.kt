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
package com.xuexiang.templateproject.fragment.news

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.RecycledViewPool
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.adapter.base.broccoli.BroccoliSimpleDelegateAdapter
import com.xuexiang.templateproject.adapter.base.delegate.SimpleDelegateAdapter
import com.xuexiang.templateproject.adapter.base.delegate.SingleDelegateAdapter
import com.xuexiang.templateproject.adapter.entity.NewInfo
import com.xuexiang.templateproject.core.BaseFragment
import com.xuexiang.templateproject.databinding.FragmentNewsBinding
import com.xuexiang.templateproject.utils.DemoDataProvider.bannerList
import com.xuexiang.templateproject.utils.DemoDataProvider.demoNewInfos
import com.xuexiang.templateproject.utils.DemoDataProvider.emptyNewInfo
import com.xuexiang.templateproject.utils.DemoDataProvider.getGridItems
import com.xuexiang.templateproject.utils.Utils.goWeb
import com.xuexiang.xpage.annotation.Page
import com.xuexiang.xpage.enums.CoreAnim
import com.xuexiang.xui.adapter.recyclerview.RecyclerViewHolder
import com.xuexiang.xui.adapter.simple.AdapterItem
import com.xuexiang.xui.utils.XToastUtils
import com.xuexiang.xui.widget.actionbar.TitleBar
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem
import com.xuexiang.xui.widget.banner.widget.banner.SimpleImageBanner
import com.xuexiang.xui.widget.imageview.ImageLoader
import com.xuexiang.xui.widget.imageview.RadiusImageView
import me.samlss.broccoli.Broccoli

/**
 * 首页动态
 *
 * @author xuexiang
 * @since 2019-10-30 00:15
 */
@Page(anim = CoreAnim.none)
class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    private var mNewsAdapter: SimpleDelegateAdapter<NewInfo>? = null

    override fun viewBindingInflate(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): FragmentNewsBinding {
        return FragmentNewsBinding.inflate(inflater, container, attachToRoot)
    }

    /**
     * @return 返回为 null意为不需要导航栏
     */
    override fun initTitle(): TitleBar? {
        return null
    }

    /**
     * 初始化控件
     */
    override fun initViews() {
        val virtualLayoutManager = VirtualLayoutManager(requireContext())
        binding?.recyclerView?.layoutManager = virtualLayoutManager
        val viewPool = RecycledViewPool()
        binding?.recyclerView?.setRecycledViewPool(viewPool)
        viewPool.setMaxRecycledViews(0, 10)

        //轮播条
        val bannerAdapter: SingleDelegateAdapter =
            object : SingleDelegateAdapter(R.layout.include_head_view_banner) {
                override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
                    val banner = holder.findViewById<SimpleImageBanner>(R.id.sib_simple_usage)
                    banner.setSource(bannerList)
                        .setOnItemClickListener { view: View?, item: BannerItem?, position1: Int ->
                            XToastUtils.toast(
                                "headBanner position--->$position1"
                            )
                        }.startScroll()
                }
            }

        //九宫格菜单
        val gridLayoutHelper = GridLayoutHelper(4)
        gridLayoutHelper.setPadding(0, 16, 0, 0)
        gridLayoutHelper.vGap = 10
        gridLayoutHelper.hGap = 0
        val commonAdapter: SimpleDelegateAdapter<AdapterItem> =
            object : SimpleDelegateAdapter<AdapterItem>(
                R.layout.adapter_common_grid_item, gridLayoutHelper, getGridItems(
                    requireContext()
                )
            ) {
                protected override fun bindData(
                    holder: RecyclerViewHolder,
                    position: Int,
                    item: AdapterItem
                ) {
                    if (item != null) {
                        val imageView = holder.findViewById<RadiusImageView>(R.id.riv_item)
                        imageView.isCircle = true
                        ImageLoader.get().loadImage(imageView, item.icon)
                        holder.text(R.id.tv_title, item.title.toString().substring(0, 1))
                        holder.text(R.id.tv_sub_title, item.title)
                        holder.click(R.id.ll_container) { v: View? ->
                            XToastUtils.toast("点击了：" + item.title)
                            // 注意: 这里由于NewsFragment是使用Viewpager加载的，并非使用XPage加载的，因此没有承载Activity， 需要使用openNewPage。
                            openNewPage(
                                GridItemFragment::class.java,
                                GridItemFragment.KEY_TITLE_NAME,
                                item.title
                            )
                        }
                    }
                }
            }

        //资讯的标题
        val titleAdapter: SingleDelegateAdapter =
            object : SingleDelegateAdapter(R.layout.adapter_title_item) {
                override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
                    holder.text(R.id.tv_title, "资讯")
                    holder.text(R.id.tv_action, "更多")
                    holder.click(R.id.tv_action) { v: View? -> XToastUtils.toast("更多") }
                }
            }

        //资讯
        mNewsAdapter = object : BroccoliSimpleDelegateAdapter<NewInfo>(
            R.layout.adapter_news_card_view_list_item,
            LinearLayoutHelper(),
            emptyNewInfo
        ) {
            protected override fun onBindData(
                holder: RecyclerViewHolder,
                model: NewInfo,
                position: Int
            ) {
                if (model != null) {
                    holder.text(R.id.tv_user_name, model.userName)
                    holder.text(R.id.tv_tag, model.tag)
                    holder.text(R.id.tv_title, model.title)
                    holder.text(R.id.tv_summary, model.summary)
                    holder.text(
                        R.id.tv_praise,
                        if (model.praise == 0) "点赞" else model.praise.toString()
                    )
                    holder.text(
                        R.id.tv_comment,
                        if (model.comment == 0) "评论" else model.comment.toString()
                    )
                    holder.text(R.id.tv_read, "阅读量 " + model.read)
                    holder.image(R.id.iv_image, model.imageUrl)
                    holder.click(R.id.card_view) {
                        goWeb(
                            context!!, model.detailUrl
                        )
                    }
                }
            }

            override fun onBindBroccoli(holder: RecyclerViewHolder, broccoli: Broccoli) {
                broccoli.addPlaceholders(
                    holder.findView(R.id.tv_user_name),
                    holder.findView(R.id.tv_tag),
                    holder.findView(R.id.tv_title),
                    holder.findView(R.id.tv_summary),
                    holder.findView(R.id.tv_praise),
                    holder.findView(R.id.tv_comment),
                    holder.findView(R.id.tv_read),
                    holder.findView(R.id.iv_image)
                )
            }
        }
        val delegateAdapter = DelegateAdapter(virtualLayoutManager)
        delegateAdapter.addAdapter(bannerAdapter)
        delegateAdapter.addAdapter(commonAdapter)
        delegateAdapter.addAdapter(titleAdapter)
        delegateAdapter.addAdapter(mNewsAdapter)
        binding?.recyclerView?.adapter = delegateAdapter
    }

    override fun initListeners() {
        //下拉刷新
        binding?.refreshLayout?.setOnRefreshListener { refreshLayout: RefreshLayout ->
            // TODO: 2020-02-25 这里只是模拟了网络请求
            refreshLayout.layout.postDelayed({
                mNewsAdapter?.refresh(demoNewInfos)
                refreshLayout.finishRefresh()
            }, 1000)
        }
        //上拉加载
        binding?.refreshLayout?.setOnLoadMoreListener { refreshLayout: RefreshLayout ->
            // TODO: 2020-02-25 这里只是模拟了网络请求
            refreshLayout.layout.postDelayed({
                mNewsAdapter?.loadMore(demoNewInfos)
                refreshLayout.finishLoadMore()
            }, 1000)
        }
        binding?.refreshLayout?.autoRefresh() //第一次进入触发自动刷新，演示效果
    }
}