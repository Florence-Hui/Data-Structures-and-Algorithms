# Meeting Scheduler

---

## Algorithms & Asymptotic Runtime Analysis

### Algorithm 1: `hasConflictPairwise`

#### Mechanism
Iterates through all possible unique pairs of meetings $(i, j)$ where $0 \le i < j < n$ using nested loops, checking if `meetings[i].overlapsWith(meetings[j])`.

#### Runtime Growth with $n$
- **Total Comparisons:**
  $$\binom{n}{2} = \frac{n(n - 1)}{2} = \frac{1}{2}n^2 - \frac{1}{2}n$$
- **Best-Case Time Complexity:** $\mathcal{O}(1)$ if the first pair examined overlaps.
- **Average-Case Time Complexity:** $\mathbf{\Theta(n^2)}$. 
---

### Algorithm 2: `hasConflictSorted`

#### Mechanism
1. Sorts the meetings in ascending order by their start times using Kotlin's standard library sort (`sortedBy { it.start }`).
2. Performs a single linear scan over the sorted list, comparing each meeting $i$ to its immediate neighbor $i + 1$. A conflict exists if:
   $$sorted[i + 1].start < sorted[i].end$$

#### Runtime Growth with $n$
- **Sorting Phase:** $\mathbf{\Theta(n \log n)}$.
- **Scan Phase:** $\mathbf{\Theta(n)}$.
- **Total Time Complexity:**
  $$\Theta(n \log n) + \Theta(n) = \mathbf{\Theta(n \log n)}$$

---

## 3. Running Tests

### Test Coverage
The test suite validates:
- Empty schedule and single-meeting lists (no conflict).
- Meeting $A$ ends exactly when Meeting $B$ starts (no conflict).
- A long meeting completely containing a shorter one (conflict).
- Identical intervals (conflict).
- Meetings provided in unordered inputs
---

## 4. Reflection on Learning Kotlin

- Writing unit tests for both algorithms made me learn to use helper functions for the first time in Kotlin. Initially, I duplicated the same assertion statements across multiple test cases. By extracting this logic into a private helper function (assertConflict), I could eventually organize test logic in a much cleaner way.
- Working on runtime analysis forced me to review and solidify several foundational runtime analysis concepts that I had previously forgotten