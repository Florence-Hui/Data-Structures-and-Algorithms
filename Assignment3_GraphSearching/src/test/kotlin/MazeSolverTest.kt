import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class MazeSolverTest {

    @Test
    fun `should find path in an open maze`() {
        val maze = listOf(
            "S..",
            ".#.",
            "..E"
        )

        val path = getMazePath(maze)

        assertNotNull(path, "Path should not be null for a solvable maze")
        assertEquals(5, path?.size, "Path should include Start, End, and 3 intermediate steps")
        assertEquals(Coordinate(0, 0), path?.first(), "Path should start at 'S'")
        assertEquals(Coordinate(2, 2), path?.last(), "Path should end at 'E'")
    }

    @Test
    fun `should return null for an unsolvable maze`() {
        val maze = listOf(
            "S.#",
            "###",
            "#.#",
            "..E"
        )

        val path = getMazePath(maze)

        assertNull(path, "Path should be null when a wall completely blocks the exit")
    }

    @Test
    fun `should return null if Start is missing`() {
        val maze = listOf(
            "...",
            ".#.",
            "..E"
        )

        val path = getMazePath(maze)

        assertNull(path, "Should handle missing Start gracefully")
    }

    @Test
    fun `should return null if End is missing`() {
        val maze = listOf(
            "S..",
            ".#.",
            "..."
        )

        val path = getMazePath(maze)

        assertNull(path, "Should handle missing End gracefully")
    }

    @Test
    fun `should find the shortest path when multiple paths exist`() {
        val maze = listOf(
            "S....",
            "####.",
            "E....",
        )

        // Path going all the way right, down, and all the way left
        val path = getMazePath(maze)

        assertNotNull(path)
        assertEquals(11, path?.size)
    }
}