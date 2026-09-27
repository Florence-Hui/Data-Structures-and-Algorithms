/**
 * ``MinPriorityQueue`` maintains a priority queue where the lower
 *  the priority value, the sooner the element will be removed from
 *  the queue.
 *  @param T the representation of the items in the queue
 */
interface MinPriorityQueue<T> {
    /**
     * @return true if the queue is empty, false otherwise
     */
    fun isEmpty(): Boolean

    /**
     * Add [elem] with at level [priority]
     */
    fun addWithPriority(elem: T, priority: Double)

    /**
     * Get the next (highest priority) element and remove this element from the queue.
     * @return the next element in terms of priority.  If empty, return null.
     */
    fun next(): T?

    /**
     * Adjust the priority of the given element
     * @param elem whose priority should change
     * @param newPriority the priority to use for the element
     *   the lower the priority the earlier the element int
     *   the order.
     */
    fun adjustPriority(elem: T, newPriority: Double)
}

/**
 * An implementation of [MinPriorityQueue]
 *
 * @param T The data type of the elements stored in the queue.
 */
class MinHeapPriorityQueue<T> : MinPriorityQueue<T> {

    // A simple data class to bind an element to its current priority
    private class Node<T>(val element: T, var priority: Double)

    // The underlying array representation of the binary tree
    private val heap = mutableListOf<Node<T>>()

    // Tracks the exact array index of each element so we can find them instantly
    private val elementIndices = mutableMapOf<T, Int>()

    /**
     * Checks if the priority queue contains no elements.
     *
     * @return true if the queue is empty, false otherwise.
     */
    override fun isEmpty(): Boolean {
        return heap.isEmpty()
    }

    /**
     * Adds an element to the priority queue with the specified priority.
     * If the element already exists in the queue, its priority is updated instead.
     *
     * @param elem The element to add or update.
     * @param priority The priority score for the element (lower means higher priority).
     */
    override fun addWithPriority(elem: T, priority: Double) {
        // If it already exists, update its priority
        if (elem in elementIndices) {
            adjustPriority(elem, priority)
            return
        }

        val node = Node(elem, priority)
        heap.add(node)

        val index = heap.size - 1
        elementIndices[elem] = index

        // Restore the min-heap property by floating the new item up
        bubbleUp(index)
    }

    /**
     * Retrieves and removes the element with the lowest priority value.
     *
     * @return The element with the lowest priority value, or null if the queue is empty.
     */
    override fun next(): T? {
        if (heap.isEmpty()) return null

        val minElem = heap[0].element

        // Swap the minimum with the last element
        swap(0, heap.size - 1)

        // Remove the old root from the end of the list and our index map
        heap.removeAt(heap.size - 1)
        elementIndices.remove(minElem)

        // Restore the min-heap property by sinking the new root down
        if (heap.isNotEmpty()) {
            bubbleDown(0)
        }

        return minElem
    }

    /**
     * Updates the priority of an existing element in the queue.
     *
     * @param elem The element whose priority should be adjusted.
     * @param newPriority The new priority value to assign to the element.
     */
    override fun adjustPriority(elem: T, newPriority: Double) {
        val index = elementIndices[elem] ?: return
        val node = heap[index]
        val oldPriority = node.priority

        node.priority = newPriority

        // If the new priority is smaller, it might need to move up the tree
        if (newPriority < oldPriority) {
            bubbleUp(index)
        }
        // If the new priority is larger, it might need to move down the tree
        else {
            bubbleDown(index)
        }
    }

    /**
     * Moves an element up the binary tree until the min-heap property is restored.
     * This is called when a new element is added or an element's priority is decreased.
     *
     * @param index The current array index of the element to bubble up.
     */
    private fun bubbleUp(index: Int) {
        var curr = index
        while (curr > 0) {
            val parent = (curr - 1) / 2
            if (heap[curr].priority < heap[parent].priority) {
                swap(curr, parent)
                curr = parent
            } else {
                break
            }
        }
    }

    /**
     * Moves an element down the binary tree until the min-heap property is restored.
     * This is called when the root is removed or an element's priority is increased.
     *
     * @param index The current array index of the element to bubble down.
     */
    private fun bubbleDown(index: Int) {
        var curr = index
        val size = heap.size

        while (true) {
            val left = 2 * curr + 1
            val right = 2 * curr + 2
            var smallest = curr

            if (left < size && heap[left].priority < heap[smallest].priority) {
                smallest = left
            }
            if (right < size && heap[right].priority < heap[smallest].priority) {
                smallest = right
            }

            if (smallest != curr) {
                swap(curr, smallest)
                curr = smallest
            } else {
                break
            }
        }
    }

    /**
     * Swaps two elements in the internal heap array and updates their corresponding
     * indices in the lookup map.
     *
     * @param i The array index of the first element.
     * @param j The array index of the second element.
     */
    private fun swap(i: Int, j: Int) {
        val temp = heap[i]
        heap[i] = heap[j]
        heap[j] = temp

        elementIndices[heap[i].element] = i
        elementIndices[heap[j].element] = j
    }
}