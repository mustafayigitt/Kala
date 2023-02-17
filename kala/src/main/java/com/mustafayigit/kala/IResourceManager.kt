package com.mustafayigit.kala

import android.content.Context
import android.graphics.drawable.Drawable


/**
 * Created by Mustafa Yiğit on 17/02/2023
 * mustafa.yt65@gmail.com
 */
interface IResourceManager {

    fun provideStrings(strings: Map<String, String>)
    fun getString(key: String): String
    fun getString(key: String, vararg args: Any): String
    fun getStringFromSource(key: String): String?
    fun getStringFromSource(key: String, vararg args: Any): String?
    fun getDrawable(context: Context, key: String): Drawable?
    fun clear()
}