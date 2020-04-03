package com.microdevrj.wave_visualizer

class RegistryList<K, V> {

    private val list: ArrayList<V> = ArrayList()

    private val registry: HashMap<K, Int> = HashMap()

    val size: Int
        get() = list.size


    /**
     * @return passed in value
     */
    @Deprecated("Not to use, confusing")
    fun registerUpdateReturnSecondParam(key: K, value: V): V {
        registerUpdate(key, value)
        return value
    }

    /**
     * Only register the value if another one with the same
     * id (even if they have different values) was not found
     */
    fun registerOnly(key: K, value: V): Boolean {
        if (registry[key] == null) {
            list.add(value)
            registry[key] = list.size - 1
            return true
        }
        return false
    }

    /**
     * @return whether the item was updated rather than added
     */
    fun registerUpdate(key: K, value: V): Boolean {
        return if (registry[key] != null) {
            list[registry[key]!!] = value
            false
        } else {
            list.add(value)
            registry[key] = list.size - 1
            true
        }
    }

    fun iterate(callback: (value: V) -> Unit) {
        for (i in list.indices)
            callback(list[i])
    }

    operator fun get(k: K): V {
            return list[registry[k]!!]
    }

}