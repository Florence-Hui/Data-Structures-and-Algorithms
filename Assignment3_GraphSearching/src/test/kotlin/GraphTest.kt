import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class GraphTest {

    @Test
    fun `new graph should be empty`() {
        val graph = AdjacencyListGraph<String>()

        assertTrue(graph.getVertices().isEmpty(), "Vertices should be empty initially")
        assertTrue(graph.getEdges("A").isEmpty(), "Edges for any vertex should be empty initially")
    }

    @Test
    fun `adding an edge should add both vertices to the graph`() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 5.0)

        val vertices = graph.getVertices()
        assertEquals(2, vertices.size)
        assertTrue(vertices.contains("A"))
        assertTrue(vertices.contains("B"))
    }

    @Test
    fun `getEdges should return correct neighbors and weights`() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 5.0)
        graph.addEdge("A", "C", 2.5)

        val edges = graph.getEdges("A")
        assertEquals(2, edges.size)
        assertEquals(5.0, edges["B"])
        assertEquals(2.5, edges["C"])
    }

    @Test
    fun `directed edges should not appear in reverse`() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 5.0)

        assertTrue(graph.getEdges("A").containsKey("B"))
        assertTrue(graph.getEdges("B").isEmpty(), "Node B should not have an edge back to A automatically")
    }

    @Test
    fun `adding an edge that already exists should update its cost`() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 5.0)
        graph.addEdge("A", "B", 10.0) // Update the cost

        val edges = graph.getEdges("A")
        assertEquals(1, edges.size)
        assertEquals(10.0, edges["B"], "The edge cost should be updated to 10.0")
    }

    @Test
    fun `clear should remove all vertices and edges`() {
        val graph = AdjacencyListGraph<String>()
        graph.addEdge("A", "B", 5.0)
        graph.addEdge("B", "C", 3.0)

        graph.clear()

        assertTrue(graph.getVertices().isEmpty())
        assertTrue(graph.getEdges("A").isEmpty())
    }
}