/**
 * Represents a mapping of keys to values.
 * @param K the type of the keys
 * @param V the type of the values
 */
interface AssociativeArray<K, V> {
    /**
     * Insert the mapping from the key, [k], to the value, [v].
     * If the key already maps to a value, replace the mapping.
     */
    operator fun set(k: K, v: V)

    /**
     * @return true if [k] is a key in the associative array
     */
    operator fun contains(k: K): Boolean

    /**
     * @return the value associated with the key [k] or null if it doesn't exist
     */
    operator fun get(k: K): V?

    /**
     * Remove the key, [k], from the associative array
     * @param k the key to remove
     * @return true if the item was successfully removed and false if the element was not found
     */
    fun remove(k: K): Boolean

    /**
     * @return the number of elements stored in the hash table
     */
    fun size(): Int

    /**
     * @return the full list of key value pairs for the associative array
     */
    fun keyValuePairs(): List<Pair<K, V>>
}

/**
 * An implementation of AssociativeArray using separate chaining and division-based hashing.
 *
 * @param K the type of the keys
 * @param V the type of the values
 */

class ChainingHashTable<K, V> : AssociativeArray<K, V> {
    // Prime numbers are used to define the total capacity (the number of buckets) of the hash table's internal array
    // In the division-based hashing method, the exact array index for a key is calculated using the modulo operator against this capacity.
    // A list of good hash table primes:
    private val primes = intArrayOf(
        53, 97, 193, 389, 769, 1543, 3079, 6151, 12289,
        24593, 49157, 98317, 196613, 393241, 786433, 1572869,
        3145739, 6291469, 12582917, 25165843, 50331653,
        100663319, 201326611, 402653189, 805306457, 1610612741
    )
    //PrimeIndex: a pointer to track which prime number from the primes array is currently being used as the hash table's capacity.
    //Rehash: grab the next prime number from primes array to expand the table's capacity
    private var primeIndex = 0

    private var capacity = primes[primeIndex]

    // The hash table structure using an Array of MutableLists for separate chaining
    private var table = Array<MutableList<Pair<K, V>>?>(capacity) { null }

    private var numElements = 0

    // Threshold to trigger rehashing when the array gets too full
    private val loadFactorThreshold = 0.75

    /**
     * Division-based hash function mapping a key to an index within the table's capacity.
     */
    private fun hash(key: K): Int {
        // Retrieve the built-in integer hash code of the key
        val hashCode = key?.hashCode() ?: 0
        return Math.abs(hashCode) % capacity
    }

    /**
     * Rehashes the entire table by increasing the capacity to the next prime number
     * and redistributing all existing elements.
     */
    private fun rehash() {
        // Check if we haven't reached the end of the predefined primes array
        if (primeIndex < primes.size - 1) {
            primeIndex++
            capacity = primes[primeIndex]
        } else {
            // Fallback strategy if we exhaust the predefined prime list
            // Manually double the capacity and add 1 to keep the number odd
            capacity = capacity * 2 + 1
        }

        // Save a reference to the current data
        val oldTable = table
        // Create a new, empty array using the newly expanded prime capacity
        table = Array(capacity) { null }
        numElements = 0 // Reset size, elements will be re-added

        // Re-insert all key-value pairs into the new table
        // Loop through every single bucket in the old, smaller table
        for (bucket in oldTable) {
            if (bucket != null) {
                // Loop through every key-value pair stored in that specific chain
                for (pair in bucket) {
                    // Call the 'set' function to re-insert the pair.
                    // Because 'capacity' has changed, 'set' will calculate a new, better-distributed index for this pair in the new table.
                    this.set(pair.first, pair.second)
                }
            }
        }
    }

    override operator fun set(k: K, v: V) {
        // Trigger rehashing if the current load factor exceeds our threshold
        if (numElements.toDouble() / capacity > loadFactorThreshold) {
            rehash()
        }
        // Calculate the specific array index for this key using hash function
        val index = hash(k)

        // If this index has never been used, initialize a new, empty list
        if (table[index] == null) {
            table[index] = mutableListOf()
        }

        val bucket = table[index]!!

        // Check if the key already exists and update its value if found
        for (i in bucket.indices) {
            if (bucket[i].first == k) {
                bucket[i] = Pair(k, v)
                return
            }
        }

        // Otherwise, add the new mapping to the end of the chain
        bucket.add(Pair(k, v))
        numElements++
    }

    override operator fun contains(k: K): Boolean {
        // Find the designated bucket for this key
        val index = hash(k)
        // If the bucket is completely empty
        val bucket = table[index] ?: return false
        return bucket.any { it.first == k }
    }

    override operator fun get(k: K): V? {
        // Find the designated bucket for this key
        val index = hash(k)
        val bucket = table[index] ?: return null
        // If found, return its 'second' element (the value). If not found, return null.
        return bucket.find { it.first == k }?.second
    }

    override fun remove(k: K): Boolean {
        val index = hash(k)
        val bucket = table[index] ?: return false

        //iterator: safely deletes the current key-value pair from the underlying list while simultaneously updating its own internal pointers
        val iterator = bucket.iterator()
        while (iterator.hasNext()) {
            if (iterator.next().first == k) {
                iterator.remove()
                numElements--
                return true
            }
        }
        return false
    }

    override fun size(): Int {
        return numElements
    }

    override fun keyValuePairs(): List<Pair<K, V>> {
        // Create a single, flat master list to hold every pair.
        val list = mutableListOf<Pair<K, V>>()
        for (bucket in table) {
            // If the bucket contains a chain of data, dump all of its contents into the master list
            if (bucket != null) {
                list.addAll(bucket)
            }
        }
        return list
    }
}