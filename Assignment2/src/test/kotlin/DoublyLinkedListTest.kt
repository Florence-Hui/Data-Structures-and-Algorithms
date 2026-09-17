import datastructures.DoublyLinkedList

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse


class DoublyLinkedListTest {

    @Test
    fun testEmptyListBehavior() {
        val list = DoublyLinkedList<Int>()
        assertTrue(list.isEmpty())
        assertNull(list.peekFront())
        assertNull(list.peekBack())
        assertNull(list.popFront())
        assertNull(list.popBack())
    }

    @Test
    fun testPushFrontAndPopFront() {
        val list = DoublyLinkedList<String>()
        list.pushFront("A")
        list.pushFront("B")

        assertFalse(list.isEmpty())
        assertEquals("B", list.peekFront())
        assertEquals("A", list.peekBack())

        assertEquals("B", list.popFront())
        assertEquals("A", list.popFront())
        assertNull(list.popFront())
        assertTrue(list.isEmpty())
    }

    @Test
    fun testPushBackAndPopBack() {
        val list = DoublyLinkedList<Int>()
        list.pushBack(10)
        list.pushBack(20)

        assertFalse(list.isEmpty())
        assertEquals(10, list.peekFront())
        assertEquals(20, list.peekBack())

        assertEquals(20, list.popBack())
        assertEquals(10, list.popBack())
        assertNull(list.popBack())
        assertTrue(list.isEmpty())
    }

}

