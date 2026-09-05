package scheduler

data class Meeting(val start: Int, val end: Int) {
    init {
        require(start < end) { "start must be strictly before end" }
    }

    fun overlapsWith(other: Meeting): Boolean {
        return maxOf(this.start, other.start) < minOf(this.end, other.end)
    }
}


fun hasConflictPairwise(meetings: List<Meeting>): Boolean {
    val n = meetings.size
    for (i in 0 until n) {
        for (j in i + 1 until n) {
            if (meetings[i].overlapsWith(meetings[j])) {
                return true
            }
        }
    }
    return false
}

fun hasConflictSorted(meetings: List<Meeting>): Boolean {
    if (meetings.size <= 1) return false

    val sorted = meetings.sortedBy { it.start }
    for (i in 0 until sorted.size - 1) {
        if (sorted[i + 1].start < sorted[i].end) {
            return true
        }
    }
    return false
}