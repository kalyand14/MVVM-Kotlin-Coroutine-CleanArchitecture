package com.android.basics.core.scope

interface InstanceScope : BaseScope {
    val size: Int

    fun has(key: Any): Boolean

    operator fun set(key: Any, value: Any)

    operator fun get(key: Any): Any?

    fun remove(key: Any): Any?
}