interface LinkedList<T> {
    /**
     * Adds the element [data] to the front of the linked list.
     */
    fun pushFront(data: T)

    /**
     * Adds the element [data] to the back of the linked list.
     */
    fun pushBack(data: T)

    /**
     * Removes an element from the front of the list. If the list is empty, it is unchanged.
     * @return the value at the front of the list or nil if none exists
     */
    fun popFront(): T?

    /**
     * Removes an element from the back of the list. If the list is empty, it is unchanged.
     * @return the value at the back of the list or nil if none exists
     */
    fun popBack(): T?

    /**
     * @return the value at the front of the list or nil if none exists
     */
    fun peekFront(): T?

    /**
     * @return the value at the back of the list or nil if none exists
     */
    fun peekBack(): T?

    /**
     * @return true if the list is empty and false otherwise
     */
    fun isEmpty(): Boolean
}

class DoublyLinkedList<T> : LinkedList<T> {
    /**
     * Internal node class representing an element in the doubly linked list.
     *
     * @property data The value stored inside this node.
     * @property prev Reference to the preceding node, or null if this is the head.
     * @property next Reference to the succeeding node, or null if this is the tail.
     */
    private class Node<T>(
        var data: T,
        var prev: Node<T>? = null,
        var next: Node<T>? = null
    )

    // Reference to the very first node in the list
    private var head: Node<T>? = null
    // Reference to the very last node in the list
    private var tail: Node<T>? = null

    /**
     * Inserts [data] at the head of the list in O(1) time.
     *
     * @param data The element to insert to the list.
     */
    override fun pushFront(data: T) {
        //prev is null since the data will become the head
        val newNode = Node(data, prev = null, next = head)
        if (isEmpty()) {
            head = newNode
            tail = newNode
        } else {
            // Link current head backwards to the new node, then update the head pointer
            head?.prev = newNode
            head = newNode
        }
    }

    /**
     * Appends [data] to the tail of the list in O(1) time.
     *
     * @param data The element to append to the list.
     */
    override fun pushBack(data: T) {
        val newNode = Node(data, prev = tail, next = null)
        if (isEmpty()) {
            head = newNode
            tail = newNode
        } else {
            tail?.next = newNode
            tail = newNode
        }
    }

    /**
     * Removes and returns the element at the head of the list in O(1) time.
     *
     * @return The element that was removed, or null if the list is empty.
     */
    override fun popFront(): T? {
        if (isEmpty()) return null

        val value = head?.data
        if (head === tail) {
            // If only 1 element in the list, clear both pointers
            head = null
            tail = null
        } else {
            head = head?.next
            head?.prev = null
        }
        return value
    }

    /**
     * Removes and returns the element at the tail of the list in O(1) time.
     *
     * @return The element that was removed, or null if the list is empty.
     */
    override fun popBack(): T? {
        if (isEmpty()) return null

        val value = tail?.data
        if (head === tail) {
            head = null
            tail = null
        } else {
            tail = tail?.prev
            tail?.next = null
        }
        return value
    }

    /**
     * Returns the element at the front without removing it in O(1) time.
     *
     * @return The element at the front, or null if the list is empty.
     */
    override fun peekFront(): T? {
        return head?.data
    }

    /**
     * Returns the element at the back without removing it in O(1) time.
     *
     * @return The element at the back, or null if the list is empty.
     */
    override fun peekBack(): T? {
        return tail?.data
    }

    /**
     * Checks if the list has no elements.
     *
     * @return True if the list is empty, false otherwise.
     */
    override fun isEmpty(): Boolean {
        return head == null
    }
}

fun main() {
    val list = DoublyLinkedList<String>()

    println("Is empty initially: ${list.isEmpty()}") // true

    list.pushFront("Middle")
    list.pushFront("Front")
    list.pushBack("Back")

    println("Front: ${list.peekFront()}") // Front
    println("Back: ${list.peekBack()}")   // Back

    println("Popped front: ${list.popFront()}") // Front
    println("Popped back: ${list.popBack()}")   // Back
    println("Popped remaining: ${list.popFront()}") // Middle
    println("Is empty at end: ${list.isEmpty()}") // true
}