package datastructures

interface CustomMutableMap<K, V> : Iterable<CustomMutableMap.Entry<K, V>> {
    data class Entry<K, V>(val key: K, val value: V)

    // Provides an iterator for traversing the entries of the map. This method is
    // required by Iterable. Strictly, this requirement can be omitted from the
    // CustomMutableMap interface because it is implicitly required by the Iterable
    // parent interface. However, restating the requirement adds clarity here.
    override operator fun iterator(): Iterator<Entry<K, V>>

    // Returns the value at 'key', or null if there is no such value.
    // This operator allows array-like indexing.
    operator fun get(key: K): V?

    // Operator version of 'put' to allow array-like indexing.
    operator fun set(key: K, value: V): V? = put(key, value)

    // Associates 'value' with 'key'. Returns the previous value associated with
    // 'key', or null if there is no such previous value.
    fun put(key: K, value: V): V?

    // Puts the given entry into the map.
    fun put(entry: Entry<K, V>): V? = put(entry.key, entry.value)

    // Removes entry with key 'key' from the map if such an entry exists, returning
    // the associated value if so. Otherwise, returns null.
    fun remove(key: K): V?

    // Returns true if and only if there is some value associated with 'key' in the map.
    fun contains(key: K): Boolean = get(key) != null
}
