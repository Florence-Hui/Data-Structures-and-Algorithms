/**
 * Given a stack return a copy of the original stack (i.e., a new stack with the same values as the original, stored in the same order as the original). Your method should create the new stack and fill it up with the same values that are stored in the original stack.
 *
 * ### Algorithm Overview:
 * Because a stack uses an opposite rule with a queue,
 * moving items between them alternates between reversing and maintaining relative order:
 * 1. **Stack [A B C] -> Queue**: Popping from stack into queue inverts element order.
 * 2. **Queue [A B C]-> Stack**: Dequeuing back into the stack inverts the order again, restoring original order.
 * 3. **Stack [C B A] -> Queue**
 * 4. **Queue -> Stack [A B C]**
 *
 * @param T The type of elements held in the stacks.
 * @param original The stack to be copied. Its contents will be preserved upon function exit.
 * @return A new [Stack] containing the same elements in the exact same order as [original].
 */
fun <T> copyStack(original: Stack<T>): Stack<T> {
    val copy = LinkedListStack<T>()
    val queue = LinkedListQueue<T>()

    // Step 1: Drain original stack into queue
    while (!original.isEmpty()) {
        original.pop()?.let { queue.enqueue(it) }
    }

    // Step 2: Drain queue back into original stack
    while (!queue.isEmpty()) {
        queue.dequeue()?.let { original.push(it) }
    }

    // Step 3: Drain original stack into queue again
    while (!original.isEmpty()) {
        original.pop()?.let { queue.enqueue(it) }
    }

    // Step 4: Dequeue into BOTH the original stack and the copy stack
    while (!queue.isEmpty()) {
        val item = queue.dequeue()
        if (item != null) {
            original.push(item)
            copy.push(item)
        }
    }

    return copy
}
