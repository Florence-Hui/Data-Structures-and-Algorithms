interface Stack<T> {
    /**
     * Add [data] to the top of the stack
     */
    fun push(data: T)

    /**
     * Remove the element at the top of the stack. If the stack is empty, it remains unchanged.
     * @return the value at the top of the stack or null if none exists
     */
    fun pop(): T?

    /**
     * @return the value on the top of the stack or null if none exists
     */
    fun peek(): T?

    /**
     * @return true if the stack is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

// Stack implementation wrapping DoublyLinkedList
class LinkedListStack<T> : Stack<T> {
    // Internal doubly linked list instance acting as the backing store
    private val list = DoublyLinkedList<T>()

    override fun push(data: T) = list.pushFront(data)

    override fun pop(): T? = list.popFront()

    override fun peek(): T? = list.peekFront()

    override fun isEmpty(): Boolean = list.isEmpty()
}