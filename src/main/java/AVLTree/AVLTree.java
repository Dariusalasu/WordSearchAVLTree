package AVLTree;

import java.util.Stack;

public class AVLTree<K> {
    private Node<K> head;

    public AVLTree() {
        this.head = null;
    }

    public AVLTree(Node<K> head) {
        this.head = head;
    }

    public Node<K> getHead() { return head; }

    public void insert(K key, Data data) {
        try {
            Node<K> prevNode = null;
            Node<K> curNode = this.head;

            Stack<Node<K>> mStack = new Stack<>();

            // Loop and find position to insert
            while (curNode != null) {
                mStack.push(curNode); // Push curNode to stack to update height later
                int result = curNode.compareTo(key); // Compare key

                if (result == 0) return; // Cannot insert duplicate key

                prevNode = curNode;
                if (result > 0) curNode = curNode.getLeftNode();
                else curNode = curNode.getRightNode();
            }

            Node<K> newNode = new Node<K>(key, data); // Create new node with key

            // Assign new node in tree
            if (prevNode != null) {
                int result = prevNode.compareTo(key);
                if (result > 0) prevNode.setLeftNode(newNode);
                else prevNode.setRightNode(newNode);
            } else {
                this.head = newNode;
            }

            while (!mStack.isEmpty()) {
                Node<K> curStackNode = mStack.pop();
                Node<K> parentNode = null;
                if (!mStack.isEmpty()) parentNode = mStack.peek();

                int heightLeft = getHeight(curStackNode.getLeftNode());
                int heightRight = getHeight(curStackNode.getRightNode());
                int maxHeight = Math.max(heightLeft, heightRight) + 1;
                curStackNode.setHeight(maxHeight);

                if (checkRotation(heightRight, heightLeft)) {
                    // curStackNode.getRightNode is assumed to not be null because of checkRotation
                    // heightRight cannot be > heightLeft if right node is null
                    if (curStackNode.getRightNode().getRightNode() == null) {
                        // Need to rotate right first
                        rotateRight(curStackNode, curStackNode.getRightNode());
                    }
                    // Rotate Left
                    rotateLeft(parentNode, curStackNode);
                } else if (checkRotation(heightLeft, heightRight)) {
                    // curStackNode.getLeftNode is assumed to not be null because of checkRotation
                    // heightRight cannot be > heightLeft if left node is null
                    if (curStackNode.getLeftNode().getLeftNode() == null) {
                        // Need to rotate left first
                        rotateLeft(curStackNode, curStackNode.getLeftNode());
                    }
                    // Rotate Right
                    rotateRight(parentNode, curStackNode);
                }
            }
        }catch(Exception e) {
            System.out.println("Error message: "+e.getMessage());
        }
    }

    private boolean checkRotation(int big, int small) { return big-small > 1; }
    private int getHeight(Node<K> node) {
        if(node == null) return -1;
        return node.getHeight();
    }

    // Rotate right where A is parent and B is node
    //       P          P
    //      ...        ...
    //       |          |
    //       A          B
    //      /          / \
    //     B    ->    C   A
    //    /
    //   C
    private void rotateRight(Node<K> parent, Node<K> node) {
        if(node == null) return; // Return on invalid parameters
//        System.out.println("Rotating right");

        // Rotate nodes on tree
        Node<K> child = node.getLeftNode();
        node.setLeftNode(child.getRightNode());
        child.setRightNode(node);

        // Fix heights
//        int nodeHL = getHeight(node.getLeftNode()), nodeHR = getHeight(node.getRightNode());
//        node.setHeight(1+Math.max(nodeHL, nodeHR));
//        int childHL = getHeight(child.getLeftNode()), childHR = getHeight(child.getRightNode());
//        child.setHeight(1+Math.max(childHL, childHR));
        fixHeights(node);
        fixHeights(child);

        // Assign parent node
        try {
            if (parent == null) this.head = child;
            else if (parent.compareTo(child.getKey()) > 0) parent.setLeftNode(child);
            else parent.setRightNode(child);
        }catch(Exception e) {
            System.out.println("Error message: "+e.getMessage());
        }
    }

    // Rotate left where A is node
    //   P              P
    //  ...            ...
    //   |              |
    //   A              B
    //    \            / \
    //     B    ->    A   C
    //      \
    //       C
    private void rotateLeft(Node<K> parent, Node<K> node) {
        if(node == null) return; // Return on invalid parameters

        // Rotate nodes on tree
        Node<K> child = node.getRightNode();
        node.setRightNode(child.getLeftNode());
        child.setLeftNode(node);

        // Fix heights
//        int nodeHL = getHeight(node.getLeftNode()), nodeHR = getHeight(node.getRightNode());
//        node.setHeight(1+Math.max(nodeHL, nodeHR));
//        int childHL = getHeight(child.getLeftNode()), childHR = getHeight(child.getRightNode());
//        child.setHeight(1+Math.max(childHL, childHR));
        fixHeights(node);
        fixHeights(child);

        // Assign parent node
        try {
            if (parent == null) this.head = child;
            else if (parent.compareTo(child.getKey()) > 0) parent.setLeftNode(child);
            else parent.setRightNode(child);
        }catch(Exception e) {
            System.out.println("Error message: "+e.getMessage());
        }
    }

    private void fixHeights(Node<K> node) {
        int hL = getHeight(node.getLeftNode()), hR = getHeight(node.getRightNode());
        node.setHeight(1+Math.max(hL, hR));
    }

    public Node<K> findKey(K key) { if(this.head == null) System.out.println("ERROR: NULL"); return findKeyHelper(key, this.head); }

    public Node<K> findKeyHelper(K key, Node<K> curNode) {
        if (curNode == null) return null;

        try {
            if (curNode.getKey().equals(key)) return curNode;
            else if (curNode.compareTo(key) > 0) return findKeyHelper(key, curNode.getLeftNode());
            else return findKeyHelper(key, curNode.getRightNode());
        }catch(Exception e) {
            System.out.println("Error message: "+e.getMessage());
        }

        return null; // Error
    }

    // Call printInOrder with head node
    public void printInOrder() { printInOrder(this.head); }
    public void printInOrder(Node<K> node) {
        if(node == null) return;

        printInOrder(node.getLeftNode());
        System.out.println(node.getKey() + ", " + node.getHeight());
        printInOrder(node.getRightNode());
    }
}
