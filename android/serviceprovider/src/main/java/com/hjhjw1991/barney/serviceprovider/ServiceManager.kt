package com.hjhjw1991.barney.serviceprovider

import java.util.concurrent.ConcurrentHashMap

object ServiceManager {
    var serviceMap: ConcurrentHashMap<Class<*>, Any> = ConcurrentHashMap()
    var servicesMap: ConcurrentHashMap<Class<*>, Set<*>> = ConcurrentHashMap()

    fun <T> getService(clazz: Class<out T>): T? {
        return serviceMap[clazz] as? T
    }

    fun <T> getServices(clazz: Class<out T>): Set<T>? {
        return servicesMap[clazz] as? Set<T>
    }

    fun <T> bindService(clazz: Class<in T>, instance: T) {
        val old = serviceMap[clazz]
        if (old != instance) {
            println("replace $old with $instance")
        }
        serviceMap[clazz] = instance as Any
    }
}