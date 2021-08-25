package com.hjhjw1991.barney.serviceprovider

object ServiceManager {
    var serviceMap: MutableMap<Class<*>, Any> = mutableMapOf()
    var servicesMap: MutableMap<Class<*>, List<*>> = mutableMapOf()

    fun <T> getService(clazz: Class<out T>): T? {
        return serviceMap[clazz] as? T
    }

    fun <T> getServices(clazz: Class<out T>): List<T>? {
        return servicesMap[clazz] as? List<T>
    }

    fun <T> bindService(clazz: Class<in T>, instance: T) {
        val old = serviceMap[clazz]
        if (old != instance) {
            println("replace $old with $instance")
        }
        serviceMap[clazz] = instance as Any
    }
}