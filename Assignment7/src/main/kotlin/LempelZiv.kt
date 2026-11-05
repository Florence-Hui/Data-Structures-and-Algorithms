class LempelZiv {

    /**
     * Encodes the input string using the Lempel-Ziv algorithm and ChainingHashTable.
     * Lempel-Ziv: shrinks files by replacing repeated patterns of characters with single integer codes
     *
     * @param uncompressed The raw input string to compress.
     * @return A list of integer codes representing the compressed data.
     */
    fun encode(uncompressed: String): List<Int> {
        if (uncompressed.isEmpty()) return emptyList()

        // Initialize the dictionary with all standard 256 ASCII single characters
        val dictionary = ChainingHashTable<String, Int>()
        var dictSize = 256
        for (i in 0 until 256) {
            dictionary[i.toChar().toString()] = i
        }

        var w = "" //the current sequence of characters that the algorithm has already matched and found in the dictionary
        val result = mutableListOf<Int>()

        // Process each character in the input string
        // 'c': the next single character being read from the raw input text
        for (c in uncompressed) {
            val wc = w + c
            // If the sequence is already in our hash table, keep building it
            if (dictionary.contains(wc)) {
                w = wc
            } else {
                // Otherwise, output the code for the known sequence 'w'
                result.add(dictionary[w]!!)
                // Add this new sequence 'wc' to the dictionary
                dictionary[wc] = dictSize++
                // Reset 'w' to the current character
                w = c.toString()
            }
        }

        // Output the code for the final remaining sequence
        if (w.isNotEmpty()) {
            result.add(dictionary[w]!!)
        }

        return result
    }

    /**
     * Decodes the compressed list of integers back into the original string.
     *
     * @param compressed The list of integer codes.
     * @return The original uncompressed string.
     */
    fun decode(compressed: List<Int>): String {
        if (compressed.isEmpty()) return ""

        // Initialize the reverse dictionary mapping integers back to strings
        val dictionary = ChainingHashTable<Int, String>()
        var dictSize = 256
        for (i in 0 until 256) {
            dictionary[i] = i.toChar().toString()
        }

        // Read the first code and output its corresponding character
        var w = dictionary[compressed.first()]!!
        val result = StringBuilder(w)

        // Process the remaining integer codes
        for (i in 1 until compressed.size) {
            val k = compressed[i]

            // Determine the string representation of the current code
            val entry = when {
                dictionary.contains(k) -> dictionary[k]!!
                // Special case: the code points to a sequence we are currently building
                // when the text contains a tightly repeating pattern
                k == dictSize -> w + w[0]
                else -> throw IllegalArgumentException("Invalid compressed code: $k")
            }

            result.append(entry)

            // Add the new sequence back into the dictionary to mirror the encoding process
            dictionary[dictSize++] = w + entry[0]
            w = entry
        }

        return result.toString()
    }
}