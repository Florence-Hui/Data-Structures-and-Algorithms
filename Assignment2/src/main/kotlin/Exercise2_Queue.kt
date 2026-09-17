interface Queue<T> {
    /**
     * Add [data] to the end of the queue.
     */
    fun enqueue(data: T)

    /**
     * Remove the element at the front of the queue. If the queue is empty, it remains unchanged.
     * @return the value at the front of the queue or null if none exists
     */
    fun dequeue(): T?

    /**
     * @return the value at the front of the queue or null if none exists
     */
    fun peek(): T?

    /**
     * @return true if the queue is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

// Queue implementation wrapping DoublyLinkedList
class LinkedListQueue<T> : Queue<T> {
    private val list = DoublyLinkedList<T>()

    override fun enqueue(data: T) = list.pushBack(data)

    override fun dequeue(): T? = list.popFront()

    override fun peek(): T? = list.peekFront()

    override fun isEmpty(): Boolean = list.isEmpty()
}