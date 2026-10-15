import kotlin.system.measureTimeMillis
import kotlin.random.Random

fun main() {
    println("Matrix Size (N) | Standard (ms) | Strassen (ms)")

    // Test matrix sizes that are powers of 2 (e.g., 2, 4, 8, 16, 32, 64, 128, 256, 512)
    val sizes = listOf(16, 32, 64, 128, 256, 512)

    for (n in sizes) {
        val matrixA = generateRandomMatrix(n)
        val matrixB = generateRandomMatrix(n)

        matrixA.multiply(matrixB)

        // Benchmark Standard Multiplication
        val standardTime = measureTimeMillis {
            matrixA.multiply(matrixB)
        }

        // Benchmark Strassen's Algorithm
        val strassenTime = measureTimeMillis {
            matrixA.strassenMultiply(matrixB)
        }

        println("${n.toString().padEnd(15)} | ${standardTime.toString().padEnd(13)} | $strassenTime")
    }
}

// Generate a matrix filled with random numbers
fun generateRandomMatrix(size: Int): Matrix {
    val matrix = Matrix(size)
    for (i in 0 until size) {
        for (j in 0 until size) {
            matrix[i, j] = Random.nextDouble(1.0, 10.0)
        }
    }
    return matrix
}