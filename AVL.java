package PhaseTwo;

public class AVL<T> {
    private AVLNode<T> root;

    public AVL() {
        root = null;
    }

    // === BASIC AVL OPERATIONS ===

    // Get height of node
    private int height(AVLNode<T> node) {
        return node == null ? 0 : node.height;
    }

    // Get balance factor
    private int getBalance(AVLNode<T> node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    // Right rotate
    private AVLNode<T> rightRotate(AVLNode<T> y) {
        AVLNode<T> x = y.left;
        AVLNode<T> T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        return x;
    }

    // Left rotate
    private AVLNode<T> leftRotate(AVLNode<T> x) {
        AVLNode<T> y = x.right;
        AVLNode<T> T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        return y;
    }

    // Insert method
    public void insert(int key, T data) {
        root = insertRec(root, key, data);
    }

    private AVLNode<T> insertRec(AVLNode<T> node, int key, T data) {
        // 1. Perform normal BST insertion
        if (node == null) {
            return new AVLNode<T>(key, data);
        }

        if (key < node.key) {
            node.left = insertRec(node.left, key, data);
        } else if (key > node.key) {
            node.right = insertRec(node.right, key, data);
        } else {
            // Duplicate keys not allowed, update data
            node.data = data;
            return node;
        }

        // 2. Update height of this ancestor node
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // 3. Get the balance factor
        int balance = getBalance(node);

        // 4. If unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && key < node.left.key) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && key > node.right.key) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && key > node.left.key) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && key < node.right.key) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    // Search method
    public boolean findkey(int key) {
        return findkeyRec(root, key);
    }

    private boolean findkeyRec(AVLNode<T> node, int key) {
        if (node == null) {
            return false;
        }

        if (key == node.key) {
            return true;
        } else if (key < node.key) {
            return findkeyRec(node.left, key);
        } else {
            return findkeyRec(node.right, key);
        }
    }

    // Get element by key 
    public T get(int key) {
        AVLNode<T> node = getRec(root, key);
        return node != null ? node.data : null;
    }

    private AVLNode<T> getRec(AVLNode<T> node, int key) {
        if (node == null) {
            return null;
        }

        if (key == node.key) {
            return node;
        } else if (key < node.key) {
            return getRec(node.left, key);
        } else {
            return getRec(node.right, key);
        }
    }

    public T retrieve() {
       return root != null ? root.data : null;
}

        
    }

    // Remove method
    public boolean removeKey(int key) {
        if (!findkey(key)) {
            return false;
        }
        root = removeRec(root, key);
        return true;
    }

    private AVLNode<T> removeRec(AVLNode<T> root, int key) {
        // STEP 1: PERFORM STANDARD BST DELETE
        if (root == null) {
            return root;
        }

        if (key < root.key) {
            root.left = removeRec(root.left, key);
        } else if (key > root.key) {
            root.right = removeRec(root.right, key);
        } else {
            // node with only one child or no child
            if ((root.left == null) || (root.right == null)) {
                AVLNode<T> temp = null;
                if (root.left != null) {
                    temp = root.left;
                } else {
                    temp = root.right;
                }

                // No child case
                if (temp == null) {
                    temp = root;
                    root = null;
                } else { // One child case
                    root = temp; // Copy the contents of the non-empty child
                }
            } else {
                // node with two children: Get the inorder successor
                AVLNode<T> temp = minValueNode(root.right);

                // Copy the inorder successor's data to this node
                root.key = temp.key;
                root.data = temp.data;

                // Delete the inorder successor
                root.right = removeRec(root.right, temp.key);
            }
        }

        // If the tree had only one node then return
        if (root == null) {
            return root;
        }

        // STEP 2: UPDATE HEIGHT OF THE CURRENT NODE
        root.height = Math.max(height(root.left), height(root.right)) + 1;

        // STEP 3: GET THE BALANCE FACTOR
        int balance = getBalance(root);

        // STEP 4: IF UNBALANCED, THEN THERE ARE 4 CASES

        // Left Left Case
        if (balance > 1 && getBalance(root.left) >= 0) {
            return rightRotate(root);
        }

        // Left Right Case
        if (balance > 1 && getBalance(root.left) < 0) {
            root.left = leftRotate(root.left);
            return rightRotate(root);
        }

        // Right Right Case
        if (balance < -1 && getBalance(root.right) <= 0) {
            return leftRotate(root);
        }

        // Right Left Case
        if (balance < -1 && getBalance(root.right) > 0) {
            root.right = rightRotate(root.right);
            return leftRotate(root);
        }

        return root;
    }

    private AVLNode<T> minValueNode(AVLNode<T> node) {
        AVLNode<T> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // === NEW METHODS FOR PHASE II REQUIREMENTS ===

    // 1. Get all elements in sorted order (in-order traversal)
    public LinkedList<T> getAllElementsInOrder() {
        LinkedList<T> result = new LinkedList<>();
        inOrderTraversal(root, result);
        return result;
    }

    private void inOrderTraversal(AVLNode<T> node, LinkedList<T> result) {
        if (node == null) return;
        inOrderTraversal(node.left, result);
        result.insert(node.data);
        inOrderTraversal(node.right, result);
    }

    // 2. Range query - get all elements with keys between minKey and maxKey
    public LinkedList<T> getRange(int minKey, int maxKey) {
        LinkedList<T> result = new LinkedList<>();
        getRange(root, minKey, maxKey, result);
        return result;
    }

    private void getRange(AVLNode<T> node, int minKey, int maxKey, LinkedList<T> result) {
        if (node == null) return;
        
        // If current node's key is greater than minKey, search left subtree
        if (node.key > minKey) {
            getRange(node.left, minKey, maxKey, result);
        }
        
        // If current node's key is in range, add to result
        if (node.key >= minKey && node.key <= maxKey) {
            result.insert(node.data);
        }
        
        // If current node's key is less than maxKey, search right subtree
        if (node.key < maxKey) {
            getRange(node.right, minKey, maxKey, result);
        }
    }

    // 3. Get all keys in sorted order 
    public LinkedList<Integer> getAllKeysInOrder() {
        LinkedList<Integer> result = new LinkedList<>();
        getKeysInOrder(root, result);
        return result;
    }

    private void getKeysInOrder(AVLNode<T> node, LinkedList<Integer> result) {
        if (node == null) return;
        getKeysInOrder(node.left, result);
        result.insert(node.key);
        getKeysInOrder(node.right, result);
    }

    // 4. Check if tree is empty
    public boolean isEmpty() {
        return root == null;
    }

    // 5. Get size of tree
    public int size() {
        return sizeRec(root);
    }

    private int sizeRec(AVLNode<T> node) {
        if (node == null) return 0;
        return 1 + sizeRec(node.left) + sizeRec(node.right);
    }

    // 6. Get the root height (for debugging)
    public int getRootHeight() {
        return height(root);
    }

    // === BACKWARD COMPATIBILITY METHODS ===
    
    // For backward compatibility 
    public LinkedList<T> getAllElements() {
        return getAllElementsInOrder();
    }

    // Display tree structure 
    public void display() {
        displayRec(root, 0);
    }

    private void displayRec(AVLNode<T> node, int level) {
        if (node == null) return;
        
        displayRec(node.right, level + 1);
        
        for (int i = 0; i < level; i++) {
            System.out.print("   ");
        }
        System.out.println(node.key + "(" + node.height + ")");
        
        displayRec(node.left, level + 1);
    }
    public AVLNode<T> getRoot() {
        return root;
    }
    
    

}

