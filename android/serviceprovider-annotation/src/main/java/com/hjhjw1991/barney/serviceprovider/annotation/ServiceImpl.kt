package com.hjhjw1991.barney.serviceprovider.annotation

import kotlin.reflect.KClass

/**
 * SPI实现注解
 * @author huangjun.barney
 * @since 2021/8/24
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class ServiceImpl(val service: Array<KClass<*>>, val singleton: Boolean = true)
