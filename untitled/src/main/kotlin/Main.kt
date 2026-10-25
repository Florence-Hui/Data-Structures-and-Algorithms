class Matrix(val n: Int) {
    // Store the underlying data using an array of arrays structure.
    val data: Array<DoubleArray> = Array(n) { DoubleArray(n) }

    // Get values at specified row and column indices.
    operator fun get(row: Int, col: Int): Double {
        return data[row][col]
    }

    // Set values at specified row and column indices.
    operator fun set(row: Int, col: Int, value: Double) {
        data[row][col] = value
    }

    // Divide the matrix into four (n/2) x (n/2) matrices, which is needed for Strassen's algorithm.
    fun split(): Array<Matrix> {
        val newSize = n / 2
        val a11 = Matrix(newSize)
        val a12 = Matrix(newSize)
        val a21 = Matrix(newSize)
        val a22 = Matrix(newSize)

        for (i in 0 until newSize) {
            for (j in 0 until newSize) {
                a11[i, j] = this[i, j]
                a12[i, j] = this[i, j + newSize]
                a21[i, j] = this[i + newSize, j]
                a22[i, j] = this[i + newSize, j + newSize]
            }
        }
        return arrayOf(a11, a12, a21, a22)
    }

    // Overload operators (e.g., +) to make the matrix class especially nice to use.
    operator fun plus(other: Matrix): Matrix {
        val result = Matrix(n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                result[i, j] = this[i, j] + other[i, j]
            }
        }
        return result
    }

    operator fun minus(other: Matrix): Matrix {
        val result = Matrix(n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                result[i, j] = this[i, j] - other[i, j]
            }
        }
        return result
    }

    /**
     * Multiply [this] matrix by [other] using traditional matrix multiplication[cite: 3].
     * @return [this]*[other] if the dimensions are compatible and null otherwise[cite: 3].
     */
    fun multiply(other: Matrix): Matrix? {
        if (this.n != other.n) return null

        val result = Matrix(this.n)
        for (i in 0 until n) {
            for (j in 0 until n) {
                var sum = 0.0
                for (k in 0 until n) {
                    sum += this[i, k] * other[k, j]
                }
                result[i, j] = sum
            }
        }
        return result
    }

    /**
     * Multiply [this] matrix by [other] using Strassen's algorithm
     * @return [this]*[other] if the dimensions are compatible and null otherwise
     */
    fun strassenMultiply(other: Matrix): Matrix? {
        if (this.n != other.n) return null

        // Hybrid Approach Switch: Fallback to regular multiplication for smaller matrices
        // This threshold (e.g., 64) is what you will tune for your benchmark analysis.
        val hybridThreshold = 64
        if (this.n <= hybridThreshold) {
            return this.multiply(other)
        }

        val a = this.split()
        val b = other.split()

        val a11 = a[0]; val a12 = a[1]; val a21 = a[2]; val a22 = a[3]
        val b11 = b[0]; val b12 = b[1]; val b21 = b[2]; val b22 = b[3]

        val m1 = (a11 + a22).strassenMultiply(b11 + b22)!!
        val m2 = (a21 + a22).strassenMultiply(b11)!!
        val m3 = a11.strassenMultiply(b12 - b22)!!
        val m4 = a22.strassenMultiply(b21 - b11)!!
        val m5 = (a11 + a12).strassenMultiply(b22)!!
        val m6 = (a21 - a11).strassenMultiply(b11 + b12)!!
        val m7 = (a12 - a22).strassenMultiply(b21 + b22)!!

        val c11 = m1 + m4 - m5 + m7
        val c12 = m3 + m5
        val c21 = m2 + m4
        val c22 = m1 - m2 + m3 + m6

        val result = Matrix(this.n)
        val half = n / 2
        for (i in 0 until half) {
            for (j in 0 until half) {
                result[i, j] = c11[i, j]
                result[i, j + half] = c12[i, j]
                result[i + half, j] = c21[i, j]
                result[i + half, j + half] = c22[i, j]
            }
        }
        return result
    }
}