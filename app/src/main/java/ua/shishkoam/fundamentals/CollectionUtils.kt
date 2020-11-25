package ua.shishkoam.fundamentals

import android.os.Bundle

object CollectionUtils {
    fun toBundleBooleanMap(input: HashMap<String, Boolean>): Bundle? {
        val output = Bundle()
        for (key in input.keys) {
            output.putBoolean(key, input[key]!!)
        }
        return output
    }

    fun fromBundleBooleanMap(input: Bundle?): Map<String, Boolean>? {
        input?.let {
            val output: MutableMap<String, Boolean> = HashMap()
            for (key in input.keySet()) {
                output[key] = input.getBoolean(key)
            }
            return output
        }
        return null
    }
}