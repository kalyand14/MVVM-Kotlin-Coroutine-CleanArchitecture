package com.android.basics.core.scope

class InstanceContainer : InstanceScope {
    private val cache = HashMap<Any, Any>()

    override val size: Int
        get() = cache.size

    override fun has(key: Any): Boolean {
        return this.cache.containsKey(key)
    }

    override fun set(key: Any, value: Any) {
        this.cache[key] = value
    }

    override fun remove(key: Any) = this.cache.remove(key)

    override fun get(key: Any) = this.cache[key]

    override fun end() {
        for ((_, value) in cache) {
            if (value is ScopeObserver) {
                value.onScopeEnded()
            }
        }
        cache.clear()
    }
}