import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ChainingHashTableTest {

    @Test
    fun testSetAndGet() {
        val hashTable = ChainingHashTable<String, Int>()
        hashTable["one"] = 1
        hashTable["two"] = 2

        assertEquals(1, hashTable["one"])
        assertEquals(2, hashTable["two"])
        assertNull(hashTable["three"]) // Key does not exist
    }

    @Test
    fun testUpdateExistingKey() {
        val hashTable = ChainingHashTable<String, String>()
        hashTable["key"] = "initial value"
        assertEquals("initial value", hashTable["key"])

        // Overwrite the existing key
        hashTable["key"] = "updated value"
        assertEquals("updated value", hashTable["key"])
        assertEquals(1, hashTable.size())
    }

    @Test
    fun testContains() {
        val hashTable = ChainingHashTable<Int, String>()
        hashTable[100] = "A"
        hashTable[200] = "B"

        assertTrue(100 in hashTable)
        assertTrue(hashTable.contains(200))
        assertFalse(300 in hashTable)
    }

    @Test
    fun testRemove() {
        val hashTable = ChainingHashTable<String, Double>()
        hashTable["pi"] = 3.14
        hashTable["e"] = 2.71

        assertTrue(hashTable.remove("pi"))
        assertFalse("pi" in hashTable)
        assertNull(hashTable["pi"])
        assertEquals(1, hashTable.size())

        // Attempting to remove a key that is already gone or never existed
        assertFalse(hashTable.remove("pi"))
        assertFalse(hashTable.remove("golden_ratio"))
    }

    @Test
    fun testSizeAndKeyValuePairs() {
        val hashTable = ChainingHashTable<String, Int>()
        assertEquals(0, hashTable.size())
        assertTrue(hashTable.keyValuePairs().isEmpty())

        hashTable["a"] = 1
        hashTable["b"] = 2
        hashTable["c"] = 3

        assertEquals(3, hashTable.size())

        val pairs = hashTable.keyValuePairs()
        assertEquals(3, pairs.size)
        assertTrue(pairs.contains(Pair("a", 1)))
        assertTrue(pairs.contains(Pair("b", 2)))
        assertTrue(pairs.contains(Pair("c", 3)))
    }

    @Test
    fun testRehashing() {
        val hashTable = ChainingHashTable<Int, Int>()

        // Insert enough elements to force the hash table to rehash multiple times.
        // The first prime capacity is 53. At a 0.75 load factor, it will rehash around 39 elements.
        for (i in 1..200) {
            hashTable[i] = i * 10
        }

        assertEquals(200, hashTable.size())

        // Ensure all elements are still accessible and properly mapped after resizing
        for (i in 1..200) {
            assertTrue(hashTable.contains(i))
            assertEquals(i * 10, hashTable[i])
        }
    }
}