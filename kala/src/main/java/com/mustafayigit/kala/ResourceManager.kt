package com.mustafayigit.kala

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat


/**
 * Created by Mustafa Yiğit on 17/02/2023
 * mustafa.yt65@gmail.com
 */
object ResourceManager : IResourceManager {

    private const val DEFAULT_LANG: String = "en"
    private const val LANG_DELIMITER: String = "_"

    private var currentLang: String = DEFAULT_LANG
    private val strings: MutableMap<String, String> = mutableMapOf()
    private val drawables: MutableMap<String, Int> = mutableMapOf()

    var outsourceStringProvider: ((lang: String, key: String) -> String?)? = null

    fun getCurrentLanguage(): String {
        return currentLang
    }

    fun setLanguage(lang: String) {
        currentLang = lang
    }

    override fun provideStrings(strings: Map<String, String>) {
        ResourceManager.strings.putAll(strings)
    }

    override fun getString(key: String): String {
        return (strings[key] ?: getStringFromSource(key)).orEmpty()
    }

    override fun getString(key: String, vararg args: Any): String {
        return (strings[key] ?: getStringFromSource(key))?.format(*args).orEmpty()
    }

    override fun getStringFromSource(key: String): String? {
        return outsourceStringProvider?.invoke(currentLang, key)?.let { string ->
            strings[key] = string
            return@let string
        }
    }

    override fun getStringFromSource(key: String, vararg args: Any): String? {
        return outsourceStringProvider?.invoke(currentLang, key)?.let { string ->
            strings[key] = string
            return@let string.format(*args)
        }
    }

    override fun getDrawable(context: Context, key: String): Drawable? {
        return drawables[key]?.let { drawableId ->
            getDrawable(context, drawableId)
        } ?: findDrawableId(context, key)?.let { drawableId ->
            drawables[key] = drawableId
            getDrawable(context, drawableId)
        }
    }

    @SuppressLint("DiscouragedApi")
    private fun findDrawableId(context: Context, key: String): Int? {
        return context.resources?.getIdentifier(
            key + LANG_DELIMITER + currentLang,
            "drawable",
            context.packageName
        )
    }

    private fun getDrawable(context: Context, id: Int): Drawable? {
        return ContextCompat.getDrawable(context, id)
    }

    override fun clear() {
        strings.clear()
        drawables.clear()
    }

}