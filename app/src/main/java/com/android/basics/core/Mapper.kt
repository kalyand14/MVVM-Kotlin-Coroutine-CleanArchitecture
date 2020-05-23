package com.android.basics.core

interface Mapper<in From : Any, out To : Any> {
    fun convert(from: From): To
}