
import kotlin.random.Random
import kotlin.time.DurationUnit
import kotlin.time.measureTime


// 1. Insertion Sort
/**
 * Sorts a list by building a sorted section one element at a time.
 *
 * It looks at each item and shifts it to the left until it finds its proper
 * place among the items that have already been looked at and sorted.
 */
fun <T : Comparable<T>> insertionSort(list: MutableList<T>) {
    for (i in 1 until list.size) {
        val key = list[i]
        var j = i - 1
        // Shift elements to the right as long as they are larger than key
        while (j >= 0 && list[j] > key) {
            list[j + 1] = list[j]
            j--
        }
        //Dropping key
        list[j + 1] = key
    }
}


// 2. Selection Sort
/**
 * Sorts a list by repeatedly scanning for the smallest remaining element and
 * moving it to the front.
 */
fun <T : Comparable<T>> selectionSort(list: MutableList<T>) {
    val n = list.size
    for (i in 0 until n - 1) {
        // Assume the first unsorted element is the smallest
        var minIdx = i
        for (j in i + 1 until n) {
            // Scan the rest of the list to see if there is actually a smaller one
            if (list[j] < list[minIdx]) {
                minIdx = j //update the index for smallest
            }
        }
        //swap the smallest to the front
        if (minIdx != i) {
            val temp = list[i]
            list[i] = list[minIdx]
            list[minIdx] = temp
        }
    }
}


// 3. Merge Sort
/**
 * It continuously splits the list in half until every piece is just one element long.
 * Then, it merges those small pieces back together in the correct order to build the final sorted list.
 */
fun <T : Comparable<T>> mergeSort(list: MutableList<T>) {
    if (list.size <= 1) return
    val aux = ArrayList<T>(list)//create a temporary array
    mergeSortHelper(list, aux, 0, list.size - 1)
}

private fun <T : Comparable<T>> mergeSortHelper(
    list: MutableList<T>,
    aux: MutableList<T>,
    low: Int, //start boundary, low = 0 when the sort starts; moves forward when you pick an item from the left
    high: Int //end boundary
) {
    if (low >= high) return
    val mid = low + (high - low) / 2
    // Recursively sort the left half, right half
    mergeSortHelper(list, aux, low, mid)
    mergeSortHelper(list, aux, mid + 1, high)
    // Merging
    merge(list, aux, low, mid, high)
}

private fun <T : Comparable<T>> merge(
    list: MutableList<T>,
    aux: MutableList<T>,
    low: Int,
    mid: Int,
    high: Int
) {
    for (k in low..high) {
        aux[k] = list[k]
    }

    var i = low // Tracker for left half
    var j = mid + 1 // Tracker for right half

    // Go through every slot in the main list and pick the smallest available item from either the left or right half
    for (k in low..high) {
        when {
            // Those two lines are important!
            // Initially I ignored the lines, and the program crashed an out of boundary error.
            // Because when i steps past mid, it is pointing to invalid data and reading completely off the edge of the array
            // Therefore, when writing merge sort algorithm, it is important to add this kind of safety check for boundary
            i > mid -> list[k] = aux[j++] //Take from right when left half has run out of items
            j > high -> list[k] = aux[i++] //Take from left when right half has run out of items

            aux[j] < aux[i] -> list[k] = aux[j++] //when right item is smaller
            else -> list[k] = aux[i++] //when left item is smaller
        }
    }
}


// 4. Quick Sort
/**
 * Sorts a list by picking a random pivot element and organizing the rest of the list around it.
 */
fun <T : Comparable<T>> quickSort(list: MutableList<T>) {
    if (list.size <= 1) return
    quickSortHelper(list, 0, list.size - 1)
}

private fun <T : Comparable<T>> quickSortHelper(list: MutableList<T>, low: Int, high: Int) {
    if (low < high) {
        // Organize the list around a pivot
        val pIndex = randomizedPartition(list, low, high)

        // Recursively sort everything to the left of the pivot, then everything to the right
        quickSortHelper(list, low, pIndex - 1)
        quickSortHelper(list, pIndex + 1, high)
    }
}

private fun <T : Comparable<T>> randomizedPartition(list: MutableList<T>, low: Int, high: Int): Int {
    // Pick a random pivot
    val randomIndex = Random.nextInt(low, high + 1)
    // Swap pivot to the very end of the list
    val temp = list[high]
    list[high] = list[randomIndex]
    list[randomIndex] = temp
    return partition(list, low, high)
}

private fun <T : Comparable<T>> partition(list: MutableList<T>, low: Int, high: Int): Int {
    val pivot = list[high]
    var i = low - 1 //tracks the boundary of where the "smaller than pivot" items end
    for (j in low until high) {
        // If we find an item smaller than or equal to the pivot, we expand the "smaller" section
        // by increasing i and swapping the item into that smaller section
        if (list[j] <= pivot) {
            i++
            val temp = list[i]
            list[i] = list[j]
            list[j] = temp
        }
    }

    // place the pivot exactly in the middle
    val temp = list[i + 1]
    list[i + 1] = list[high]
    list[high] = temp
    return i + 1
}