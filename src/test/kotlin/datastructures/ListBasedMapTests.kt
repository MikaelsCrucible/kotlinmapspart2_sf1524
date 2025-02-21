package datastructures

import kotlin.test.Test
import kotlin.test.assertNull

class ListBasedMapTests : CustomMutableMapTestsParent() {
    override fun emptyCustomMutableMapStringInt(): CustomMutableMap<String, Int> = ListBasedMap()

    override fun emptyCustomMutableMapCollidingStringInt(): CustomMutableMap<CollidingString, Int> = ListBasedMap()

    @Test
    fun testPlaceholder() {
        // A placeholder so that this class has at least one test, because sometimes IDEs
        // behave strangely if all tests are in the superclass.
        assertNull(emptyCustomMutableMapStringInt()["a"])
    }
}
