import unittest
from max_heap import ChildMaxHeap
from baseline_mcts import GridEnv, BaselineMCTS
from heap_mcts import HeapMCTS

class TestNode:
    """Mock node to test heap sorting logic independently of MCTS mechanics."""
    def __init__(self, name, score):
        self.name = name
        self.score = score
        self.heap_index = -1
        
    def ucb_score(self):
        return self.score

# Test for the data structure
class TestMaxHeap(unittest.TestCase):
    def test_insert_and_get_max(self):
        heap = ChildMaxHeap()
        heap.insert(TestNode("A", 10.5))
        heap.insert(TestNode("B", 25.0))
        heap.insert(TestNode("C", 5.2))
        
        # Max should be B (25.0)
        self.assertEqual(heap.get_max().name, "B")

    def test_update_node_shift_up(self):
        heap = ChildMaxHeap()
        node_c = TestNode("C", 5.2)
        heap.insert(TestNode("B", 25.0))
        heap.insert(node_c)
        
        # Simulating MCTS Backpropagation: A node receives a massive reward
        node_c.score = 50.0

        # Trigger the O(log n) repair. It should sift "C" up to the root.
        heap.update_node(node_c)
        self.assertEqual(heap.get_max().name, "C")

    def test_update_node_shift_down(self):
        heap = ChildMaxHeap()
        node_b = TestNode("B", 25.0)
        heap.insert(node_b)
        heap.insert(TestNode("A", 10.5))
        
        # Manually decrease B's score to trigger a shift down
        node_b.score = 2.0
        heap.update_node(node_b)
        self.assertEqual(heap.get_max().name, "A")

# Test for the MCTS algorithms
class TestMCTS(unittest.TestCase):
    def setUp(self):
        self.env = GridEnv(width=5, height=5, goal=(4, 4))
        self.start_state = (0, 0)
        
    def test_baseline_mcts_valid_move(self):
        # Run a small number of iterations to ensure the loop completes without crashing
        mcts = BaselineMCTS(self.env, iterations=50)
        best_move = mcts.search(self.start_state)
        valid_moves = self.env.get_valid_moves(self.start_state)
        
        # The returned move must be a legally valid step from the start state
        self.assertIn(best_move, valid_moves)

    def test_heap_mcts_valid_move(self):
        # Repeat the exact same check for the Heap version
        mcts = HeapMCTS(self.env, iterations=50)
        best_move = mcts.search(self.start_state)
        valid_moves = self.env.get_valid_moves(self.start_state)
        
        self.assertIn(best_move, valid_moves)

if __name__ == '__main__':
    unittest.main()