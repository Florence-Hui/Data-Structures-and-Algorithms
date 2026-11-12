import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Represents a point in a k-dimensional space.
 *
 * @property coordinates An array of double values representing the point's position along each axis.
 */
data class Point(val coordinates: DoubleArray) {

    /**
     * Calculates the Euclidean distance between this point and another point.
     *
     * @param other The point to calculate the distance to.
     * @return The Euclidean distance as a Double.
     */
    fun distanceTo(other: Point): Double {
        var sum = 0.0
        for (i in coordinates.indices) {
            sum += (this.coordinates[i] - other.coordinates[i]).pow(2)
        }
        return sqrt(sum)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Point) return false
        return coordinates.contentEquals(other.coordinates)
    }

    override fun hashCode(): Int {
        return coordinates.contentHashCode()
    }
}

/**
 * Represents a single node within the K-D Tree.
 *
 * @property point The k-dimensional point stored at this node.
 * @property left The left child node, containing points that are less than this node's point on the splitting axis.
 * @property right The right child node, containing points that are greater than or equal to this node's point on the splitting axis.
 */
class KDNode(
    val point: Point,
    val left: KDNode?,
    val right: KDNode?
)

/**
 * A K-Dimensional Tree (K-D Tree) for efficient space partitioning and nearest neighbor search.
 *
 * @property points A list of [Point] objects used to build the tree.
 */
class KDTree(points: List<Point>) {
    private val root: KDNode?
    private val k: Int

    init {
        k = if (points.isNotEmpty()) points.first().coordinates.size else 0
        root = buildTree(points, 0)
    }

    /**
     * Recursively builds the K-D tree by finding the median point along the current axis
     * and splitting the dataset into left and right subtrees.
     *
     * @param points The current subset of points to partition.
     * @param depth The current depth in the tree, used to determine which axis to split on.
     * @return The constructed [KDNode] for the current subtree, or null if the point list is empty.
     */
    private fun buildTree(points: List<Point>, depth: Int): KDNode? {
        if (points.isEmpty()) return null

        val axis = depth % k
        // This sorts by the current axis.
        // For extra credit, you could replace this sort with Quickselect for O(n) median finding.
        val sortedPoints = points.sortedBy { it.coordinates[axis] }
        val medianIndex = sortedPoints.size / 2

        return KDNode(
            point = sortedPoints[medianIndex],
            left = buildTree(sortedPoints.subList(0, medianIndex), depth + 1),
            right = buildTree(sortedPoints.subList(medianIndex + 1, sortedPoints.size), depth + 1)
        )
    }
    /**
     * Finds the nearest neighbor to a given query point.
     *
     * @param query The k-dimensional [Point] to search for.
     * @return The closest [Point] found in the tree, or null if the tree is empty.
     */
    fun nearestNeighbor(query: Point): Point? {
        return nearestNeighbor(root, query, 0, null)?.point
    }

    /**
     * Recursively traverses the tree to find the nearest neighbor, omitting branches
     * that cannot possibly contain a closer point.
     *
     * @param node The current [KDNode] being evaluated.
     * @param query The target [Point].
     * @param depth The current depth in the tree.
     * @param bestSoFar The closest [KDNode] discovered so far in the traversal.
     * @return The best [KDNode] found in this subtree or globally so far.
     */
    private fun nearestNeighbor(
        node: KDNode?,
        query: Point,
        depth: Int,
        bestSoFar: KDNode?
    ): KDNode? {
        if (node == null) return bestSoFar

        // 1. Update the best-known point if the current node is closer
        var currentBest = bestSoFar
        if (currentBest == null || node.point.distanceTo(query) < currentBest.point.distanceTo(query)) {
            currentBest = node
        }
        // 2. Determine which child branch to search first based on the splitting axis
        val axis = depth % k
        val goesLeft = query.coordinates[axis] < node.point.coordinates[axis]

        val firstBranch = if (goesLeft) node.left else node.right
        val secondBranch = if (goesLeft) node.right else node.left

        // 3. Recursively search the most promising branch
        currentBest = nearestNeighbor(firstBranch, query, depth + 1, currentBest)

        // Check if the other branch could possibly contain a closer point
        // (check the perpendicular distance from the query point to the current node's splitting plane)
        val axisDistance = abs(query.coordinates[axis] - node.point.coordinates[axis])
        if (currentBest == null || axisDistance < currentBest.point.distanceTo(query)) {
            currentBest = nearestNeighbor(secondBranch, query, depth + 1, currentBest)
        }

        return currentBest
    }
}

/**
 * A baseline nearest neighbor search implementation that calculates the distance
 * to every single point in the dataset.
 */
class BruteForceSearch {
    /**
     * Iterates through all points to find the closest match to the query.
     *
     * @param points The full list of k-dimensional points.
     * @param query The target [Point] to match.
     * @return The closest [Point], or null if the input list is empty.
     */
    fun nearestNeighbor(points: List<Point>, query: Point): Point? {
        if (points.isEmpty()) return null

        var nearest = points[0]
        var minDistance = query.distanceTo(nearest)

        for (point in points) {
            val dist = query.distanceTo(point)
            if (dist < minDistance) {
                minDistance = dist
                nearest = point
            }
        }
        return nearest
    }
}