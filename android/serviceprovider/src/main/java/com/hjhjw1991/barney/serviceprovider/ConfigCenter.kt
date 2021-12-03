package com.hjhjw1991.barney.serviceprovider

/**
 * service config
 * @author huangjun.barney
 * @since 2021/12/3
 */
object ConfigCenter {

    private var configMap = mutableMapOf<String, MutableSet<String>>()

    fun getConfig(name: String): Set<String> {
        return configMap[name].orEmpty()
    }

    fun addConfig(name: String, config: String) {
        if (name !in configMap) {
            configMap[name] = mutableSetOf()
        }
        configMap[name]?.add(config)
    }
}