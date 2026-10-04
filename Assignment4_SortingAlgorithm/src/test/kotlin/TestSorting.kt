

fun main() {
    testInsertionSort()
    testMergeSort()
    println("All manual tests passed successfully!")
}

fun testInsertionSort() {
    val input = mutableListOf(5, 4, 3, 2, 1)
    val expected = listOf(1, 2, 3, 4, 5)

    insertionSort(input)

    // check() throws an error and stops the program if the condition is false
    check(input == expected) {
        "Insertion Sort Failed! Expected $expected but got $input"
    }
    println("✓ Insertion Sort passed")
}

fun testMergeSort() {
    val input = mutableListOf(3, 1, 4, 1, 5, 9, 2, 6)
    val expected = listOf(1, 1, 2, 3, 4, 5, 6, 9)

    mergeSort(input)

    check(input == expected) {
        "Merge Sort Failed! Expected $expected but got $input"
    }
    println("✓ Merge Sort passed")
}