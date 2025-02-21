package datastructures

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class CustomLinkedListMutableIteratorTests {

    @Test
    fun testBasicIteratorFunctionality() {
        val list = CustomLinkedList<Char>()
        list.addFirst('A')
        list.addFirst('B')
        list.addFirst('C')
        list.addFirst('D')
        val encounteredElements = mutableListOf<Char>()
        list.forEach {
            encounteredElements.add(it)
        }
        assertEquals(listOf('D', 'C', 'B', 'A'), encounteredElements)
    }

    @Test
    fun testIteratorNext() {
        val list = CustomLinkedList<Double>()
        list.addFirst(1.0)
        list.addFirst(2.0)
        list.addFirst(3.0)
        list.addFirst(4.0)
        val iterator: MutableIterator<Double> = list.iterator()
        assertTrue(iterator.hasNext())
        assertEquals(4.0, iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(3.0, iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(2.0, iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(1.0, iterator.next())
        assertFalse(iterator.hasNext())
        try {
            iterator.next()
            fail("Expected NoSuchElementException")
        } catch (e: NoSuchElementException) {
            // Good: an exception was expected
        }
    }

    @Test
    fun testIteratorNextAndRemove() {
        val list = CustomLinkedList<Double>()
        list.addFirst(1.0)
        list.addFirst(2.0)
        list.addFirst(3.0)
        list.addFirst(4.0)
        val iterator: MutableIterator<Double> = list.iterator()
        assertTrue(iterator.hasNext())
        assertEquals(4.0, iterator.next())
        iterator.remove()
        assertTrue(iterator.hasNext())
        assertEquals(3.0, iterator.next())
        iterator.remove()
        assertTrue(iterator.hasNext())
        assertEquals(2.0, iterator.next())
        iterator.remove()
        assertTrue(iterator.hasNext())
        assertEquals(1.0, iterator.next())
        iterator.remove()
        assertFalse(iterator.hasNext())
        assertTrue(list.isEmpty)
    }

    @Test
    fun testIteratorExceptionOnDoubleRemove() {
        val list = CustomLinkedList<Double>()
        list.addFirst(1.0)
        list.addFirst(2.0)
        val iterator: MutableIterator<Double> = list.iterator()
        assertTrue(iterator.hasNext())
        assertEquals(2.0, iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(1.0, iterator.next())
        iterator.remove()
        try {
            iterator.remove()
            fail("Expected IllegalStateException")
        } catch (e: IllegalStateException) {
            // Good: an exception was expected.
        }
    }

    @Test
    fun testIteratorExceptionOnImmediateRemove() {
        val list = CustomLinkedList<Int>()
        try {
            list.iterator().remove()
            fail("Expected IllegalStateException")
        } catch (e: IllegalStateException) {
            // Good, an exception was expected.
        }
        list.addFirst(1)
        try {
            list.iterator().remove()
            fail("Expected IllegalStateException")
        } catch (e: IllegalStateException) {
            // Good, an exception was expected.
        }
        list.addFirst(2)
        try {
            list.iterator().remove()
            fail("Expected IllegalStateException")
        } catch (e: IllegalStateException) {
            // Good, an exception was expected.
        }
        list.addFirst(3)
        try {
            list.iterator().remove()
            fail("Expected IllegalStateException")
        } catch (e: IllegalStateException) {
            // Good, an exception was expected.
        }
    }

    @Test
    fun testIteratorRemoveWhenSizeIs1() {
        val list = CustomLinkedList<Int>()
        list.addFirst(1)
        val iterator: MutableIterator<Int> = list.iterator()
        assertEquals(1, iterator.next())
        iterator.remove()
        assertTrue(list.isEmpty)
        try {
            iterator.remove()
            fail("Expected IllegalStateException")
        } catch (e: IllegalStateException) {
            // Good, an exception was expected.
        }
    }
}
