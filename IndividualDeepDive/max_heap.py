class ChildMaxHeap:
    def __init__(self):
        # The internal array representing the binary tree
        self.heap = []

    def _swap(self, i, j):
        """Swaps two nodes and updates their internal heap_index."""
        self.heap[i], self.heap[j] = self.heap[j], self.heap[i]
        self.heap[i].heap_index = i
        self.heap[j].heap_index = j

    def insert(self, node):
        """Appends a new node to the end and bubbles it up."""
        node.heap_index = len(self.heap)
        self.heap.append(node)
        # Force the node to climb the tree until it stops being larger than its parent
        self._shift_up(node.heap_index)

    def get_max(self):
        """Returns the node with the highest UCB score"""
        return self.heap[0] if self.heap else None

    def _shift_up(self, idx):
        """Moves a node up the tree if its UCB score is greater than its parent's."""
        while idx > 0:
            parent_idx = (idx - 1) // 2
            
            # Max-Heap condition: child score > parent score
            if self.heap[idx].ucb_score() > self.heap[parent_idx].ucb_score():
                self._swap(idx, parent_idx)
                idx = parent_idx
            else:
                break

    def _shift_down(self, idx):
        """Moves a node down the tree if its UCB score is less than its children's."""
        size = len(self.heap)
        while True:
            largest = idx
            left = 2 * idx + 1
            right = 2 * idx + 2

            # Check if left child exists and is larger than current largest
            if left < size and self.heap[left].ucb_score() > self.heap[largest].ucb_score():
                largest = left
                
            # Check if right child exists and is larger than current largest
            if right < size and self.heap[right].ucb_score() > self.heap[largest].ucb_score():
                largest = right

            # If the largest is not the current node, swap and continue shifting down
            if largest != idx:
                self._swap(idx, largest)
                idx = largest
            else:
                break

    def update_node(self, node):
        """
        Restores heap order after a node's UCB score changes during backpropagation.
        This allows O(log n) updates instead of rebuilding the heap.
        """
        idx = node.heap_index
        if idx < 0 or idx >= len(self.heap):
            return
            
        # A node's UCB score might have gone up or down, so both are called
        # Only one of these will actually move the node based on the heap conditions.
        self._shift_up(idx)
        self._shift_down(idx)