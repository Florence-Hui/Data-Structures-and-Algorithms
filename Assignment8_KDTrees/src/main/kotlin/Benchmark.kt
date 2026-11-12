import kotlin.random.Random
import kotlin.time.Duration
import kotlin.time.measureTime

/**
 * Generates a list of random points.
 *
 * @param numPoints The number of points to generate.
 * @param k The dimensionality of each point.
 * @return A list of randomly generated [Point] objects.
 */
fun generateRandomPoints(numPoints: Int, k: Int): List<Point> {
    return List(numPoints) {
        Point(DoubleArray(k) { Random.nextDouble(0.0, 100.0) })
    }
}

/**
 * Benchmark KDTree against brute force nearest neighbor.
 * 1000 test points will be generated to test against the training points.
 *
 * @param k: the dimensionality of the dataset to create
 * @param numPoints: the number of points to use to match against (1000 test points)
 * @return the triple of [Duration] where the first specifies the time to build the tree,
 * the second specifies the time it takes to query the tree with 1,000 points,
 * the third is the time it takes to find the nearest neighbor to these points using the brute force approach.
 */
fun runExperiment(k: Int, numPoints: Int): Triple<Duration, Duration, Duration> {
    // Generate the training dataset and the 1000 test points
    val trainingPoints = generateRandomPoints(numPoints, k)
    val testPoints = generateRandomPoints(1000, k)

    // 1. Measure tree build time
    var kdTree: KDTree? = null
    val buildTime = measureTime {
        kdTree = KDTree(trainingPoints)
    }

    // 2. Measure KD-Tree query time for 1000 points
    val kdQueryTime = measureTime {
        for (testPoint in testPoints) {
            kdTree?.nearestNeighbor(testPoint)
        }
    }

    // 3. Measure brute force query time for 1000 points
    val bruteForce = BruteForceSearch()
    val bruteForceQueryTime = measureTime {
        for (testPoint in testPoints) {
            bruteForce.nearestNeighbor(trainingPoints, testPoint)
        }
    }

    return Triple(buildTime, kdQueryTime, bruteForceQueryTime)
}

fun main() {
    // Test various dimensions
    val dimensions = listOf(2, 3, 5, 10)

    // Allow numPoints to range over different orders of magnitude rather than a linear range[cite: 2]
    val pointCounts = listOf(10, 100, 1000, 10000, 100000)

    println(String.format("%-5s | %-12s | %-15s | %-15s | %-15s", "k", "numPoints", "Tree Build", "k-D Tree", "Brute Force"))
    println("-".repeat(75))

    // Nested loops to test various values of k and numPoints[cite: 2]
    for (k in dimensions) {
        for (numPoints in pointCounts) {
            val (buildTime, kdTime, bfTime) = runExperiment(k, numPoints)
            println(String.format("%-5d | %-12d | %-15s | %-15s | %-15s", k, numPoints, buildTime, kdTime, bfTime))
        }
    }
}