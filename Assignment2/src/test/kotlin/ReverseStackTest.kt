import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReverseStackTest {

    @Test
    fun testReverseStackMultipleElements() {
        val stack = LinkedListStack<Int>()
        stack.push(1)
        stack.push(2)
        stack.push(3)

        // Stack currently has 3 at top, then 2, then 1 at bottom
        reverseStack(stack)

        // After reversing, top should be 1, then 2, then 3
        assertEquals(1, stack.pop())
        assertEquals(2, stack.pop())
        assertEquals(3, stack.pop())
        assertTrue(stack.isEmpty())
    }

    @Test
    fun testReverseEmptyStack() {
        val stack = LinkedListStack<String>()
        reverseStack(stack)
        assertTrue(stack.isEmpty())
    }

    @Test
    fun testReverseSingleElementStack() {
        val stack = LinkedListStack<Int>()
        stack.push(42)

        reverseStack(stack)

        assertEquals(42, stack.pop())
        assertTrue(stack.isEmpty())
    }
}
