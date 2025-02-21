package datastructures

class CustomLinkedList<T> : MutableIterable<T> {

    private interface Node<T> {
        var next: ValueNode<T>?
    }

    private class ValueNode<T>(
        val value: T,
        override var next: ValueNode<T>?,
    ) : Node<T>

    private class RootNode<T> : Node<T> {
        override var next: ValueNode<T>? = null
    }

    private val root: RootNode<T> = RootNode()

    val isEmpty: Boolean
        get() = root.next === null

    fun addFirst(item: T) {
        root.next = ValueNode(item, root.next)
    }

    fun removeFirst(): T? {
        val toRemove = root.next ?: return null
        root.next = toRemove.next
        return toRemove.value
    }

    fun peek(): T? = root.next?.value

    override fun iterator(): MutableIterator<T> = object : MutableIterator<T> {
        private var prev: Node<T>? = null
        private var curr: Node<T> = root
        private var next: ValueNode<T>? = root.next

        override fun hasNext(): Boolean = next !== null

        override fun next(): T {
            val nextNotNull = next ?: throw NoSuchElementException("Called next on empty iterator!")
            prev = curr
            curr = nextNotNull
            next = nextNotNull.next
            return nextNotNull.value
        }

        override fun remove() {
            if (prev === null) {
                throw IllegalStateException("Called remove before next!")
            }
            val prevNotNull = prev!!
            prevNotNull.next = next
            curr = prevNotNull
            prev = null
        }
    }
}
