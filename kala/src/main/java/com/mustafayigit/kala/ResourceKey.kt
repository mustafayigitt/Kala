package com.mustafayigit.kala

import android.content.Context
import android.graphics.drawable.Drawable
import kotlin.reflect.KClass


/**
 * Created by Mustafa Yiğit on 17/02/2023
 * mustafa.yt65@gmail.com
 */
class ResourceKey<T : Any>(
    private val type: KClass<T>,
    private val key: String
) {
    context(IResourceManager, Context)
    @Suppress("UNCHECKED_CAST")
    operator fun invoke(): T {
        return when (type) {
            String::class -> getString(key) as T
            Drawable::class -> getDrawable(this@Context, key) as T
            else -> throw IllegalArgumentException("Resource type is not supported")
        }
    }

    context(IResourceManager)
    operator fun invoke(vararg args: Any): String {
        return getString(key, *args)
    }

    override fun toString(): String {
        return key
    }

    companion object {
        inline operator fun <reified T : Any> invoke(key: String) = ResourceKey(T::class, key)
    }
}
