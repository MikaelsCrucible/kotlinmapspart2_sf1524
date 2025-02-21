package datastructures
/*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.fail

class TreeBasedMapIteratorTests {

    @Test
    fun `test empty map`() {
        val map = TreeBasedMap<String, Int>(String::compareTo)
        val iterator = map.iterator()
        assertFalse(iterator.hasNext())
        try {
            iterator.next()
            fail("Expected NoSuchElementException")
        } catch (e: NoSuchElementException) {
            // Good - an exception was expected.
        }
    }

    @Test
    fun `test iterator yields sorted order`() {
        val map = TreeBasedMap<String, Int>(String::compareTo)
        map["a"] = 1
        map["z"] = 2
        map["c"] = 3
        map["f"] = 12
        map["g"] = 4
        map["b"] = 14
        map["s"] = 5
        map["a"] = 11
        map["z"] = 12
        map["c"] = 13
        map["f"] = 112
        map["g"] = 14
        map["b"] = 114
        map["s"] = 15
        val iterator = map.iterator()

        assertTrue(iterator.hasNext())
        val e1 = iterator.next()
        assertEquals("a", e1.key)
        assertEquals(11, e1.value)

        assertTrue(iterator.hasNext())
        val e2 = iterator.next()
        assertEquals("b", e2.key)
        assertEquals(114, e2.value)

        assertTrue(iterator.hasNext())
        val e3 = iterator.next()
        assertEquals("c", e3.key)
        assertEquals(13, e3.value)

        assertTrue(iterator.hasNext())
        val e4 = iterator.next()
        assertEquals("f", e4.key)
        assertEquals(112, e4.value)

        assertTrue(iterator.hasNext())
        val e5 = iterator.next()
        assertEquals("g", e5.key)
        assertEquals(14, e5.value)

        assertTrue(iterator.hasNext())
        val e6 = iterator.next()
        assertEquals("s", e6.key)
        assertEquals(15, e6.value)

        assertTrue(iterator.hasNext())
        val e7 = iterator.next()
        assertEquals("z", e7.key)
        assertEquals(12, e7.value)

        assertFalse(iterator.hasNext())
        try {
            iterator.next()
            fail("Expected NoSuchElementException")
        } catch (e: NoSuchElementException) {
            // Good - an exception was expected.
        }
    }
}
*/
