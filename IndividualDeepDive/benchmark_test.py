import time
from baseline_mcts import GridEnv, BaselineMCTS
from heap_mcts import HeapMCTS

def run_benchmark():
    # Define the different board sizes to test: (width, height, goal)
    board_configs = [
        (5, 5, (4, 4)),
        (8, 8, (7, 7)),
        (12, 12, (11, 11))
    ]
    
    iterations = 2000
    start_state = (0, 0)

    print(f"Running Scaling Benchmark: {iterations} Iterations per test\n")

    for width, height, goal in board_configs:
        env = GridEnv(width=width, height=height, goal=goal)
        print(f"Testing Grid Size: {width}x{height} | Goal: {goal}")
        
        # Test Baseline MCTS
        baseline_mcts = BaselineMCTS(env, iterations=iterations)
        start_time = time.time()
        baseline_best = baseline_mcts.search(start_state)
        baseline_duration = time.time() - start_time
        
        # Test Heap-Optimized MCTS
        heap_mcts = HeapMCTS(env, iterations=iterations)
        start_time = time.time()
        heap_best = heap_mcts.search(start_state)
        heap_duration = time.time() - start_time
        
        # Print Comparison for this size
        print(f"  Baseline Time: {baseline_duration:.4f}s | Best Move: {baseline_best}")
        print(f"  Heap-Opt Time: {heap_duration:.4f}s | Best Move: {heap_best}")
        
        if heap_duration < baseline_duration:
            speedup = ((baseline_duration - heap_duration) / baseline_duration) * 100
            print(f"  Result: Heap was {speedup:.1f}% FASTER\n")
        else:
            slowdown = ((heap_duration - baseline_duration) / baseline_duration) * 100
            print(f"  Result: Baseline was {slowdown:.1f}% FASTER\n")

if __name__ == "__main__":
    run_benchmark()