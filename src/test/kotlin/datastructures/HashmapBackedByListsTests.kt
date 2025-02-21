package datastructures

import kotlin.test.Test
import kotlin.test.assertNull

class HashmapBackedByListsTests : CustomMutableMapTestsParent() {
    override fun emptyCustomMutableMapStringInt(): CustomMutableMap<String, Int> = HashmapBackedByLists()

    override fun emptyCustomMutableMapCollidingStringInt(): CustomMutableMap<CollidingString, Int> = HashmapBackedByLists()

    @Test
    fun testPlaceholder() {
        // A placeholder so that this class has at least one test, because sometimes IDEs
        // behave strangely if all tests are in the superclass.
        assertNull(emptyCustomMutableMapStringInt()["a"])
    }
}
