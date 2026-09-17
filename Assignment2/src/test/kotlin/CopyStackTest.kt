import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertNotSame

class CopyStackTest {

    @Test
    fun testCopyStackNormal() {
        val original = LinkedListStack<Int>()
        original.push(10)
        original.push(20)
        original.push(30)

        val copy = copyStack(original)

        // Verify that original and copy are distinct objects
        assertNotSame(original, copy)

        // Verify both stacks have the same elements in the exact same order
        assertEquals(30, original.pop())
        assertEquals(20, original.pop())
        assertEquals(10, original.pop())
        assertTrue(original.isEmpty())

        assertEquals(30, copy.pop())
        assertEquals(20, copy.pop())
        assertEquals(10, copy.pop())
        assertTrue(copy.isEmpty())
    }

    @Test
    fun testCopyEmptyStack() {
        val emptyOriginal = LinkedListStack<String>()
        val copy = copyStack(emptyOriginal)

        assertTrue(emptyOriginal.isEmpty())
        assertTrue(copy.isEmpty())
    }
}