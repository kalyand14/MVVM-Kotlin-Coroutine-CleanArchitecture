package com.android.basics.core.functional

import com.android.basics.core.exception.Failure

data class Resource<out T>(val status: ResourceStatus, val data: T?, val failure: Failure?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(ResourceStatus.SUCCESS, data, null)
        }

        fun <T> error(failure: Failure): Resource<T> {
            return Resource(ResourceStatus.ERROR, null, failure)
        }

        fun <T> loading(): Resource<T> {
            return Resource(ResourceStatus.LOADING, null, null)
        }
    }

}