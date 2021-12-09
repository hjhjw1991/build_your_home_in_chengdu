package com.hjhjw1991.barney.util

import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

/**
 * JSON扩展
 * @author huangjun.barney
 * @since 2021/8/23
 */
val gson = Gson()

fun <T> fromJsonString(json: String, typeClass: Class<T>): T = gson.fromJson(json, typeClass)

fun toJsonString(obj: Any): String = gson.toJson(obj)

fun JSONObject.fromMap(data: Map<*, *>): JSONObject = JSONObject().apply {
    data.entries.forEach {
        put(it.key.toString(), it.value)
    }
}

fun JSONArray.fromList(data: List<*>): JSONArray = JSONArray().apply {
    data.forEach {
        put(it)
    }
}

fun JSONObject.toMap(): Map<String, Any> {
   val ret = mutableMapOf<String, Any>()
   this.keys().forEach {
       when (val value = this[it]) {
           is JSONObject -> ret[it] = value.toMap()
           is JSONArray -> ret[it] = value.toList()
           else -> ret[it] = value
       }
   }
    return ret
}

fun JSONArray.toList(): List<*> = mutableListOf<Any>().apply {
    val ret = mutableListOf<Any>()
    forEach {
        when (val value = it) {
            is JSONObject -> ret.add(value.toMap())
            is JSONArray -> ret.add(value.toList())
            else -> ret.add(value)
        }
    }
    return ret
}

infix fun JSONObject.merge(other: JSONObject): JSONObject {
    for(key in other.keys()) {
        this.put(key, other.get(key))
    }
    return this
}