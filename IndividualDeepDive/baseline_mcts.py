import math
import random
import time

# Basic Node Structure
class BaselineNode:
    def __init__(self, state, parent=None):
        self.state = state # (x, y) coordinates
        self.parent = parent
        self.children = []
        self.visits = 0
        self.reward = 0.0

    # Calculates the upper confidence bound (UCB). If a node has never been visited, it returns infinity to force the algorithm to try it. Otherwise, it calculates using the below formula
    def ucb_score(self, exploration_weight=1.414):
        if self.visits == 0:
            return float('inf')
        parent_visits = self.parent.visits if self.parent else 1
        exploitation = self.reward / self.visits
        exploration = exploration_weight * math.sqrt(math.log(parent_visits) / self.visits)
        return exploitation + exploration

    # Checks if the node has at least one child
    def is_fully_expanded(self):
        # In our 2D grid, max possible moves from any spot is 4 (Up, Down, Left, Right)
        return len(self.children) > 0

# Simple 2D Grid Environment
class GridEnv:
    def __init__(self, width=8, height=8, goal=(7, 7)):
        self.width = width
        self.height = height
        self.goal = goal

    # Checks the current (x,y) coordinate and returns a list of adjacent coordinates so that it can prevent moves that would fall of the grid edges.
    def get_valid_moves(self, state):
        x, y = state
        moves = []
        if x > 0: moves.append((x - 1, y)) # Left
        if x < self.width - 1: moves.append((x + 1, y)) # Right
        if y > 0: moves.append((x, y - 1)) # Down
        if y < self.height - 1: moves.append((x, y + 1)) # Up
        return moves

    # Takes a starting position and takes random valid moves until it hits the goal or tuns out of max steps
    def simulate_random_rollout(self, state, max_steps=20):
        """Randomly walks until it hits the goal or runs out of steps."""
        curr_state = state
        for _ in range(max_steps):
            if curr_state == self.goal:
                return 100.0 
            moves = self.get_valid_moves(curr_state)
            curr_state = random.choice(moves)
        
        # If it doesn't find the goal, give a partial reward based on how close it got
        dist_to_goal = abs(curr_state[0] - self.goal[0]) + abs(curr_state[1] - self.goal[1])
        return -dist_to_goal 

# The Baseline MCTS Algorithm
class BaselineMCTS:
    def __init__(self, env, iterations=1000):
        self.env = env
        self.iterations = iterations

    def search(self, initial_state):
        root = BaselineNode(initial_state)

        for _ in range(self.iterations):
            # Selection
            node = root
            while node.is_fully_expanded() and node.state != self.env.goal:
                # Traverse down the tree and pick the child with the highest UCB score as long as the node has children and is not the goal state. This is the selection phase of MCTS.
                node = max(node.children, key=lambda c: c.ucb_score())

            # Expansion
            #Once it hits a node that hasn't explored all its children, it looks at all valid grid moves
            if node.state != self.env.goal:
                valid_moves = self.env.get_valid_moves(node.state)
                # Filter out moves we already expanded
                expanded_states = [c.state for c in node.children]
                unexpanded = [m for m in valid_moves if m not in expanded_states]
                
                if unexpanded:
                    # Pick one at random, creates a new BaselineNode for it, and add it to the tree.
                    move = random.choice(unexpanded)
                    new_node = BaselineNode(move, parent=node)
                    node.children.append(new_node)
                    node = new_node

            # Simulation
            # Call simulate_random_rollout to get a reward for the current node's state.
            reward = self.env.simulate_random_rollout(node.state)

            # Backpropagation
            # Takes the 'reward' from the simulation and propagates it back up the tree, updating the visits and rewards for each node along the path.
            curr = node
            while curr is not None:
                curr.visits += 1
                curr.reward += reward
                curr = curr.parent

        # Return the best move from the root (the child with the highest 'visits')
        best_child = max(root.children, key=lambda c: c.visits)
        return best_child.state

