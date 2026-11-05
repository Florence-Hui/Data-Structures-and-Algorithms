# Lempel-Ziv (LZW) Compression Results

## Overview
For this project, I implemented the Lempel-Ziv data compression algorithm. I used the custom `ChainingHashTable` implementation of the `AssociativeArray` interface.

The algorithm relies on constant, rapid lookups to check if a sequence of characters already exists in the dictionary. By using an associative array with an efficient `hash()` function and a collision-handling chaining mechanism, the algorithm was able to compress and decode text in near O(N) time complexity relative to the input length.

## Compression Analysis
To test the effectiveness, I ran the encoder on three different types of input strings to observe how the compression ratio behaves depending on the data's length and repetitiveness.

### Test 1: Short, Unique Text
* **Input:** "ABABA" 
* **Result:** [65, 66, 256, 65]
* **Analysis:** This baseline test confirms the ChainingHashTable correctly stores and retrieves new sequences. However, for short strings with very little repetition, the actually *expands* the data size. The algorithm spends the entire time building the dictionary but rarely gets a chance to reuse the sequences it learns. The algorithm is not designed for tiny, randomized datasets.

### Test 2: Highly Repetitive Data
* **Input:** "ABABABABABABABABABABABABABABABABABABABAB"
* **Result:** The algorithm generated 10 integer codes.
* **Analysis:** Because the pattern "AB" repeats constantly, the associative array quickly maps "AB", "ABA", "ABAB", etc., to single integer codes. By the end of the string, a single integer code represents a massive chunk of characters. The compression ratio here was very high.

### Test 3: Standard Natural Language (Paragraph)
* **Input:** "Implement the Lempel-Ziv algorithm for lossless data compression There are a lot of resources for learning this"
* **Result:** The algorithm encoded list size strictly less than 111 codes
* **Analysis:** In standard English, common character pairs and prefixes appear frequently. Therefore, as the text goes on, the algorithm hits on longer learned sequences, significantly speeding up the compression, which resulted in a solid, moderate compression ratio.

