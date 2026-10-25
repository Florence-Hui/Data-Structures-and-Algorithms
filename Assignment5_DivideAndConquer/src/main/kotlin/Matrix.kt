class Matrix(val size: Int) {
    // The matrix data is stored as an array of a 2D grid of Double values
    val data: Array<DoubleArray> = Array(size) { DoubleArray(size) }

    // Allows us to use matrix[row, col] to get a value
    operator fun get(row: Int, col: Int): Double {
        return data[row][col]
    }

    // Allows us to use matrix[row, col] = value to set a value
    operator fun set(row: Int, col: Int, value: Double) {
        data[row][col] = value
    }

    // Overloading the '+' operator
    operator fun plus(other: Matrix): Matrix {
        //Ensure not adding matrices of different sizes
        require(this.size == other.size) { "Matrices must be the same size to add." }
        val result = Matrix(this.size)
        for (i in 0 until size) {
            for (j in 0 until size) {
                result[i, j] = this[i, j] + other[i, j]
            }
        }
        return result
    }

    // Overloading the '-' operator
    operator fun minus(other: Matrix): Matrix {
        require(this.size == other.size) { "Matrices must be the same size to subtract." }
        val result = Matrix(this.size)
        for (i in 0 until size) {
            for (j in 0 until size) {
                result[i, j] = this[i, j] - other[i, j]
            }
        }
        return result
    }

    /**
     * SetUp for Strassen Multiplication:
     * Divides the matrix into four (size/2 x size/2) quadrants.
     * Returns a data class containing the four sub-matrices.
     */
    fun split(): Quadrants {
        val newSize = size / 2
        val c11 = Matrix(newSize) // Top-Left
        val c12 = Matrix(newSize) // Top-Right
        val c21 = Matrix(newSize) // Bottom-Left
        val c22 = Matrix(newSize) // Bottom-Right

        for (i in 0 until newSize) {
            for (j in 0 until newSize) {
                c11[i, j] = this[i, j] // Top-Left
                c12[i, j] = this[i, j + newSize] // Top-Right
                c21[i, j] = this[i + newSize, j] // Bottom-Left
                c22[i, j] = this[i + newSize, j + newSize] // Bottom-Right
            }
        }

        return Quadrants(c11, c12, c21, c22)
    }

    override fun toString(): String {
        return data.joinToString(separator = "\n") { row ->
            row.joinToString(prefix = "[", postfix = "]") { String.format("%.1f", it) }
        }
    }

    /**
     * Standard O(N^3) matrix multiplication.
     * @return [this]*[other] if the dimensions match, null otherwise
     */
    fun multiply(other: Matrix): Matrix? {
        // Two matrices can only be multiplied if their dimensions are compatible, otherwise should return null
        if (this.size != other.size) return null

        val result = Matrix(this.size)
        // Uses three nested loops (row, column, and a shared inner dimension k) to calculate the result
        for (i in 0 until size) {
            for (j in 0 until size) {
                var sum = 0.0
                // uses 'k' to pair up the individual numbers being multiplied together
                for (k in 0 until size) {
                    sum += this[i, k] * other[k, j]
                }
                result[i, j] = sum
            }
        }
        return result
    }

    /**
     * Strassen's algorithm for matrix multiplication.
     * Uses a hybrid approach: switches to standard multiplication for small matrices.
     * @return [this]*[other] if the dimensions match, null otherwise
     */
    fun strassenMultiply(other: Matrix): Matrix? {
        if (this.size != other.size) return null

        if (this.size <= 64) {
            return this.multiply(other)
        }

        // Split matrices into quadrants
        val a = this.split()
        val b = other.split()

        // Compute the 7 Strassen products
        val m1 = (a.a11 + a.a22).strassenMultiply(b.a11 + b.a22)!!
        val m2 = (a.a21 + a.a22).strassenMultiply(b.a11)!!
        val m3 = a.a11.strassenMultiply(b.a12 - b.a22)!!
        val m4 = a.a22.strassenMultiply(b.a21 - b.a11)!!
        val m5 = (a.a11 + a.a12).strassenMultiply(b.a22)!!
        val m6 = (a.a21 - a.a11).strassenMultiply(b.a11 + b.a12)!!
        val m7 = (a.a12 - a.a22).strassenMultiply(b.a21 + b.a22)!!

        // Combine M1-M7 into the new result quadrants
        val c11 = m1 + m4 - m5 + m7
        val c12 = m3 + m5
        val c21 = m2 + m4
        val c22 = m1 - m2 + m3 + m6

        // Reassemble the four quadrants into a single matrix
        return combine(c11, c12, c21, c22)
    }

    /**
     * Helper function to merge four (size/2 x size/2) quadrants into a full matrix.
     */
    private fun combine(c11: Matrix, c12: Matrix, c21: Matrix, c22: Matrix): Matrix {
        val halfSize = c11.size
        val result = Matrix(halfSize * 2) // Create a blank result matrix that is twice the size of one quadrant

        //Uses index shifting to push the data into the right zones
        for (i in 0 until halfSize) {
            for (j in 0 until halfSize) {
                result[i, j] = c11[i, j] // Top-Left: No need to shift
                result[i, j + halfSize] = c12[i, j] // Top-Right: Shift horizontally
                result[i + halfSize, j] = c21[i, j] // Bottom-Left: Shift vertically
                result[i + halfSize, j + halfSize] = c22[i, j] // Bottom-Right: Shift both horizontally and vertically
            }
        }
        return result
    }
}

data class Quadrants(val a11: Matrix, val a12: Matrix, val a21: Matrix, val a22: Matrix)