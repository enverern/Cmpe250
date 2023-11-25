public class Node {
    double GMS;
    String name;
    Node left;
    Node right;
    int height;
    int liss;  // Added for LISS calculation

    public Node(double GMS, String name) {
        this.GMS = GMS;
        this.name = name;
        left = null;
        right = null;
        height = 0;
        liss = 0;  // Initialize LISS to 0
    }
}
