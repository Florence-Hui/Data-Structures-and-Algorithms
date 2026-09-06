package solution
import java.util.Scanner

fun checkSortedResult(a: IntArray): Int {
    val n = a.size
    var isSorted = true
    for (i in 0 until n - 1) {
        if (a[i] > a[i + 1]) {
            isSorted = false
            break
        }
    }
    return if (isSorted) n else 1
}

fun solve(scanner: Scanner) {
    val n = scanner.nextInt()
    val a = IntArray(n) { scanner.nextInt() }
    val result = checkSortedResult(a)
    println(result)
}

fun main() {
    val scanner = Scanner(System.`in`)
    if (scanner.hasNextInt()) {
        val t = scanner.nextInt()
        repeat(t) {
            solve(scanner)
        }
    }
}