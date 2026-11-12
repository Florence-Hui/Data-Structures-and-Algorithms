# Assignment 8: K-D Tree Benchmarking Report

## Experimental Data

The following table presents the benchmark results comparing the performance of our K-D Tree implementation against a Brute Force search. The tests were run using various dimensionalities ($k$) and dataset sizes (`numPoints`). The query time reflects the duration to find the nearest neighbor for 1,000 randomly generated test points against the built tree or dataset.

| $k$ | `numPoints` ($N$) | Tree Build Time | K-D Tree Query Time | Brute Force Query Time |
| :--- | :--- | :--- | :--- | :--- |
| **2** | 10 | 10.785 ms | 3.347 ms | 1.617 ms |
| **2** | 100 | 1.778 ms | 621.6 µs | 1.542 ms |
| **2** | 1,000 | 13.387 ms | 709.0 µs | 4.423 ms |
| **2** | 10,000 | 23.078 ms | 1.054 ms | 41.974 ms |
| **2** | 100,000 | 299.485 ms | 2.518 ms | 490.944 ms |
| **3** | 10 | 121.6 µs | 291.8 µs | 115.3 µs |
| **3** | 100 | 227.9 µs | 1.095 ms | 941.9 µs |
| **3** | 1,000 | 1.230 ms | 1.348 ms | 5.115 ms |
| **3** | 10,000 | 15.658 ms | 1.780 ms | 43.649 ms |
| **3** | 100,000 | 283.781 ms | 4.862 ms | 431.017 ms |
| **5** | 10 | 17.7 µs | 308.9 µs | 136.8 µs |
| **5** | 100 | 46.9 µs | 1.342 ms | 540.3 µs |
| **5** | 1,000 | 726.1 µs | 3.049 ms | 4.097 ms |
| **5** | 10,000 | 15.720 ms | 6.135 ms | 53.048 ms |
| **5** | 100,000 | 281.663 ms | 15.637 ms | 893.426 ms |
| **10** | 10 | 24.2 µs | 614.2 µs | 269.5 µs |
| **10** | 100 | 112.7 µs | 5.466 ms | 1.188 ms |
| **10** | 1,000 | 1.276 ms | 48.637 ms | 8.608 ms |
| **10** | 10,000 | 24.112 ms | 188.670 ms | 88.303 ms |
| **10** | 100,000 | 410.643 ms | 602.996 ms | 1.753 s |

## Analysis and Conclusion

Based on the benchmarking data,Based on the test results, K-D trees are much faster than a simple brute-force search in certain situations, but lose in others.

**Situations where K-D Trees are Better:**
* **Low Dimensionality combined with Large Datasets:** The K-D tree is fast in 2D and 3D spaces with high point counts. For instance, at $k=2$ and $N=100,000$, the K-D tree queries 1,000 points in just $\sim 2.5$ ms, compared to the brute force method taking $\sim 491$ ms. The reason why K-D tree stands out is that it rapidly shrinks the search area and safely ignores the vast majority of the data.

**Situations where Brute Force is Competitive or Superior:**
* **High Dimensionality (Large $k$):** The benchmark demonstrates the breakdown of the K-D tree's efficiency as dimensions increase. At $k=10$, the brute force method actually *outperforms* the K-D tree for $N=10$, $N=100$, $N=1,000$, and even $N=10,000$. The K-D tree only manages to overtake brute force at the massive $N=100,000$ mark (taking $\sim 603$ ms vs $\sim 1.75$ s).
* **Small Datasets (Small $N$):** Across almost all dimensions, for very small datasets (e.g., $N=10$), the overhead of navigating the K-D tree's nodes and performing boundary checks is slower than simply calculating the distance to all 10 points. This is because if the list is not massively huge, organizing it into a complex 10D tree is inefficient.

**Summary:**
Use a K-D tree when dealing with lower dimensions (like 2D maps or 3D spatial data) and large datasets where the upfront $O(N \log N)$ build time pays off. If you are dealing with very high-dimensional data (e.g., $k \ge 10$) and smaller dataset sizes, a simple brute force search is much easier to implement and will likely be significantly faster.