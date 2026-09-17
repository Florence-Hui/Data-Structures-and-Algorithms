import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class QueueTest {

    @Test
    fun testEmptyQueue() {
        val queue = LinkedListQueue<Int>()
        assertTrue(queue.isEmpty())
        assertNull(queue.peek())
        assertNull(queue.dequeue())
    }

    @Test
    fun testEnqueueAndPeek() {
        val queue = LinkedListQueue<String>()
        queue.enqueue("first")
        assertFalse(queue.isEmpty())
        assertEquals("first", queue.peek())

        queue.enqueue("second")
        // Peek should still show the earliest element
        assertEquals("first", queue.peek())
    }

    @Test
    fun testFIFOBehavior() {
        val queue = LinkedListQueue<Int>()
        queue.enqueue(1)
        queue.enqueue(2)
        queue.enqueue(3)

        // Dequeue must return elements in original arrival order: 1, 2, 3
        assertEquals(1, queue.dequeue())
        assertEquals(2, queue.dequeue())
        assertEquals(3, queue.dequeue())
        assertNull(queue.dequeue())
        assertTrue(queue.isEmpty())
    }
}
