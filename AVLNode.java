package PhaseTwo;

public class AVLNode<T> {
    public int key;
    public T data;
    public AVLNode<T> left;
    public AVLNode<T> right;
    public int height;

    public AVLNode(int key, T data) {
        this.key = key;
        this.data = data;
        this.left = null;
        this.right = null;
        this.height = 1;
    }

    // Helper methods
    public boolean isLeaf() {
        return left == null && right == null;
    }

    public boolean hasLeft() {
        return left != null;
    }

    public boolean hasRight() {
        return right != null;
    }

    public boolean hasBothChildren() {
        return left != null && right != null;
    }

    @Override
    public String toString() {
        return "AVLNode{key=" + key + ", height=" + height + "}";
    }
}