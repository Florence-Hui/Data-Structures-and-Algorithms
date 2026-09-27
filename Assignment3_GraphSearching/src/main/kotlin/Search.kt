/**
 * Finds the shortest path from [start] to [target] in the given [graph].
 * Returns a list of vertices representing the path, or null if no path exists.
 */
fun <VertexType> findShortestPath(
    graph: Graph<VertexType>,
    start: VertexType,
    target: VertexType
): List<VertexType>? {

    // Tracks the shortest known distance to each vertex
    val distances = mutableMapOf<VertexType, Double>()

    // Tracks the optimal previous vertex to reconstruct the final path
    val previous = mutableMapOf<VertexType, VertexType>()

    val pq = MinHeapPriorityQueue<VertexType>()

    // Initialize the start vertex
    distances[start] = 0.0
    pq.addWithPriority(start, 0.0)

    while (!pq.isEmpty()) {
        // Get the closest vertex from the queue
        val current = pq.next() ?: break

        // If we reached our destination, reconstruct and return the path
        if (current == target) {
            // Using the sequence approach recommended in the assignment's Tips and Tricks
            return generateSequence(seed = target) { curr ->
                previous[curr]
            }.toList().asReversed()
        }

        val currentDist = distances[current] ?: Double.MAX_VALUE

        // Check all outgoing edges from the current vertex
        for ((neighbor, edgeWeight) in graph.getEdges(current)) {
            val newDist = currentDist + edgeWeight
            val knownDist = distances[neighbor] ?: Double.MAX_VALUE

            // If we found a strictly shorter path to this neighbor
            if (newDist < knownDist) {
                distances[neighbor] = newDist
                previous[neighbor] = current

                // addWithPriority handles both inserting new nodes and adjusting existing ones
                pq.addWithPriority(neighbor, newDist)
            }
        }
    }

    // If the queue empties and the target is still not reached, no path exists
    return null
}
