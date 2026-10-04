

import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.measureTime

fun runBenchmarks() {
    val sizes = listOf(10, 100, 1000, 10000, 100000)

    val algorithms = mapOf<String, (MutableList<Int>) -> Unit>(
        "Insertion Sort" to ::insertionSort,
        "Selection Sort" to ::selectionSort,
        "Merge Sort" to ::mergeSort,
        "Quick Sort" to ::quickSort
    )

    for ((name, sortAlgorithm) in algorithms) {
        println("Benchmarking $name...")
        val runTimes = mutableListOf<Double>()

        for (size in sizes) {
            // Generate a list of random numbers.
            val x = (0 until size).map { Random.nextInt(100000) }.toMutableList()

            // Time the sorting algorithm
            val runTime = measureTime {sortAlgorithm(x)}
            runTimes.add(runTime.toDouble(DurationUnit.SECONDS))
        }

        // Print the accumulated results for this algorithm
        println("$name Runtimes are$runTimes\n")
    }
}

fun main() {
    runBenchmarks()
}