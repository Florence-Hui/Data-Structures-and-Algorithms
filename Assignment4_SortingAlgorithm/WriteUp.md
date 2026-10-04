# Assignment 4: Sorting Algorithms

## 1. Sorting Algorithm Benchmarking
**How I Tested the Algorithms**

I generated lists of random integers ranging from 0 to 99,999. To see how each algorithm scales, I ran them through lists of vastly different sizes: 10, 100, 1,000, 10,000, and 100,000 elements. Because algorithms like Insertion and Selection sort can take an impractically long time on massive lists, the script was designed to skip them for the 100,000-element test

**Conclusions**

*   **Small Lists (10 to 100 items):** All four algorithms finished almost instantly. The difference is negligible
*   **Large Lists (10,000 to 100,000 items):** Selection Sort and Insertion Sort start taking noticeably longer because their time to finish grows exponentially as the list grows.

In conclusion, Merge Sort and Quick sort have overall better performance

## 2. Complexity Analysis
*   **Insertion Sort:** $O(n^2)$
    This complexity arises because the algorithm uses nested loops. In the worst-case scenario (a list in exact reverse order), for every new element added to the sorted portion, the algorithm must compare and shift it past every single previously sorted element. 
    
    This results in $1 + 2 + 3 + ... + (n-1)$ operations, leading to quadratic time growth.

*   **Selection Sort:** $O(n^2)$
    Selection Sort also relies on nested loops. For every position in the list, it scans the entire remaining unsorted section to find the absolute minimum value. Because it always checks every remaining element regardless of whether the list is already sorted, the number of comparisons is always quadratic.

*   **Merge Sort:** $O(n \log n)$
    Merge Sort consistently divides the list in half until it reaches single elements (which takes $\log n$ splitting steps). Then, it merges those sub-lists back together. At each of the $\log n$ levels of division, the merging process touches every element once, taking $n$ operations. Multiplying the levels by the operations per level gives $n \log n$.

*   **Quick Sort:** $O(n^2)$
    Quick Sort's worst-case time complexity is $O(n^2)$. This happens if the algorithm consistently chooses a terrible "pivot" (like the highest or lowest number in the list). When this occurs, it only peels off one item per recursive step. This forces it to make $n$ recursive passes, each doing linear work, resulting in quadratic time.

## 3. Extra Credit: New Frontiers in Sorting – IPS⁴o

**How I learned about this result:**
I read the original academic paper, "In-place Parallel Super Scalar Samplesort (IPS⁴o)"

**The Problem Setting:**
While computer science has established sorting algorithms with optimal $\mathcal{O}(n \log n)$ complexity, the default algorithms used in standard programming libraries are still variants of the Quicksort, since it does not require massive amounts of extra memory to sort large inputs. Newer, cache-efficient algorithms like super scalar samplesort ($s^3$-sort) process data significantly faster by avoiding costly CPU branch mispredictions, but they require $\mathcal{O}(n)$ additional space. Therefore, the challenge is to design an algorithm that is parallel, cache-efficient, while remaining strictly in-place.  

**How the Algorithm Works:**
Instead of copying the entire list into a massive second array to organize it, $IPS^4o$ uses tiny, temporary holding spaces. As the computer reads through the original list, it naturally leaves empty space behind it. Whenever one of those temporary holding spaces gets full, the computer dumps its contents directly into that available space in the main list.

Once all the data is grouped into chunks inside the main list, the chunks are usually still in the wrong order. The computer threads then safely swap these chunks around until they are in their correct final positions, managing to do all of this without asking for extra memory. Finally, if the list has a lot of duplicate items, the algorithm would toss them into "duplicate piles", which stops the computer's processor from stalling.

Phase 1 (Sampling): divide the data range into k buckets

Phase 2 (Classification): organize elements into small local memory buffers

Phase 3 (Block Permutation): calculate the exact global boundaries for each bucket, then systematically swap the blocks into their correct final positions

Phase 4 (Cleanup): process boundaries to ensure every single element is perfectly partitioned

**Why the Result is Significant:**
This algorithm demonstrates that it is possible to achieve the advanced hardware-level optimizations of samplesort without sacrificing the in-place memory constraint.