data class AlignmentResult(val alignedSeq1: String, val alignedSeq2: String, val score: Int)

/**
 * Needleman-Wunsch Algorithm for Global Sequence Alignment
 */
fun needlemanWunsch(seq1: String, seq2: String, match: Int = 1, mismatch: Int = -1, gap: Int = -1): AlignmentResult {
    val n = seq1.length
    val m = seq2.length

    // Initialize the Dynamic Programming matrix
    val dp = Array(n + 1) { IntArray(m + 1) }

    // Fill the first row and column with cumulative gap penalties
    for (i in 0..n) dp[i][0] = i * gap
    for (j in 0..m) dp[0][j] = j * gap

    // Fill the matrix using Dynamic Programming
    for (i in 1..n) {
        for (j in 1..m) {
            val matchScore = if (seq1[i - 1] == seq2[j - 1]) match else mismatch

            val diagonal = dp[i - 1][j - 1] + matchScore // Match or Mismatch
            val up = dp[i - 1][j] + gap // Gap in seq2
            val left = dp[i][j - 1] + gap // Gap in seq1

            dp[i][j] = maxOf(diagonal, up, left)
        }
    }

    // Traceback to reconstruct the alignment
    var i = n
    var j = m
    val align1 = java.lang.StringBuilder()
    val align2 = java.lang.StringBuilder()

    while (i > 0 || j > 0) {
        if (i > 0 && j > 0) {
            val matchScore = if (seq1[i - 1] == seq2[j - 1]) match else mismatch
            // Came from diagonal?
            if (dp[i][j] == dp[i - 1][j - 1] + matchScore) {
                align1.append(seq1[i - 1])
                align2.append(seq2[j - 1])
                i--
                j--
                continue
            }
        }
        // Came from above?
        if (i > 0 && dp[i][j] == dp[i - 1][j] + gap) {
            align1.append(seq1[i - 1])
            align2.append("-")
            i--
        }
        // Came from left?
        else {
            align1.append("-")
            align2.append(seq2[j - 1])
            j--
        }
    }

    // Traceback builds strings backwards, so reverse them at the end
    return AlignmentResult(
        align1.reverse().toString(),
        align2.reverse().toString(),
        dp[n][m]
    )
}

fun main() {
    val seq1 = genomeSnippet
    val seq2 = testAgainst
    val result = needlemanWunsch(seq1, seq2)

    println("Alignment Score: ${result.score}")
    println("Aligned Sequence 1: \n${result.alignedSeq1}")
    println("Aligned Sequence 2: \n${result.alignedSeq2}")
}