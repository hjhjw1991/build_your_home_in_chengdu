package com.hjhjw1991.barney.util

/**
 * @author huangjun.barney
 * @since 2021/12/10
 */

fun <T> Any?.castTo(clazz: Class<out T>): T? {
    return if (clazz.isInstance(this)) {
        this as T
    } else {
        null
    }
}

/**
 * collection extension
 * @author huangjun.barney
 * @since 2021/12/7
 */
fun <E> Collection<E>?.toString(): String {
    if (this == null) return "[]"
    return this.joinToString(prefix = "[", postfix = "]")
}