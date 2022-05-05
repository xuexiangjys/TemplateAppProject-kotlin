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
package com.xuexiang.templateproject.activity

import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.xuexiang.templateproject.R
import com.xuexiang.templateproject.core.BaseActivity
import com.xuexiang.templateproject.databinding.ActivityMainBinding
import com.xuexiang.templateproject.fragment.news.NewsFragment
import com.xuexiang.templateproject.fragment.other.AboutFragment
import com.xuexiang.templateproject.fragment.other.SettingsFragment
import com.xuexiang.templateproject.fragment.profile.ProfileFragment
import com.xuexiang.templateproject.fragment.trending.TrendingFragment
import com.xuexiang.templateproject.utils.Utils.Companion.isColorDark
import com.xuexiang.templateproject.utils.XToastUtils
import com.xuexiang.templateproject.utils.sdkinit.XUpdateInit
import com.xuexiang.templateproject.widget.GuideTipsDialog.Companion.showTips
import com.xuexiang.templateproject.widget.GuideTipsDialog.Companion.showTipsForce
import com.xuexiang.xaop.annotation.SingleClick
import com.xuexiang.xui.adapter.FragmentAdapter
import com.xuexiang.xui.utils.ResUtils
import com.xuexiang.xui.utils.ThemeUtils
import com.xuexiang.xui.utils.WidgetUtils
import com.xuexiang.xui.widget.imageview.RadiusImageView
import com.xuexiang.xutil.XUtil
import com.xuexiang.xutil.common.ClickUtils
import com.xuexiang.xutil.common.ClickUtils.OnClick2ExitListener
import com.xuexiang.xutil.common.CollectionUtils
import com.xuexiang.xutil.display.Colors

/**
 * 程序主页面,只是一个简单的Tab例子
 *
 * @author xuexiang
 * @since 2019-07-07 23:53
 */
class MainActivity : BaseActivity<ActivityMainBinding?>(), View.OnClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener, OnClick2ExitListener,
    Toolbar.OnMenuItemClickListener {
    private lateinit var mTitles: Array<String>
    override fun viewBindingInflate(inflater: LayoutInflater?): ActivityMainBinding? {
        return ActivityMainBinding.inflate(inflater!!)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        initData()
        initListeners()
    }

    override val isSupportSlideBack = false

    private fun initViews() {
        WidgetUtils.clearActivityBackground(this)
        mTitles = ResUtils.getStringArray(R.array.home_titles)
        binding?.includeMain?.toolbar?.title = mTitles[0]
        binding?.includeMain?.toolbar?.inflateMenu(R.menu.menu_main)
        binding?.includeMain?.toolbar?.setOnMenuItemClickListener(this)
        initHeader()

        //主页内容填充
        val fragments = arrayOf(
            NewsFragment(),
            TrendingFragment(),
            ProfileFragment()
        )
        val adapter = FragmentAdapter(supportFragmentManager, fragments)
        binding?.includeMain?.viewPager?.offscreenPageLimit = mTitles.size - 1
        binding?.includeMain?.viewPager?.adapter = adapter
    }

    private fun initData() {
        showTips(this)
        XUpdateInit.checkUpdate(this, false)
    }

    private fun initHeader() {
        binding?.navView?.itemIconTintList = null
        val headerView = binding?.navView?.getHeaderView(0)
        val navHeader = headerView?.findViewById<LinearLayout>(R.id.nav_header)
        val ivAvatar: RadiusImageView? = headerView?.findViewById(R.id.iv_avatar)
        val tvAvatar = headerView?.findViewById<TextView>(R.id.tv_avatar)
        val tvSign = headerView?.findViewById<TextView>(R.id.tv_sign)
        if (isColorDark(ThemeUtils.resolveColor(this, R.attr.colorAccent))) {
            tvAvatar?.setTextColor(Colors.WHITE)
            tvSign?.setTextColor(Colors.WHITE)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar?.imageTintList =
                    ResUtils.getColors(R.color.xui_config_color_white)
            }
        } else {
            tvAvatar?.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_title_text))
            tvSign?.setTextColor(ThemeUtils.resolveColor(this, R.attr.xui_config_color_explain_text))
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ivAvatar?.imageTintList = ResUtils.getColors(R.color.xui_config_color_gray_3)
            }
        }

        // TODO: 2019-10-09 初始化数据
        ivAvatar?.setImageResource(R.drawable.ic_default_head)
        tvAvatar?.setText(R.string.app_name)
        tvSign?.text = "这个家伙很懒，什么也没有留下～～"
        navHeader?.setOnClickListener(this)
    }

    private fun initListeners() {
        val toggle = ActionBarDrawerToggle(
            this,
            binding?.drawerLayout,
            binding?.includeMain?.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        binding?.drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()

        //侧边栏点击事件
        binding?.navView?.setNavigationItemSelectedListener { menuItem: MenuItem ->
            if (menuItem.isCheckable) {
                binding?.drawerLayout?.closeDrawers()
                return@setNavigationItemSelectedListener handleNavigationItemSelected(menuItem)
            } else {
                when (menuItem.itemId) {
                    R.id.nav_settings -> {
                        openNewPage(SettingsFragment::class.java)
                    }
                    R.id.nav_about -> {
                        openNewPage(AboutFragment::class.java)
                    }
                    else -> {
                        XToastUtils.toast("点击了:" + menuItem.title)
                    }
                }
            }
            true
        }
        //主页事件监听
        binding?.includeMain?.viewPager?.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                val item = binding?.includeMain?.bottomNavigation?.menu?.getItem(position)
                binding?.includeMain?.toolbar?.title = item?.title
                item?.isChecked = true
                updateSideNavStatus(item)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
        binding?.includeMain?.bottomNavigation?.setOnNavigationItemSelectedListener(this)
    }

    /**
     * 处理侧边栏点击事件
     *
     * @param menuItem
     * @return
     */
    private fun handleNavigationItemSelected(menuItem: MenuItem): Boolean {
        val index = CollectionUtils.arrayIndexOf(mTitles, menuItem.title)
        if (index != -1) {
            binding?.includeMain?.toolbar?.title = menuItem.title
            binding?.includeMain?.viewPager?.setCurrentItem(index, false)
            return true
        }
        return false
    }

    override fun onMenuItemClick(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_privacy) {
            showTipsForce(this)
        } else if (id == R.id.action_about) {
            openNewPage(AboutFragment::class.java)
        }
        return false
    }

    @SingleClick
    override fun onClick(v: View) {
        val id = v.id
        if (id == R.id.nav_header) {
            XToastUtils.toast("点击头部！")
        }
    }
    //================Navigation================//
    /**
     * 底部导航栏点击事件
     *
     * @param menuItem
     * @return
     */
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val index = CollectionUtils.arrayIndexOf(mTitles, menuItem.title)
        if (index != -1) {
            binding?.includeMain?.toolbar?.title = menuItem.title
            binding?.includeMain?.viewPager?.setCurrentItem(index, false)
            updateSideNavStatus(menuItem)
            return true
        }
        return false
    }

    /**
     * 更新侧边栏菜单选中状态
     *
     * @param menuItem
     */
    private fun updateSideNavStatus(menuItem: MenuItem?) {
        menuItem?.let {
            val side = binding?.navView?.menu?.findItem(it.itemId)
            if (side != null) {
                side.isChecked = true
            }
        }
    }

    /**
     * 菜单、返回键响应
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ClickUtils.exitBy2Click(2000, this)
        }
        return true
    }

    override fun onRetry() {
        XToastUtils.toast("再按一次退出程序")
    }

    override fun onExit() {
        XUtil.exitApp()
    }
}