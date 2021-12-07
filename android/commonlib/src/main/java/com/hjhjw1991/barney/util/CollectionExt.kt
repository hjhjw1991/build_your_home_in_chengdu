package com.hjhjw1991.barney.util

/**
 * collection extension
 * @author huangjun.barney
 * @since 2021/12/7
 */
fun <E> Collection<E>?.toString(): String {
    if (this == null) return "[]"
    return this.joinToString(prefix = "[", postfix = "]")
}