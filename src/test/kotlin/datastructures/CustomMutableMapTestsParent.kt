package datastructures

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

abstract class CustomMutableMapTestsParent {
    abstract fun emptyCustomMutableMapStringInt(): CustomMutableMap<String, Int>

    abstract fun emptyCustomMutableMapCollidingStringInt(): CustomMutableMap<CollidingString, Int>

    @Test
    fun `test contains when empty`() {
        val map = emptyCustomMutableMapStringInt()
        assertFalse(map.contains("Hello"))
    }

    @Test
    fun `test contains after put`() {
        val map = emptyCustomMutableMapStringInt()
        map.put("Hello", 3)
        assertTrue(map.contains("Hello"))
    }

    @Test
    fun `test get returns null`() {
        val map = emptyCustomMutableMapStringInt()
        map.put("Hello", 3)
        map.put("World", 4)
        assertNull(map.get("You"))
    }

    @Test
    fun `test get returns latest value`() {
        val map = emptyCustomMutableMapStringInt()
        map.put("Hello", 3)
        map.put("World", 4)
        map.put("Hello", 10)
        map.put("Hello", 11)
        assertEquals(11, map.get("Hello"))
    }

    @Test
    fun `test entries initially empty`() {
        val map = emptyCustomMutableMapStringInt()
        for (e in map) {
            fail("Map entries should be empty")
        }
    }

    @Test
    fun `test entries after some putting`() {
        val expected = emptyCustomMutableMapStringInt()
        val entries = (1..100).map {
            CustomMutableMap.Entry(it.toString(), it)
        }
        entries.forEach(expected::put)
        assertEquals(entries, expected.toSet().sortedBy { it.value })
    }

    @Test
    fun `test entries after some setting`() {
        val map = emptyCustomMutableMapStringInt()
        val expected: List<CustomMutableMap.Entry<String, Int>> = (1..100).map {
            CustomMutableMap.Entry(it.toString(), it)
        }
        expected.forEach {
            map[it.key] = it.value
        }
        assertEquals(expected, map.toList().sortedBy { it.value })
    }

    @Test
    fun `test entries after some putting, removing and setting`() {
        val map = createCustomMutableMapByPuttingRemovingAndSetting()
        val expected = createExpectedEntriesFromPuttingRemovingAndSetting()
        assertEquals(expected, map.toList().sortedBy { it.value })
    }

    @Test
    fun `test entries after some putting (collision prone)`() {
        val map = emptyCustomMutableMapCollidingStringInt()
        val expected = (1..100).map {
            CustomMutableMap.Entry(CollidingString(it.toString()), it)
        }
        expected.forEach(map::put)
        assertEquals(expected, map.toList().sortedBy { it.value })
    }

    @Test
    fun `test entries after some setting (collision prone)`() {
        val map = emptyCustomMutableMapCollidingStringInt()
        val expected = (1..100).map {
            CustomMutableMap.Entry(CollidingString(it.toString()), it)
        }
        expected.forEach {
            map[it.key] = it.value
        }
        assertEquals(expected, map.toList().sortedBy { it.value })
    }

    @Test
    fun `test entries after some putting, removing and setting (collision prone)`() {
        val map = createCollisionProneMapByPuttingRemovingAndSetting()
        val expected = createCollisionProneExpectedEntriesFromPuttingRemovingAndSetting()
        assertEquals(expected, map.toList().sortedBy { it.value })
    }

    class CollidingString(val string: String) : Comparable<CollidingString> {
        override fun hashCode(): Int = 5
        override fun compareTo(other: CollidingString): Int = string.compareTo(other.string)

        override fun equals(other: Any?): Boolean {
            if (other is CollidingString) {
                return string == other.string
            }
            return false
        }
    }

    private fun createCustomMutableMapByPuttingRemovingAndSetting(): CustomMutableMap<String, Int> {
        val map = emptyCustomMutableMapStringInt()
        for (i in 1..100) {
            assertFalse(map.contains(i.toString()))
            assertNull(map.get(i.toString()))
            assertNull(map.put(CustomMutableMap.Entry(i.toString(), i)))
        }
        for (i in 1..100) {
            assertTrue(map.contains(i.toString()))
            assertEquals(i, map.get(i.toString()))
            if (i % 2 == 0) {
                val previous = map.remove(i.toString())
                assertNotNull(previous)
                assertEquals(i, previous)
            }
        }
        for (i in 1..100) {
            if (i % 4 == 0) {
                assertNull(map.get(i.toString()))
                assertFalse(map.contains(i.toString()))
                assertNull(map.set(i.toString(), i))
            }
        }
        return map
    }

    private fun createExpectedEntriesFromPuttingRemovingAndSetting(): List<CustomMutableMap.Entry<String, Int>> {
        val entries = (1..100).map {
            CustomMutableMap.Entry(it.toString(), it)
        }.filter {
            it.value % 2 != 0 || it.value % 4 == 0
        }
        return entries
    }

    private fun createCollisionProneMapByPuttingRemovingAndSetting(): CustomMutableMap<CollidingString, Int> {
        val map = emptyCustomMutableMapCollidingStringInt()
        for (i in 1..100) {
            assertFalse(map.contains(CollidingString(i.toString())))
            assertNull(map.get(CollidingString(i.toString())))
            assertNull(map.put(CustomMutableMap.Entry(CollidingString(i.toString()), i)))
        }
        for (i in 1..100) {
            assertTrue(map.contains(CollidingString(i.toString())))
            assertEquals(i, map.get(CollidingString(i.toString())))
            if (i % 2 == 0) {
                val previous = map.remove(CollidingString(i.toString()))
                assertNotNull(previous)
                assertEquals(i, previous)
            }
        }
        for (i in 1..100) {
            if (i % 4 == 0) {
                assertNull(map.get(CollidingString(i.toString())))
                assertFalse(map.contains(CollidingString(i.toString())))
                assertNull(map.set(CollidingString(i.toString()), i))
            }
        }
        return map
    }

    private fun createCollisionProneExpectedEntriesFromPuttingRemovingAndSetting(): List<CustomMutableMap.Entry<CollidingString, Int>> {
        val entries = (1..100).map {
            CustomMutableMap.Entry(CollidingString(it.toString()), it)
        }.filter {
            it.value % 2 != 0 || it.value % 4 == 0
        }
        return entries
    }
}
