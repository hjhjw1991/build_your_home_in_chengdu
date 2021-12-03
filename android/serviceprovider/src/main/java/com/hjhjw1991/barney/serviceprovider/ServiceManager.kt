package com.hjhjw1991.barney.serviceprovider

import androidx.annotation.Keep
import java.util.concurrent.ConcurrentHashMap

@Keep
object ServiceManager {
    var serviceMap: ConcurrentHashMap<Class<*>, Any> = ConcurrentHashMap()
    var servicesMap: ConcurrentHashMap<Class<*>, Set<*>> = ConcurrentHashMap()

    fun <T> getService(clazz: Class<out T>): T? {
        var instance: T? = serviceMap[clazz] as? T
        if (instance == null) {
            instance = createServiceFor(clazz)
            if (instance != null) {
                serviceMap[clazz] = instance as Any
            }
        }
        return instance
    }

    private fun <T> createServiceFor(name: Class<T>): T? {
        val configs = ConfigCenter.getConfig(name.name)
        return configs.firstOrNull()
            ?.runCatching {
                Class.forName(this).newInstance() as T
            }
            ?.getOrNull()
    }

    private fun <T> createServicesFor(name: Class<T>): Set<T>? {
        val configs = ConfigCenter.getConfig(name.name)
        return configs.map {
            Class.forName(it).newInstance() as T
        }.toSet()
    }

    fun <T> getServices(clazz: Class<out T>): Set<T>? {
        var instance: Set<T>? = servicesMap[clazz] as? Set<T>
        if (instance == null) {
            instance = createServicesFor(clazz)
            if (instance != null) {
                serviceMap[clazz] = instance as Any
            }
        }
        return instance
    }

    fun <T> bindService(clazz: Class<in T>, instance: T) {
        val old = serviceMap[clazz]
        if (old != instance) {
            println("replace $old with $instance")
        }
        serviceMap[clazz] = instance as Any
    }
}