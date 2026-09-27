/**
 * Represents a 2D coordinate on the maze grid.
 *
 * @property row The vertical index (0-based, top-to-bottom).
 * @property col The horizontal index (0-based, left-to-right).
 */
data class Coordinate(val row: Int, val col: Int)

/**
 * A function that parses a maze, finds the shortest path, and prints the resulting path
 *
 * If a path is found, it will print the number of steps and the visual representation of the solved maze.
 * If no path is found, or if the start/end points are missing, it prints an appropriate error message.
 *
 * @param maze A list of strings representing the rows of the maze grid.
 */
fun solveMaze(maze: List<String>) {
    val graph = AdjacencyListGraph<Coordinate>()
    var start: Coordinate? = null
    var target: Coordinate? = null

    val rows = maze.size
    val cols = if (rows > 0) maze[0].length else 0

    // Step 1: Parse the maze and build the graph
    for (r in 0 until rows) {
        for (c in 0 until cols) {
            val char = maze[r][c]

            // Skip walls
            if (char == '#') continue

            val current = Coordinate(r, c)
            if (char == 'S') start = current
            if (char == 'E') target = current

            // Check all 4 adjacent directions (Up, Down, Left, Right)
            val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

            for ((dr, dc) in directions) {
                val neighborRow = r + dr
                val neighborCol = c + dc

                // If the neighbor is within bounds and is NOT a wall
                if (neighborRow in 0 until rows && neighborCol in 0 until cols && maze[neighborRow][neighborCol] != '#') {
                    val neighbor = Coordinate(neighborRow, neighborCol)
                    // Add a directed edge. (The reverse edge will be added when the loop reaches the neighbor)
                    graph.addEdge(current, neighbor, 1.0)
                }
            }
        }
    }

    if (start == null || target == null) {
        println("Error: Maze must contain a Start ('S') and an End ('E').")
        return
    }

    // Step 2: Run Dijkstra's algorithm
    val path = findShortestPath(graph, start, target)

    // Step 3: Print the results
    if (path != null) {
        println("Path found! It takes ${path.size - 1} steps.")
        printSolvedMaze(maze, path)
    } else {
        println("No valid path exists from Start to End.")
    }
}

fun printSolvedMaze(maze: List<String>, path: List<Coordinate>) {
    // Convert strings to mutable char arrays so we can modify them
    val solvedGrid = maze.map { it.toCharArray() }

    // Mark the path with '*'
    for (coord in path) {
        val currentChar = solvedGrid[coord.row][coord.col]
        if (currentChar != 'S' && currentChar != 'E') {
            solvedGrid[coord.row][coord.col] = '*'
        }
    }

    // Print the final grid
    println("\nSolved Maze:")
    for (row in solvedGrid) {
        println(row.joinToString(""))
    }
}

/**
 * Parses a text-based maze, converts it into a graph, and finds the shortest path
 * from the start to the end using Dijkstra's algorithm.
 *
 * The maze should be represented as a grid of characters where:
 * - 'S' represents the Starting position.
 * - 'E' represents the Ending (target) position.
 * - '#' represents a wall or impassable terrain.
 * - Any other character (e.g., '.') represents an open, walkable space.
 *
 * @param maze A list of strings representing the rows of the maze grid.
 * @return A list of [Coordinate] objects representing the sequential path from 'S' to 'E',
 *         or null if no valid path exists or if the maze is missing a start or target.
 */
fun getMazePath(maze: List<String>): List<Coordinate>? {
    val graph = AdjacencyListGraph<Coordinate>()
    var start: Coordinate? = null
    var target: Coordinate? = null

    val rows = maze.size
    val cols = if (rows > 0) maze[0].length else 0

    for (r in 0 until rows) {
        for (c in 0 until cols) {
            val char = maze[r][c]
            if (char == '#') continue

            val current = Coordinate(r, c)
            if (char == 'S') start = current
            if (char == 'E') target = current

            val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
            for ((dr, dc) in directions) {
                val neighborRow = r + dr
                val neighborCol = c + dc
                if (neighborRow in 0 until rows && neighborCol in 0 until cols && maze[neighborRow][neighborCol] != '#') {
                    graph.addEdge(current, Coordinate(neighborRow, neighborCol), 1.0)
                }
            }
        }
    }

    if (start == null || target == null) return null
    return findShortestPath(graph, start, target)
}
