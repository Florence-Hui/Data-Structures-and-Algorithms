package solution

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.util.Scanner

class MainTest {

    @Test
    @DisplayName("Strictly increasing array")
    fun testStrictlyIncreasing() {
        val input = intArrayOf(1, 2, 3, 4, 5)
        val expected = 5
        assertEquals(expected, checkSortedResult(input))
    }

    @Test
    @DisplayName("Array with duplicate adjacent elements")
    fun testNonDecreasingWithDuplicates() {
        val input = intArrayOf(1, 2, 2, 3, 3, 4)
        val expected = 6
        assertEquals(expected, checkSortedResult(input))
    }

    @Test
    @DisplayName("Array with all elements identical")
    fun testAllIdenticalElements() {
        val input = intArrayOf(7, 7, 7, 7)
        val expected = 4
        assertEquals(expected, checkSortedResult(input))
    }

    @Test
    @DisplayName("Unsorted at the beginning")
    fun testUnsortedAtStart() {
        val input = intArrayOf(10, 2, 3, 4, 5)
        val expected = 1
        assertEquals(expected, checkSortedResult(input))
    }

    @Test
    @DisplayName("Unsorted at the end")
    fun testUnsortedAtEnd() {
        val input = intArrayOf(1, 2, 3, 5, 4)
        val expected = 1
        assertEquals(expected, checkSortedResult(input))
    }

    @Test
    @DisplayName("Single-element array")
    fun testSingleElement() {
        val input = intArrayOf(42)
        val expected = 1
        assertEquals(expected, checkSortedResult(input))
    }

    @Test
    @DisplayName("Empty array")
    fun testEmptyArray() {
        val input = intArrayOf()
        val expected = 0
        assertEquals(expected, checkSortedResult(input))
    }

    @Test
    @DisplayName("Multi-line input Check")
    fun testEndToEndScanner() {
        val simulatedInput = """
            3
            4
            1 2 3 4
            3
            3 2 1
            2
            5 5
        """.trimIndent()

        val scanner = Scanner(simulatedInput)
        val t = scanner.nextInt()
        val results = mutableListOf<Int>()

        repeat(t) {
            val n = scanner.nextInt()
            val a = IntArray(n) { scanner.nextInt() }
            results.add(checkSortedResult(a))
        }

        assertEquals(listOf(4, 1, 2), results)
    }
}