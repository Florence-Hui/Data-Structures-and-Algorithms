import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import kotlin.random.Random

class MatrixTests {

    /**
     * Helper function to compare two matrices cell-by-cell.
     */
    private fun assertMatrixEquals(expected: Matrix, actual: Matrix?, delta: Double = 0.0001) {
        assertNotNull(actual, "Result matrix should not be null")
        assertEquals(expected.size, actual!!.size, "Matrix sizes should match")

        for (i in 0 until expected.size) {
            for (j in 0 until expected.size) {
                assertEquals(expected[i, j], actual[i, j], delta, "Mismatch at row $i, col$j")
            }
        }
    }

    @Test
    fun `test standard multiplication with known 2x2 values`() {
        val a = Matrix(2)
        a[0, 0] = 1.0; a[0, 1] = 2.0
        a[1, 0] = 3.0; a[1, 1] = 4.0

        val b = Matrix(2)
        b[0, 0] = 2.0; b[0, 1] = 0.0
        b[1, 0] = 1.0; b[1, 1] = 2.0

        val expected = Matrix(2)
        expected[0, 0] = 4.0;  expected[0, 1] = 4.0
        expected[1, 0] = 10.0; expected[1, 1] = 8.0

        val result = a.multiply(b)
        assertMatrixEquals(expected, result)
    }

    @Test
    fun `test Strassen multiplication matches standard multiplication`() {
        // We use size 128 to ensure it exceeds the crossover point (N=64)
        // so the actual recursive Strassen logic is triggered.
        val size = 128
        val a = Matrix(size)
        val b = Matrix(size)

        // Fill with random values
        for (i in 0 until size) {
            for (j in 0 until size) {
                a[i, j] = Random.nextDouble(1.0, 10.0)
                b[i, j] = Random.nextDouble(1.0, 10.0)
            }
        }

        // Use standard multiplication as the source of truth
        val expectedResult = a.multiply(b)!!

        // Test Strassen against the known good result
        val strassenResult = a.strassenMultiply(b)

        assertMatrixEquals(expectedResult, strassenResult)
    }

    @Test
    fun `test identity matrix multiplication`() {
        val size = 4
        val matrix = Matrix(size)
        val identity = Matrix(size)

        for (i in 0 until size) {
            identity[i, i] = 1.0 // 1s on the diagonal
            for (j in 0 until size) {
                matrix[i, j] = (i + j).toDouble() // Arbitrary values
            }
        }

        val result = matrix.multiply(identity)
        assertMatrixEquals(matrix, result) // Multiplying by identity returns the original
    }

    @Test
    fun `test incompatible dimensions return null`() {
        val a = Matrix(2)
        val b = Matrix(4)

        assertNull(a.multiply(b), "Should return null for mismatched sizes")
        assertNull(a.strassenMultiply(b), "Should return null for mismatched sizes")
    }
}