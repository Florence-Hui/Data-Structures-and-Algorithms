# Assignment 5: Matrix Multiplication and Sequence Alignment

## Part 1 & 2: Matrix Multiplication Benchmarks

### Theoretical Background

Standard matrix multiplication operates with a time complexity of $O(N^3)$ using a traditional triple-nested loop approach. Strassen's algorithm optimizes this by dividing the matrices into quadrants and reducing the number of recursive multiplications from 8 to 7, resulting in a theoretical time complexity of $O(N^{\log_2 7}) \approx O(N^{2.81})$.

### Benchmark Results

| Matrix Size ($N$) | Standard $O(N^3)$ Time (ms) | Strassen $O(N^{2.81})$ Time (ms) | Faster Algorithm | 
| ----- | ----- | ----- | ----- | 
| 16 | 1 | 8 | Standard | 
| 32 | 3 | 42 | Standard | 
| 64 | 15 | 210 | Standard | 
| 128 | 80 | 180 | Standard | 
| 256 | 450 | 410 | Strassen | 
| 512 | 3800 | 2900 | Strassen | 


### Crossover Point Analysis

Based on the benchmark data, standard multiplication is significantly faster for smaller matrices. The memory overhead of creating sub-matrices heavily penalizes the Strassen implementation early on.

However, as the growth curve of $N^3$ begins to scale vertically, the efficiency of doing fewer multiplications allows Strassen's algorithm to catch up. On our testing hardware, the **crossover point occurs between** $N = 128$ **and** $N = 256$.

## Part 3: DNA Sequence Alignment

### Needleman-Wunsch Algorithm

The Needleman-Wunsch algorithm is a classic dynamic programming technique used to find the optimal global alignment between two biological sequences. The algorithm attempts to align the entire length of both sequences from end to end, inserting gaps as necessary to maximize the overall similarity score.

For this assignment, the algorithm is used to compare a snippet of a Salmonella genome against a target sequence to determine their exact alignment and calculate an optimal similarity score based on matches, mismatches, and gaps.

### Implementation Logic

The implementation breaks the algorithm down into three primary phases:

1. **Initialization and Scoring System:**
    Define a scoring system with a Match reward of `+1`, a Mismatch penalty of `-1`, and a Gap penalty of `-1`. A 2D matrix (grid) is initialized where the dimensions are `(Length of Sequence 1 + 1)` by `(Length of Sequence 2 + 1)`. The first row and first column are pre-filled with cumulative gap penalties (e.g., 0, -1, -2, -3) to represent the cost of aligning the beginning of either sequence against entirely blank spaces.

2. **Matrix Filling (Dynamic Programming):**
   The algorithm iterates through every remaining empty cell in the matrix. For each cell, it calculates the optimal score to reach that position by evaluating the three adjacent cells it could have come from:

    * **Diagonal:** Adding the match or mismatch score to the diagonal cell's value.

    * **Up:** Adding a gap penalty to the cell directly above it (representing a gap in sequence 2).

    * **Left:** Adding a gap penalty to the cell directly to its left (representing a gap in sequence 1).

   The maximum of these three calculated values is placed into the current cell. Once the matrix is full, the absolute highest possible score for the global alignment is located in the very bottom-right corner.

3. **Traceback:**
   To reconstruct the actual alignment, we start at the bottom-right corner and trace our path backward to the top-left (the `[0,0]` index). By reversing the mathematical operations used to fill the grid, the algorithm determines which direction yielded the current cell's score. Moving diagonally consumes a character from both sequences, while moving left or up inserts a gap (`-`) into the respective sequence. Because this process builds the strings backward, the final output sequences are reversed before being returned.

### Best Alignment

Alignment Score: 143
Aligned Sequence 1:
TGGCGACAACCG-TAGCGGAATATTTTCGCGACCAGGGAAAACGGGTCGTGCTT-TTTATCGATTC-CATGACCCGTTATGCGC-GTGCTTTGCGAGACGTGGCACTGG-CGTCGG-GAGAGCGTCCGG-CTCGTCGAGGTTATCCCGCC-TCCGTA-TTCGAT-AATTTGCCCCG-CTTGCTGGAACGCCCAGGG---GCGACCAGCGAGGGAAGCATTACTGCCTTTTATACGGTACTGCTGGAAAGCGAGGA-A-GAGGCGGACCCGATGGCGGATGAAATTCGCTCTA-TCCTTGACGGTCA-CCTGTATCTGA-GCAGAAAGCTGGCCGGGCA-GGGACATTACCCGGCAATCGATGTACT-GAAAAGCGTAAGCCGCGTTTTT
Aligned Sequence 2:
TGGCCACCA-CGATAGCAGAATTTTTTCGCGATAATGGAAAGCGAGTCGT-CTTGCTTGCCGACTCAC-TGACGCGTTATGC-CAGGGCCGCACGGGAAATCGCTCTGGCCGCCGGAGAGACCG--CGGTTTC-TGGAGAATAT-CCGCCAGGCGTATTTAG-TGCA-TTGCCACGACTT-TTAGAACGTAC-GGGAATGGGA-GA-AAAAGGCAGTATTACCGCATTTTATACGGTACTGGTGGAAGGCGATGATATGA--ATGAGCCGTTGGCGGATGAAGTCCGTTC-ACTGCTTGATGGACATATTGTA-CT-ATCCCGACGGCTTGCAGAG-AGGGGGCATTATCCTGCCATTGACGTGTTGGCAACGC-TCAGCCGCGTTTTT

### Question & Future Work

Currently, when evaluating the optimal path during the traceback phase, it is possible for a cell to have multiple valid directions (e.g., moving diagonally or moving left both yield the same optimal score). The current implementation resolves these "ties" using a strict priority rule (if/else statement).
I'm curious about the feasibility of splitting the traceback into multiple independent branches, and allow the algorithm to return a collection containing *all* equally optimal global alignments.