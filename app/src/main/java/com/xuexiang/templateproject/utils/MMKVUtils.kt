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
package com.xuexiang.templateproject.utils

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV

/**
 * MMKV工具类
 *
 * @author xuexiang
 * @since 2019-07-04 10:20
 */
object MMKVUtils {

    private lateinit var sMMKV: MMKV

    /**
     * 初始化
     *
     * @param context
     */
    fun init(context: Context) {
        MMKV.initialize(context.applicationContext)
        sMMKV = MMKV.defaultMMKV()
    }

    fun getMMKV(): MMKV {
        if (sMMKV == null) {
            sMMKV = MMKV.defaultMMKV()
        }
        return sMMKV
    }
    //=======================================键值保存==================================================//
    /**
     * 保存键值
     *
     * @param key
     * @param value
     * @return
     */
    fun put(key: String?, value: Any?): Boolean {
        return when (value) {
            is Int -> getMMKV().encode(key, value)
            is Float -> getMMKV().encode(key, value)
            is String -> getMMKV().encode(key, value)
            is Boolean -> getMMKV().encode(key, value)
            is Long -> getMMKV().encode(key, value)
            is Double -> getMMKV().encode(key, value)
            is Parcelable -> getMMKV().encode(key, value)
            is ByteArray -> getMMKV().encode(key, value)
            is Set<*> -> getMMKV().encode(key, value as Set<String?>?)
            else -> false
        }
    }
    //=======================================键值获取==================================================//
    /**
     * 获取键值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    operator fun get(key: String?, defaultValue: Any?): Any? {
        return when (defaultValue) {
            is Int -> getMMKV().decodeInt(key, defaultValue)
            is Float -> getMMKV().decodeFloat(key, defaultValue)
            is String -> getMMKV().decodeString(key, defaultValue)
            is Boolean -> getMMKV().decodeBool(key, defaultValue)
            is Long -> getMMKV().decodeLong(key, defaultValue)
            is Double -> getMMKV().decodeDouble(key, defaultValue)
            is ByteArray -> getMMKV().decodeBytes(key)
            is Set<*> -> getMMKV().decodeStringSet(key, defaultValue as Set<String?>?)
            else -> null
        }
    }

    /**
     * 根据key获取boolean值
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getBoolean(key: String?, defValue: Boolean): Boolean {
        try {
            return getMMKV().getBoolean(key, defValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defValue
    }

    /**
     * 根据key获取long值
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getLong(key: String?, defValue: Long): Long {
        try {
            return getMMKV().getLong(key, defValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defValue
    }

    /**
     * 根据key获取float值
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getFloat(key: String?, defValue: Float): Float {
        try {
            return getMMKV().getFloat(key, defValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defValue
    }

    /**
     * 根据key获取String值
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getString(key: String?, defValue: String?): String? {
        try {
            return getMMKV().getString(key, defValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defValue
    }

    /**
     * 根据key获取int值
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getInt(key: String?, defValue: Int): Int {
        try {
            return getMMKV().getInt(key, defValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defValue
    }

    /**
     * 根据key获取double值
     *
     * @param key
     * @param defValue
     * @return
     */
    fun getDouble(key: String?, defValue: Double): Double {
        try {
            return getMMKV().decodeDouble(key, defValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defValue
    }

    /**
     * 获取对象
     *
     * @param key
     * @param tClass 类型
     * @param <T>
     * @return
    </T> */
    fun <T : Parcelable?> getObject(key: String?, tClass: Class<T>?): T? {
        return getMMKV().decodeParcelable(key, tClass)
    }

    /**
     * 获取对象
     *
     * @param key
     * @param tClass 类型
     * @param <T>
     * @return
    </T> */
    fun <T : Parcelable?> getObject(key: String?, tClass: Class<T>?, defValue: T): T? {
        try {
            return getMMKV().decodeParcelable(key, tClass, defValue)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return defValue
    }

    /**
     * 判断键值对是否存在
     *
     * @param key 键
     * @return 键值对是否存在
     */
    fun containsKey(key: String?): Boolean {
        return getMMKV().containsKey(key)
    }

    /**
     * 清除指定键值对
     *
     * @param key 键
     */
    fun remove(key: String?) {
        getMMKV().remove(key).apply()
    }
}