/**
 * ``Graph`` represents a directed graph
 * @param VertexType the type that represents a vertex in the graph
 */
interface Graph<VertexType> {
    /**
     * @return the vertices in the graph
     */
    fun getVertices(): Set<VertexType>

    /**
     * Add an edge between [from] and [to] with edge weight [cost]
     */
    fun addEdge(from: VertexType, to: VertexType, cost: Double)

    /**
     * Get all the edges that begin at [from]
     * @return a map where each key represents a vertex connected to [from] and the value represents the edge weight.
     */
    fun getEdges(from: VertexType): Map<VertexType, Double>

    /**
     * Remove all edges and vertices from the graph
     */
    fun clear()
}

/**
 * An implementation of [Graph] using an adjacency list structure.
 *
 * This class uses a nested Map (`adjacencyMap`) to store vertices and their corresponding outgoing edges.
 *
 * @param VertexType The data type representing the vertices (nodes) in the graph.
 */
class AdjacencyListGraph<VertexType> : Graph<VertexType> {
    // Maps each vertex to a map of its neighbors and their corresponding edges
    private val adjacencyMap: MutableMap<VertexType, MutableMap<VertexType, Double>> = mutableMapOf()

    /**
     * Retrieves all unique vertices currently registered in the graph.
     *
     * @return A Set containing all vertices.
     */
    override fun getVertices(): Set<VertexType> {
        return adjacencyMap.keys
    }

    /**
     * Adds a directed edge between two vertices with a specified weight/cost.
     *
     * @param from The starting vertex of the edge.
     * @param to The destination vertex of the edge.
     * @param cost The weight or cost of traveling this edge.
     */
    override fun addEdge(from: VertexType, to: VertexType, cost: Double) {
        // Gets the inner map for 'from', or creates a new one if it doesn't exist.
        // getOrPut: A built-in kotlin function for mutable maps that takes a key and a lambda function
        // getOrPut then looks in the map for the key, returns the existing value if exists; or generate a new value if not
        adjacencyMap.getOrPut(from) { mutableMapOf() }[to] = cost

        // Ensure the 'to' node is registered in the graph layout
        adjacencyMap.getOrPut(to) { mutableMapOf() }
    }

    /**
     * Retrieves all outgoing edges and their weights for a specific vertex.
     *
     * @param from The vertex to find connections for.
     * @return A Map where each key is a connected destination vertex, and the value is the edge weight.
     *         Returns an empty map if the vertex has no outgoing edges or does not exist.
     */
    override fun getEdges(from: VertexType): Map<VertexType, Double> {
        // Return the map of neighbors, or an empty map if the vertex doesn't exist
        return adjacencyMap[from] ?: emptyMap()
    }

    /**
     * Clears the graph, removing all vertices and edges.
     */
    override fun clear() {
        adjacencyMap.clear()
    }
}