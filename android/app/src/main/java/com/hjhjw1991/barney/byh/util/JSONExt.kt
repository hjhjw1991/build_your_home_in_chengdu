package com.hjhjw1991.barney.byh.util

import org.json.JSONObject

/**
 * JSON扩展
 * @author huangjun.barney
 * @since 2021/8/23
 */

fun JSONObject.fromMap(data: Any, clazz: Class<Map<*, *>>): JSONObject {
    // todo convert map to json
    return JSONObject()
}

fun JSONObject.toMap(): Map<String, Any> {
    // todo convert json to map
    return mutableMapOf()
}

infix fun JSONObject.merge(other: JSONObject): JSONObject {
    for(key in other.keys()) {
        this.put(key, other.get(key))
    }
    return this
}