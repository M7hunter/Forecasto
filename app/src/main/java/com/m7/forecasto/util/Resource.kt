package com.m7.forecasto.util

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val msg: String? = null,
    val resId: Int? = null
) {
    companion object {
        fun <T> loading(data: T? = null): Resource<T> = Resource(Status.LOADING, data)
        fun <T> success(data: T?, msg: String? = null): Resource<T> = Resource(Status.SUCCESS, data, msg)
        fun <T> error(data: T? = null, msg: String? = null, resId: Int? = null): Resource<T> =
            Resource(Status.ERROR, data, msg, resId)
    }
}
