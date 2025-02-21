package datastructures

private const val POW2_SIZE = true
private const val MAX_LOAD_FACTOR = 0.75
private const val DEFAULT_SIZE = 32

typealias BucketFactory<K, V> = () -> CustomMutableMap<K, V>

abstract class GenericHashmap<K, V>(private val bucketFactory: BucketFactory<K, V>) : CustomMutableMap<K, V> {

    private var buckets: Array<CustomMutableMap<K, V>> = Array(DEFAULT_SIZE) { bucketFactory() }

    private var size: Int = 0

    private fun resize() {
        val entries = CustomLinkedList<CustomMutableMap.Entry<K, V>>()
        this.forEach(entries::addFirst)
        buckets = Array(buckets.size * 2) { bucketFactory() }
        entries.forEach { put(it.key, it.value) }
    }

    private fun K.bucketIndex(): Int =
        if (POW2_SIZE) {
            hashCode() and (buckets.size - 1)
        } else {
            Math.floorMod(hashCode(), buckets.size)
        }

    private fun K.bucket(): CustomMutableMap<K, V> = buckets[bucketIndex()]

    final override fun iterator(): Iterator<CustomMutableMap.Entry<K, V>> = object : Iterator<CustomMutableMap.Entry<K, V>> {

        private val bucketIterators = CustomLinkedList<Iterator<CustomMutableMap.Entry<K, V>>>()

        init {
            for (bucket in buckets) {
                bucketIterators.addFirst(bucket.iterator())
            }
        }

        override fun hasNext(): Boolean {
            while (!bucketIterators.isEmpty) {
                if (bucketIterators.peek()!!.hasNext()) {
                    return true
                }
                bucketIterators.removeFirst()
            }
            return false
        }

        override fun next(): CustomMutableMap.Entry<K, V> {
            if (bucketIterators.isEmpty || !bucketIterators.peek()!!.hasNext()) {
                throw NoSuchElementException()
            }
            val result = bucketIterators.peek()!!.next()
            // Skip on to the next element that can be provided by a bucket iterator.
            hasNext()
            return result
        }
    }

    final override fun put(key: K, value: V): V? {
        val result = key.bucket().put(key, value)
        if (result == null) {
            size++
            // Using Java's approach to resizing Hashmaps - i.e: never shrinks, doubles when
            // load factor exceeds maximum load factor.
            if (size > buckets.size * MAX_LOAD_FACTOR) {
                resize()
            }
        }
        return result
    }

    final override fun get(key: K): V? = key.bucket()[key]

    final override fun remove(key: K): V? {
        val result = key.bucket().remove(key)
        if (result != null) {
            size--
        }
        return result
    }
}
