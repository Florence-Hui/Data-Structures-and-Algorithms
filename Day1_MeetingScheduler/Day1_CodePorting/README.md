# C++ to Kotlin Migration: Array Sortedness Checker

The program evaluates test cases to determine whether an array of integers is sorted in non-decreasing order.

---

## Overview

The algorithm solves a simple decision and evaluation problem:
1. Reads the number of test cases $t$.
2. For each test case, ingests an integer array of size $n$.
3. Checks whether the array satisfies the non-decreasing condition: $a_i \le a_{i+1}$ for all $0 \le i < n - 1$.
4. **Output:**
    - Prints $n$ if the array is fully sorted.
    - Prints $1$ if there is at least one inversion ($a_i > a_{i+1}$).

--- 
## 5. Source Code Comparison

### Original C++
```cpp
#include <iostream>
#include <vector>

using namespace std;

void solve(){
    int n;
    cin >> n;
    vector<int> a(n);
    for (int i = 0; i < n; i++){
        cin >> a[i];
    }
    bool is_sorted = true;
    for (int i = 0; i < n - 1; i++){
        if (a[i] > a[i+1]){
            is_sorted = false;
            break;
        }
    }
    if (is_sorted){
        cout << n << endl;
    }else{
        cout << 1 << endl; 
    }
}

int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(NULL);

    int t;
    cin >> t;
    while (t--) {
        solve();
    }
    return 0;
}
```
### Ported Kotlin
```kotlin
import java.util.Scanner

fun solve(scanner: Scanner) {
    val n = scanner.nextInt()
    val a = IntArray(n) { scanner.nextInt() }

    var isSorted = true
    for (i in 0 until n - 1) {
        if (a[i] > a[i + 1]) {
            isSorted = false
            break
        }
    }

    if (isSorted) {
        println(n)
    } else {
        println(1)
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    if (scanner.hasNextInt()) {
        val t = scanner.nextInt()
        repeat(t) {
            solve(scanner)
        }
    }
}
```
---

## Porting Reflection
### The Good

- In Kotlin, `IntArray(n) { scanner.nextInt() }` allows allocating and populating the array in a single expressive line without manual index-tracking loops.
- Kotlin also enforces a strict model between immutable references and mutable variables. In this problem, only the state flag is mutable. I feel like this separation makes tracing variable and state transitions much easier than in C++
### The Bad
- In C++，I could use `ios_base::sync_with_stdio(false); cin.tie(NULL)` to optimize the running time. But Kotlin does not have (or I haven't found out) a direct equivalent to this configuration.

