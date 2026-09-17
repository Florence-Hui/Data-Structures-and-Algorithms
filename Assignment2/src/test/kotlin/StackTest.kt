import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class StackTest {

    @Test
    fun testEmptyStack() {
        val stack = LinkedListStack<Int>()
        assertTrue(stack.isEmpty())
        assertNull(stack.peek())
        assertNull(stack.pop())
    }

    @Test
    fun testPushAndPeek() {
        val stack = LinkedListStack<String>()
        stack.push("first")
        assertFalse(stack.isEmpty())
        assertEquals("first", stack.peek())

        stack.push("second")
        assertEquals("second", stack.peek())
    }

    @Test
    fun testLIFOBehavior() {
        val stack = LinkedListStack<Int>()
        stack.push(10)
        stack.push(20)
        stack.push(30)

        // Must pop in reverse order of push: 30, 20, 10
        assertEquals(30, stack.pop())
        assertEquals(20, stack.pop())
        assertEquals(10, stack.pop())
        assertNull(stack.pop())
        assertTrue(stack.isEmpty())
    }
}