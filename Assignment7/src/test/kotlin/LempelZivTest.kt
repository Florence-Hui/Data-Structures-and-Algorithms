import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LempelZivTest {

    private val lzw = LempelZiv()

    @Test
    fun testBasicEncodingAndDecoding() {
        val original = "ABABA"
        val encoded = lzw.encode(original)

        // Verifies the specific compression steps: A(65), B(66), AB(256), A(65)
        assertEquals(listOf(65, 66, 256, 65), encoded)

        val decoded = lzw.decode(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testLzwEdgeCase() {
        // "AAAA" forces the decoder to reconstruct a code it hasn't added yet (k == dictSize)
        val original = "AAAA"
        val encoded = lzw.encode(original)

        assertEquals(listOf(65, 256, 65), encoded)

        val decoded = lzw.decode(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testHighlyRepetitiveData() {
        // Test: Highly repetitive pattern
        val original = "ABABABABABABABABABABABABABABABABABABABAB"
        assertEquals(40, original.length)

        val encoded = lzw.encode(original)
        val decoded = lzw.decode(encoded)

        // Analysis: Because the pattern "AB" repeats constantly, the associative array quickly maps "AB", "ABA", "ABAB", etc., to single integer codes. By the end of the string, a single integer code represents a massive chunk of characters
        // The compression ratio is incredibly high
        assertTrue(encoded.size <= 15) // Will be around 10-12 codes

        // Ensure lossless data restoration
        assertEquals(original, decoded)
    }

    @Test
    fun testEmptyString() {
        val original = ""
        val encoded = lzw.encode(original)

        assertTrue(encoded.isEmpty())

        val decoded = lzw.decode(encoded)
        assertEquals(original, decoded)
    }

    @Test
    fun testLongTextCompression() {
        val original = "Implement the Lempel-Ziv algorithm for lossless data compression There are a lot of resources for learning this"
        val encoded = lzw.encode(original)
        val decoded = lzw.decode(encoded)

        // Ensure the decoded text perfectly matches the original
        assertEquals(original, decoded)

        // Verify that compression actually occurred (encoded list should be shorter than original string)
        assertTrue(encoded.size < original.length)
    }
}