/**
 * Reverses the order of elements in the given [stack] in-place using a single auxiliary queue.
 *
 * ### Algorithm Overview:
 * 1. **Stack -> Queue**: Popping from the stack reverses the relative order of elements.
 * 2. **Queue -> Stack**: All elements are dequeued and pushed back into the stack.
 *    Elements are pushed back in the newly inverted order.
 *
 * @param T The type of elements held within the stack.
 * @param stack The stack to be reversed in-place. If empty or containing one element,
 *              its contents remain unchanged.
 */
fun <T> reverseStack(stack: Stack<T>) {
    val queue: Queue<T> = LinkedListQueue()

    // Drain stack into queue
    while (!stack.isEmpty()) {
        val item = stack.pop()
        if (item != null) {
            queue.enqueue(item)
        }
    }

    // Refill stack from queue
    while (!queue.isEmpty()) {
        val item = queue.dequeue()
        if (item != null) {
            stack.push(item)
        }
    }
}
