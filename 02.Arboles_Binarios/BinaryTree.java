package arboles;

public sealed interface BinaryTree<T> permits BEmpty, BLeaf, BTree {
    default boolean isEmpty() {
        return this instanceof BEmpty;
    }
}

record BEmpty<T>() implements BinaryTree<T> {
    @Override
    public String toString() { return "Empty"; }
}

record BLeaf<T>(T label) implements BinaryTree<T> {
    @Override
    public String toString() { return "Leaf(" + label + ")"; }
}

record BTree<T>(T label, BinaryTree<T> left, BinaryTree<T> right) implements BinaryTree<T> {
    @Override
    public String toString() { return "Tree(" + label + ", " + left + ", " + right + ")"; }
}