import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class SearchTest {

    @Test
    fun `should find path in a simple linear graph`() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 1.0)
        graph.addEdge("B", "C", 1.0)

        val path = findShortestPath(graph, "A", "C")

        assertNotNull(path)
        assertEquals(listOf("A", "B", "C"), path)
    }

    @Test
    fun `should choose the path with the lowest total cost, not fewest edges`() {
        val graph = AdjacencyListGraph<String>()
        // Path 1: 1 edge, but expensive (cost 10.0)
        graph.addEdge("A", "B", 10.0)

        // Path 2: 2 edges, but cheaper overall (cost 2.0 + 3.0 = 5.0)
        graph.addEdge("A", "C", 2.0)
        graph.addEdge("C", "B", 3.0)

        val path = findShortestPath(graph, "A", "B")

        assertNotNull(path)
        assertEquals(listOf("A", "C", "B"), path, "Should route through C for the lower cost")
    }

    @Test
    fun `should return null when target is unreachable`() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 1.0)
        graph.addEdge("C", "D", 1.0)
        // No connection between A/B and C/D

        val path = findShortestPath(graph, "A", "D")

        assertNull(path, "Path should be null when the target is in a disconnected component")
    }

    @Test
    fun `should return null when path goes wrong direction on directed edges`() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 1.0)
        graph.addEdge("B", "C", 1.0)

        // Try to go backwards from C to A
        val path = findShortestPath(graph, "C", "A")

        assertNull(path, "Path should be null because edges are directed A -> B -> C")
    }

    @Test
    fun `should return single node list when start equals target`() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 1.0)

        val path = findShortestPath(graph, "A", "A")

        assertNotNull(path)
        assertEquals(listOf("A"), path, "Path to itself should just be the start node")
    }
}