import java.util.*;


public class Tree {
    private Node root;
    public ArrayList<String> log = new ArrayList<String>();


    public Tree() {
        root = null;
    }

    public void insert(double GMS, String name) {
        root = insert(root, GMS, name);
    }

    public void delete(double GMS, String name, boolean loggable) {
        root = delete(root, GMS, name, loggable);
    }

    // function to find the maximum depth common parent of two nodes
    public void intel_target(double GMS1, String name1, double GMS2, String name2) {
        findCommonParent(root, GMS1, GMS2);
    }
    public int intel_divide() {
        clearLISSMemoization(root); // Clear LISS memoization values
        int maxSetSize = findLargestIndependentSet(root);
        log.add("Division Analysis Result: " + maxSetSize);
        return maxSetSize;
    }

    private void clearLISSMemoization(Node node) {
        if (node == null) {
            return;
        }
        node.liss = 0; // Clear LISS memoization for this node
        clearLISSMemoization(node.left); // Recursively clear LISS memoization for left subtree
        clearLISSMemoization(node.right); // Recursively clear LISS memoization for right subtree
    }



    private int findLargestIndependentSet(Node root) {
        if (root == null)
            return 0;
        if (root.liss != 0)
            return root.liss;
        if (root.left == null && root.right == null)
            return root.liss = 1;

        int lissExcl = findLargestIndependentSet(root.left) + findLargestIndependentSet(root.right);

        int lissIncl = 1;
        if (root.left != null) {
            lissIncl += (findLargestIndependentSet(root.left.left) + findLargestIndependentSet(root.left.right));
        }
        if (root.right != null) {
            lissIncl += (findLargestIndependentSet(root.right.left) + findLargestIndependentSet(root.right.right));
        }
        return root.liss = Math.max(lissExcl, lissIncl);
    }

    public ArrayList<String> getLog() {
        return log;
    }

    public void intel_rank(double GMS, String name) {
        int rank = findDepth(root, GMS);
        int currentdepth = 0;
        ArrayList<String> rank_list = new ArrayList<String>();
        findtheSameRankNodes(root, rank, currentdepth, rank_list);
        StringBuffer str = new StringBuffer();
        for (String s : rank_list) {
            str.append(s);
            str.append(" ");
        }
        str.deleteCharAt(str.length() - 1);
        String result = str.toString();
        log.add("Rank Analysis Result: " + result);
    }

    // function to find the nodes with the same rank
    private void findtheSameRankNodes(Node node, int rank, int currentdepth, ArrayList<String> rank_list) {
        if (node == null) {
            return;
        }
        if (currentdepth == rank) {
            double gmscopy = node.GMS;
            String gmsform = String.format("%.3f", gmscopy);
            String gmsadj = gmsform.replace(",", ".");
            rank_list.add(node.name + " " + gmsadj);
        }
        findtheSameRankNodes(node.left, rank, currentdepth + 1, rank_list);
        findtheSameRankNodes(node.right, rank, currentdepth + 1, rank_list);
    }


    private int findDepth(Node node, double GMS) {
        if (node == null) {
            return -1;
        }
        if (GMS < node.GMS) {
            return findDepth(node.left, GMS) + 1;
        } else if (GMS > node.GMS) {
            return findDepth(node.right, GMS) + 1;
        } else {
            return 0;
        }
    }

    private void findCommonParent(Node node, double GMS1, double GMS2) {
        if (node == null) {
            log.add("Target Analysis Result: Nodes are not found");
        }
        if (GMS1 < node.GMS && GMS2 < node.GMS) {
            findCommonParent(node.left, GMS1, GMS2);
        } else if (GMS1 > node.GMS && GMS2 > node.GMS) {
            findCommonParent(node.right, GMS1, GMS2);
        } else {
            // while logging node gms should be 3 deciaml places (no string it is double)
            double gmscopy = node.GMS;
            String gmsform = String.format("%.3f", gmscopy);
            String gmsadj = gmsform.replace(",", ".");
            log.add("Target Analysis Result: " + node.name + " " + gmsadj);
        }
    }


    private Node insert(Node node, double GMS, String name) {
        if (node == null) {
            return new Node(GMS, name);
        }
        log.add(node.name + " welcomed " + name);
        if (GMS < node.GMS) {
            node.left = insert(node.left, GMS, name);
        } else if (GMS > node.GMS) {
            node.right = insert(node.right, GMS, name);
        }
        // Update the height and balance factor of the ancestor node
        node.height = 1 + Math.max(height(node.left), height(node.right));
        // Check if the tree height increased after insert

        // Balance the tree
        node = balance(node);
        return node;
    }

    private Node balance(Node node) {
        // Balance factor
        int balance = height(node.left) - height(node.right);

        // Left heavy subtree
        if (balance > 1) {
            // Left-Left case
            if (height(node.left.left) >= height(node.left.right)) {
                return rightRotate(node);
            }
            // Left-Right case
            else {
                node.left = leftRotate(node.left);
                return rightRotate(node);
            }
        }
        // Right heavy subtree
        else if (balance < -1) {
            // Right-Right case
            if (height(node.right.right) >= height(node.right.left)) {
                return leftRotate(node);
            }
            // Right-Left case
            else {
                node.right = rightRotate(node.right);
                return leftRotate(node);
            }
        }
        // Tree is balanced
        return node;
    }

    private Node leftRotate(Node node) {
        Node newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right));
        return newRoot;
    }

    private Node rightRotate(Node node) {
        Node newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        node.height = 1 + Math.max(height(node.left), height(node.right));
        newRoot.height = 1 + Math.max(height(newRoot.left), height(newRoot.right));
        return newRoot;
    }

    private int height(Node node) {
        if (node == null) {
            return -1;
        }
        return node.height;
    }

    private Node delete(Node node, double GMS, String name, boolean loggable) {
        if (node == null) {
            return null;
        }
        if (GMS < node.GMS) {
            node.left = delete(node.left, GMS, name, loggable);
        } else if (GMS > node.GMS) {
            node.right = delete(node.right, GMS, name, loggable);
        } else {
            // node to delete found
            if (node.left == null && node.right == null) {
                // node is a leaf
                if (loggable) {
                    log.add(node.name + " left the family, replaced by nobody");
                }
                return null;
            } else if (node.left == null) {
                if (loggable) {
                    log.add(node.name + " left the family, replaced by " + node.right.name);
                }
                return node.right;
            } else if (node.right == null) {
                if (loggable) {
                    log.add(node.name + " left the family, replaced by " + node.left.name);
                }
                return node.left;
            } else {
                // node has two children
                Node successor = getSuccessor(node);
                log.add(node.name + " left the family, replaced by " + successor.name);
                node.GMS = successor.GMS;
                node.name = successor.name;
                node.right = delete(node.right, successor.GMS, successor.name, false);
            }
        }
        // update the height and balance factor of the ancestor node
        node.height = 1 + Math.max(height(node.left), height(node.right));
        // balance the tree
        node = balance(node);
        return node;
    }

    private Node getSuccessor(Node node) {
        // get the node with the smallest GMS in the right subtree
        if (node == null) {
            return null;
        }
        Node temp = node.right;
        while (temp.left != null) {
            temp = temp.left;
        }
        return temp;
    }

}