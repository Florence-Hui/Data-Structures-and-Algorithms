# MCTS Trajectory Planning: Max-Heap Optimization

## Project Summary
This project optimizes the Selection and Backpropagation phases of a Monte Carlo Tree Search (MCTS) algorithm used for autonomous trajectory planning. By replacing the standard list-based node storage with a custom Max-Heap, the algorithm reduces the time complexity of selecting the optimal next move from $O(n)$ to $O(1)$.

## Background: MCTS and Trajectory Planning
Trajectory planning requires autonomous vehicles to calculate optimal paths through an environment while minimizing distance. MCTS solves this by building a probabilistic decision tree. It simulates thousands of random paths (rollouts) and evaluates them using the Upper Confidence Bound (UCB) formula. The UCB score balances **exploitation** (favoring paths that historically yielded high rewards) and **exploration** (forcing the algorithm to test unvisited areas). 

### The Max-Heap Optimization
In a standard MCTS implementation, selecting the best child node to explore requires an $O(n)$ linear scan across all children to calculate and compare their UCB scores. As the branching factor (available valid moves) increases, this selection would become a bottleneck.

To solve this, the baseline array was replaced with a Max-Heap:
* **Selection:** The Max-Heap guarantees the node with the highest UCB score is always maintained at the root (index 0), reducing extraction time to $O(1)$.
* **Backpropagation:** As nodes are visited and rewards are updated, their UCB scores change, breaking the heap's sorting order. The custom heap implements $O(\log n)$ `_shift_up` and `_shift_down` methods to dynamically repair the tree structure during backpropagation without requiring a full rebuild.

## Repository Structure
* **`max_heap.py`**: The core data structure containing the `ChildMaxHeap` class, handling $O(1)$ max extraction and $O(\log n)$ dynamic re-sorting.
* **`baseline_mcts.py`**: The original algorithm using a standard Python list for $O(n)$ child selection, along with the `GridEnv` 2D map simulation.
* **`heap_mcts.py`**: The optimized algorithm using the `ChildMaxHeap` for $O(1)$ child selection and $O(\log n)$ backpropagation updates.
* **`benchmark_test.py`**: A scaling performance suite that runs both algorithms across multiple grid sizes (5x5, 8x8, 12x12) to measure runtime differences.
* **`test_mcts.py`**: A `unittest` suite verifying the integrity of the heap sorting logic and confirming both algorithms generate mathematically valid trajectories.

## Performance Benchmark Report

A scaling benchmark was conducted to compare the execution time of the Baseline MCTS ($O(n)$ selection) against the Heap-Optimized MCTS ($O(1)$ selection). Both algorithms were given 2,000 iterations from the starting state `(0, 0)` across three different grid dimensions. 


### Benchmark Results

| Grid Size | Goal Coordinate | Baseline Time | Heap-Opt Time | Best Move (Base / Heap) | Verdict |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **5x5** | (4, 4) | 0.0850s | 0.0582s | (1, 0) / (1, 0) | **Heap was 31.5% FASTER** |
| **8x8** | (7, 7) | 0.2501s | 0.1046s | (1, 0) / (1, 0) | **Heap was 58.2% FASTER** |
| **12x12** | (11, 11) | 0.7046s | 0.1105s | (1, 0) / (0, 1) | **Heap was 84.3% FASTER** |


The benchmark data highlights the fundamental efficiency of the optimized data structure as the problem space scales:

**1. Overcoming Heap Overhead at Small Scales**
While a Max-Heap introduces $O(\log n)$ computational overhead during the backpropagation phase (requiring active `_shift_up` and `_shift_down` operations to repair the tree), this data proves that the $O(1)$ selection advantage still outweighs that overhead. Even on the smallest 5x5 grid with a strictly limited branching factor of 4, bypassing the $O(n)$ linear scan yields a 31.5% speedup.

**2. Compounding Efficiency at Scale**
The most significant takeaway is the scaling curve. As the grid expands from 5x5 to 12x12, the search space grows exponentially, forcing the algorithm to build much deeper search trees. In the Baseline algorithm, the runtime increases from 0.085s to 0.704s as the $O(n)$ selection step is called thousands of times across deeper branches. The Heap-Optimized version remains more stable, resulting in a massive 84.3% speedup on the largest map. 
