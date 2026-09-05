import java.util.Scanner

fun solve(scanner: Scanner) {
    val n = scanner.nextInt()
    val a = IntArray(n) { scanner.nextInt() }

    var isSorted = true
    for (i in 0 until n - 1) {
        if (a[i] > a[i + 1]) {
            isSorted = false
            break
        }
    }

    if (isSorted) {
        println(n)
    } else {
        println(1)
    }
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