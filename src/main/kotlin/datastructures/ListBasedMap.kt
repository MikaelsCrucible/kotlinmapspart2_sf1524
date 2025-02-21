package datastructures

class ListBasedMap<K, V> : CustomMutableMap<K, V> {

    private val contents: CustomLinkedList<CustomMutableMap.Entry<K, V>> = CustomLinkedList()

    override fun iterator(): Iterator<CustomMutableMap.Entry<K, V>> = contents.iterator()

    override fun get(key: K): V? = contents.find { it.key == key }?.value

    override fun put(key: K, value: V): V? {
        val prev = get(key)
        remove(key)
        contents.addFirst(CustomMutableMap.Entry(key, value))
        return prev
    }

    override fun remove(key: K): V? {
        val iterator = contents.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.key == key) {
                iterator.remove()
                return entry.value
            }
        }
        return null
    }
}
