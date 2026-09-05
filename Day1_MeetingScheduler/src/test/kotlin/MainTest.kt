package scheduler

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class MainTest {

    fun assertConflict(expected: Boolean, meetings: List<Meeting>) {
        val pairwiseResult = hasConflictPairwise(meetings)
        val sortedResult = hasConflictSorted(meetings)

        if (expected) {
            assertTrue(pairwiseResult, "The first algorithm failed: expected conflict")
            assertTrue(sortedResult, "The second algorithm failed: expected conflict")
        } else {
            assertFalse(pairwiseResult, "The first algorithm failed: expected no conflict")
            assertFalse(sortedResult, "The second algorithm failed: expected mo conflict")
        }
    }

    @Test
    @DisplayName("Empty or single meeting has no conflict")
    fun testTrivialCases() {
        assertConflict(false, emptyList())
        assertConflict(false, listOf(Meeting(600, 660)))
    }

    @Test
    @DisplayName("Two consecutive meetings should not conflict")
    fun testBackToBackMeetings() {
        val meetings = listOf(
            Meeting(600, 660),
            Meeting(660, 690)
        )
        assertConflict(false, meetings)
    }

    @Test
    @DisplayName("Two meetings partially overlapping should conflict")
    fun testPartialOverlap() {
        val meetings = listOf(
            Meeting(600, 660),
            Meeting(645, 690)
        )
        assertConflict(true, meetings)
    }

    @Test
    @DisplayName("One meeting containing another should conflict")
    fun testCompleteEnclosure() {
        val meetings = listOf(
            Meeting(600, 720),
            Meeting(630, 660)
        )
        assertConflict(true, meetings)
    }

    @Test
    @DisplayName("Identical intervals should conflict")
    fun testIdenticalMeetings() {
        val meetings = listOf(
            Meeting(600, 660),
            Meeting(600, 660)
        )
        assertConflict(true, meetings)
    }

    @Test
    @DisplayName("Unsorted inputs should return correct conflict results")
    fun testUnsortedInputOrder() {
        val noConflict = listOf(
            Meeting(900, 960),
            Meeting(700, 800),
            Meeting(500, 600)
        )
        assertConflict(false, noConflict)

        val withConflict = listOf(
            Meeting(900, 960),
            Meeting(700, 850),
            Meeting(800, 900)
        )
        assertConflict(true, withConflict)
    }
}