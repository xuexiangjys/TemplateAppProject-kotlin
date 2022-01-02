package com.xuexiang.templateproject.utils

/**
 * SharedPreferences管理工具基类
 *
 * @author xuexiang
 * @since 2018/11/27 下午5:16
 */
class SettingUtils private constructor() {
    companion object {
        private const val IS_FIRST_OPEN_KEY = "is_first_open_key"
        private const val IS_AGREE_PRIVACY_KEY = "is_agree_privacy_key"
        /**
         * 是否是第一次启动
         */
        /**
         * 设置是否是第一次启动
         */
        var isFirstOpen: Boolean
            get() = MMKVUtils.getBoolean(IS_FIRST_OPEN_KEY, true)
            set(isFirstOpen) {
                MMKVUtils.put(IS_FIRST_OPEN_KEY, isFirstOpen)
            }
        /**
         * 获取是否同意隐私政策
         *
         * @return 是否同意隐私政策
         */
        /**
         * 设置是否同意隐私政策
         *
         * @param isAgreePrivacy 是否同意隐私政策
         */
        @JvmStatic
        var isAgreePrivacy: Boolean
            get() = MMKVUtils.getBoolean(IS_AGREE_PRIVACY_KEY, false)
            set(isAgreePrivacy) {
                MMKVUtils.put(IS_AGREE_PRIVACY_KEY, isAgreePrivacy)
            }
    }

    init {
        throw UnsupportedOperationException("u can't instantiate me...")
    }
}