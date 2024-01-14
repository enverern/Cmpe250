import java.util.*;

public class Node {
    public ArrayList<Node> adjList;
    private final Airport Airport;
    public final HashMap<Node, Edge> outgoing_edges = new HashMap<>();

    public Node(Airport airport){
        this.Airport = airport;
        this.adjList = new ArrayList<>();
    }

    public Airport getAirport() {
        return Airport;
    }

    public void addNeighbor(Node node2){
        adjList.add(node2);
        outgoing_edges.put(node2, new Edge(this, node2));
    }
}