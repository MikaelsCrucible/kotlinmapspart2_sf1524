package datastructures

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CustomLinkedListBasicTests {

    @Test
    fun testAddFirst() {
        val list = CustomLinkedList<String>()
        assertTrue(list.isEmpty)
        assertFalse(list.contains("cat"))
        assertFalse(list.contains("dog"))
        list.addFirst("cat")
        assertFalse(list.isEmpty)
        assertTrue(list.contains("cat"))
        assertFalse(list.contains("dog"))
        list.addFirst("dog")
        assertTrue(list.contains("cat"))
        assertTrue(list.contains("dog"))
    }

    @Test
    fun testPeek() {
        val list = CustomLinkedList<Int>()
        assertNull(list.peek())
        list.addFirst(1)
        assertEquals(1, list.peek())
        list.addFirst(2)
        assertEquals(2, list.peek())
    }

    @Test
    fun testRemoveFirst() {
        val list = CustomLinkedList<Double>()
        list.addFirst(1.0)
        list.addFirst(2.0)
        list.addFirst(3.0)
        list.addFirst(4.0)
        assertEquals(4.0, list.removeFirst())
        assertEquals(3.0, list.removeFirst())
        assertEquals(2.0, list.removeFirst())
        assertEquals(1.0, list.removeFirst())
        assertNull(list.removeFirst())
    }
}
