import math
import random
import time
from baseline_mcts import GridEnv
from max_heap import ChildMaxHeap


# Heap-Optimized Node
class HeapNode:
    def __init__(self, state, parent=None):
        self.state = state
        self.parent = parent
        # Optimization: Instead of a flat list, every node manages its children 
        # using the custom binary Max-Heap for O(1) selection.
        self.children_heap = ChildMaxHeap()  
        self.visits = 0
        self.reward = 0.0
        self.heap_index = -1  

    def ucb_score(self, exploration_weight=1.414):
        if self.visits == 0:
            return float('inf')
        parent_visits = self.parent.visits if self.parent else 1
        exploitation = self.reward / self.visits
        exploration = exploration_weight * math.sqrt(math.log(parent_visits) / self.visits)
        return exploitation + exploration

    def is_fully_expanded(self, env):
        return len(self.children_heap.heap) == len(env.get_valid_moves(self.state))


# Heap-Optimized MCTS Algorithm
class HeapMCTS:
    def __init__(self, env, iterations=1000):
        self.env = env
        self.iterations = iterations

    def search(self, initial_state):
        root = HeapNode(initial_state)

        for _ in range(self.iterations):
            # Selection: Heap Extraction
            node = root
            while node.is_fully_expanded(self.env) and node.state != self.env.goal:
                # Instead of looping through all children (O(n)), it instantly grabs the highest UCB score at index 0 (O(1)).
                node = node.children_heap.get_max()

            # Expansion
            if node.state != self.env.goal:
                valid_moves = self.env.get_valid_moves(node.state)
                expanded_states = [c.state for c in node.children_heap.heap]
                unexpanded = [m for m in valid_moves if m not in expanded_states]
                
                if unexpanded:
                    move = random.choice(unexpanded)
                    new_node = HeapNode(move, parent=node)
                    # Insert appends the node and dynamically bubbles it up to the correct position
                    node.children_heap.insert(new_node)
                    node = new_node

            # Simulation
            reward = self.env.simulate_random_rollout(node.state)

            # 4. Backpropagation: O(log n) Heap Update
            curr = node
            while curr is not None:
                curr.visits += 1
                curr.reward += reward
                if curr.parent is not None:
                    # triggers shift_up and shift_down to instantly repair the sorting order  (O(log n))
                    curr.parent.children_heap.update_node(curr)
                curr = curr.parent

        # Return the best move from the root based on most visits
        best_child = max(root.children_heap.heap, key=lambda c: c.visits)
        return best_child.state


