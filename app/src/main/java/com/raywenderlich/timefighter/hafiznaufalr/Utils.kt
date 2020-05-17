package com.raywenderlich.timefighter.hafiznaufalr

enum class Status {
    RUNNING,
    DONE
}

data class Resource<out T>(val status: Status, val data: T?) {
    companion object {
        fun <T> running(data: T?): Resource<T> {
            return Resource(Status.RUNNING, data)
        }

        fun <T> done(data: T?): Resource<T> {
            return Resource(Status.DONE, data)
        }
    }
}