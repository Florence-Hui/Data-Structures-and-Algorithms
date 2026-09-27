import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MinPriorityQueueTest {

    @Test
    fun `new queue should be empty and return null on next`() {
        val pq = MinHeapPriorityQueue<String>()

        assertTrue(pq.isEmpty())
        assertNull(pq.next())
    }

    @Test
    fun `next should return elements in ascending order of priority`() {
        val pq = MinHeapPriorityQueue<String>()

        pq.addWithPriority("C", 3.0)
        pq.addWithPriority("A", 1.0)
        pq.addWithPriority("D", 4.0)
        pq.addWithPriority("B", 2.0)

        assertFalse(pq.isEmpty())
        assertEquals("A", pq.next())
        assertEquals("B", pq.next())
        assertEquals("C", pq.next())
        assertEquals("D", pq.next())
        assertTrue(pq.isEmpty())
    }

    @Test
    fun `adjustPriority lowering the value should move element to the front`() {
        val pq = MinHeapPriorityQueue<String>()

        pq.addWithPriority("A", 1.0)
        pq.addWithPriority("B", 5.0)
        pq.addWithPriority("C", 10.0)

        // Decrease B's priority so it is lower than A
        pq.adjustPriority("B", 0.5)

        assertEquals("B", pq.next(), "B should have bubbled up to the front")
        assertEquals("A", pq.next())
        assertEquals("C", pq.next())
    }

    @Test
    fun `adjustPriority increasing the value should move element back`() {
        val pq = MinHeapPriorityQueue<String>()

        pq.addWithPriority("A", 1.0)
        pq.addWithPriority("B", 5.0)
        pq.addWithPriority("C", 10.0)

        // Increase A's priority so it is higher than B and C
        pq.adjustPriority("A", 15.0)

        assertEquals("B", pq.next(), "B should now be the minimum")
        assertEquals("C", pq.next())
        assertEquals("A", pq.next(), "A should have bubbled down to the end")
    }

    @Test
    fun `addWithPriority on an existing element should update its priority`() {
        val pq = MinHeapPriorityQueue<String>()

        pq.addWithPriority("Target", 10.0)
        pq.addWithPriority("Other", 5.0)

        // Calling addWithPriority again on "Target" should trigger an update, not a duplicate
        pq.addWithPriority("Target", 2.0)

        assertEquals("Target", pq.next(), "Target should now be first")
        assertEquals("Other", pq.next())
        assertTrue(pq.isEmpty(), "Queue should be empty, confirming no duplicates were made")
    }
}
