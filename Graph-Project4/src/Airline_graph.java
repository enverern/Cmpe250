import java.util.*;

class Airline_graph<T extends Node> {
    //airport code -> airport
    private final HashMap<String, Node> airportMap;
    //set of permanent nodes
    private final HashMap<String, Double> weatherMultiplier;
    private int numberOfNodes;


    public Airline_graph() {
        this.airportMap = new HashMap<>();
        this.weatherMultiplier = new HashMap<>();
        this.numberOfNodes = 0;
    }

    public void addNode(T node) {
        airportMap.put(node.getAirport().getAirportCode(), node);
        numberOfNodes++;
    }

    public void addEdge(String fromAirportCode, String toAirportCode) {
        Node node1 = airportMap.get(fromAirportCode);
        Node node2 = airportMap.get(toAirportCode);
        node1.addNeighbor(node2);
    }

    public void addWeather(String airfieldName, long timestamp, int weatherCode) {
        String key = airfieldName + timestamp;
        // get the last 5 digit of the string
        StringBuilder weatherBits = new StringBuilder(Integer.toBinaryString(weatherCode));
        if (weatherBits.length() > 5) {
            weatherBits = new StringBuilder(weatherBits.substring(weatherBits.length() - 5));
        }
        while (weatherBits.length() < 5) {
            weatherBits.insert(0, "0");
        }

        int bw = Integer.parseInt(weatherBits.substring(0, 1));
        int br = Integer.parseInt(weatherBits.substring(1, 2));
        int bs = Integer.parseInt(weatherBits.substring(2, 3));
        int bh = Integer.parseInt(weatherBits.substring(3, 4));
        int bb = Integer.parseInt(weatherBits.substring(4, 5));

        double w = (bw * 1.05 + (1 - bw)) * (br * 1.05 + (1 - br)) * (bs * 1.10 + (1 - bs)) * (bh * 1.15 + (1 - bh)) * (bb * 1.20 + (1 - bb));

        weatherMultiplier.put(key, w);
    }

    public ArrayList<String> addMission(String plane_model, String fromAirportCode, String toAirportCode, long departureTime, long deadlineTime) {
        Node node1 = airportMap.get(fromAirportCode);
        Node node2 = airportMap.get(toAirportCode);
        String task1 = DijkstraTask1(node1, node2, departureTime);
        String task2 = Task2(node1, node2, departureTime, deadlineTime, plane_model);
        ArrayList<String> outputs = new ArrayList<>();
        outputs.add(task1);
        outputs.add(task2);
        return outputs;
    }


    private String Task2(Node node1, Node node2, long departureTime, long deadlineTime, String plane_model) {
        ArrayList<HashMap<Node, ArrayList<String>>> time_step_hashmaps = new ArrayList<>();
        ArrayList<Node> visited_nodes = new ArrayList<>();
        int number_of_time_steps = (int) ((deadlineTime - departureTime) / (6*3600));
        for (int i = 0; i <= number_of_time_steps+3; i++) {
            HashMap<Node, ArrayList<String>> time_step_hashmap = new HashMap<>();
            time_step_hashmaps.add(time_step_hashmap);
        }
        HashMap<Node, ArrayList<String>> time_step_hashmap = time_step_hashmaps.get(0);
        ArrayList<String> first_time_step = new ArrayList<>();
        first_time_step.add(node1.getAirport().getAirportCode());
        first_time_step.add("0.0");
        time_step_hashmap.put(node1, first_time_step);
        visited_nodes.add(node1);
        for (int i = 0; i < number_of_time_steps; i++) {
            HashMap<Node, ArrayList<String>> current_time_step_hashmap = time_step_hashmaps.get(i);
            for (Node node : visited_nodes) {
                ArrayList<String> node_current_path = current_time_step_hashmap.get(node);
                double node_current_cost = Double.parseDouble(node_current_path.get(node_current_path.size()-1));
                for (Node neighbor : node.adjList) {
                    long duration = get_duration(node, neighbor, plane_model);
                    boolean b = (duration + departureTime + (long) i * 6 * 3600) <= deadlineTime;
                    if (b){
                        if (duration == (long) 6*3600){
                            HashMap<Node, ArrayList<String>> neighbor_time_step_hashmap = time_step_hashmaps.get(i+1);
                            if (neighbor_time_step_hashmap.get(neighbor) == null){
                                ArrayList<String> neighbor_current_path = new ArrayList<>(node_current_path);
                                double cost = node_current_cost + (node.outgoing_edges.get(neighbor).getDistance() + 300 * weatherMultiplier.get(neighbor.getAirport().getAirfieldName() + (departureTime + (long) (i + 1) *6*3600)) * weatherMultiplier.get(node.getAirport().getAirfieldName() + (departureTime + (long) i *6*3600)));
                                neighbor_current_path.set(neighbor_current_path.size()-1, neighbor.getAirport().getAirportCode());
                                neighbor_current_path.add(String.valueOf(cost));
                                neighbor_time_step_hashmap.put(neighbor, neighbor_current_path);
                            }
                            else {
                                ArrayList<String> neighbor_current_path = neighbor_time_step_hashmap.get(neighbor);
                                double cost = node_current_cost + (node.outgoing_edges.get(neighbor).getDistance() + 300 * weatherMultiplier.get(neighbor.getAirport().getAirfieldName() + (departureTime + (long) (i + 1) *6*3600)) * weatherMultiplier.get(node.getAirport().getAirfieldName() + (departureTime + (long) i *6*3600)));
                                if (cost < Double.parseDouble(neighbor_current_path.get(neighbor_current_path.size()-1))){
                                    ArrayList<String> new_path = new ArrayList<>(node_current_path);
                                    new_path.set(new_path.size() - 1, neighbor.getAirport().getAirportCode());
                                    new_path.add(String.valueOf(cost));
                                    neighbor_time_step_hashmap.put(neighbor, new_path);
                                }
                            }
                        } else if (duration == 12*3600){
                            HashMap<Node, ArrayList<String>> neighbor_time_step_hashmap = time_step_hashmaps.get(i+2);
                            if (neighbor_time_step_hashmap.get(neighbor) == null){
                                ArrayList<String> neighbor_current_path = new ArrayList<>(node_current_path);
                                double cost = node_current_cost + (node.outgoing_edges.get(neighbor).getDistance() + 300 * weatherMultiplier.get(neighbor.getAirport().getAirfieldName() + (departureTime + (long) (i + 2) *6*3600)) * weatherMultiplier.get(node.getAirport().getAirfieldName() + (departureTime + (long) i *6*3600)));
                                neighbor_current_path.set(neighbor_current_path.size()-1, neighbor.getAirport().getAirportCode());
                                neighbor_current_path.add(String.valueOf(cost));
                                neighbor_time_step_hashmap.put(neighbor, neighbor_current_path);
                            }
                            else {
                                ArrayList<String> neighbor_current_path = neighbor_time_step_hashmap.get(neighbor);
                                double cost = node_current_cost + (node.outgoing_edges.get(neighbor).getDistance() + 300 * weatherMultiplier.get(neighbor.getAirport().getAirfieldName() + (departureTime + (long) (i + 2) *6*3600)) * weatherMultiplier.get(node.getAirport().getAirfieldName() + (departureTime + (long) i *6*3600)));
                                if (cost < Double.parseDouble(neighbor_current_path.get(neighbor_current_path.size()-1))){
                                    ArrayList<String> new_path = new ArrayList<>(node_current_path);
                                    new_path.set(new_path.size() - 1, neighbor.getAirport().getAirportCode());
                                    new_path.add(String.valueOf(cost));
                                    neighbor_time_step_hashmap.put(neighbor, new_path);
                                }
                            }
                        } else {
                            HashMap<Node, ArrayList<String>> neighbor_time_step_hashmap = time_step_hashmaps.get(i + 3);
                            if (neighbor_time_step_hashmap.get(neighbor) == null) {
                                ArrayList<String> neighbor_current_path = new ArrayList<>(node_current_path);
                                double cost = node_current_cost + (node.outgoing_edges.get(neighbor).getDistance() + 300 * weatherMultiplier.get(neighbor.getAirport().getAirfieldName() + (departureTime + (long) (i + 3) * 6 * 3600)) * weatherMultiplier.get(node.getAirport().getAirfieldName() + (departureTime + (long) i * 6 * 3600)));
                                neighbor_current_path.set(neighbor_current_path.size() - 1, neighbor.getAirport().getAirportCode());
                                neighbor_current_path.add(String.valueOf(cost));
                                neighbor_time_step_hashmap.put(neighbor, neighbor_current_path);
                            } else {
                                ArrayList<String> neighbor_current_path = neighbor_time_step_hashmap.get(neighbor);
                                double cost = node_current_cost + (node.outgoing_edges.get(neighbor).getDistance() + 300 * weatherMultiplier.get(neighbor.getAirport().getAirfieldName() + (departureTime + (long) (i + 3) * 6 * 3600)) * weatherMultiplier.get(node.getAirport().getAirfieldName() + (departureTime + (long) i * 6 * 3600)));
                                if (cost < Double.parseDouble(neighbor_current_path.get(neighbor_current_path.size() - 1))) {
                                    ArrayList<String> new_path = new ArrayList<>(node_current_path);
                                    new_path.set(new_path.size() - 1, neighbor.getAirport().getAirportCode());
                                    new_path.add(String.valueOf(cost));
                                    neighbor_time_step_hashmap.put(neighbor, new_path);
                                }
                            }
                        }
                    }
                }
                HashMap<Node, ArrayList<String>> next_time_step_hashmap = time_step_hashmaps.get(i+1);
                if (next_time_step_hashmap.get(node) == null){
                    ArrayList<String> node_next_path = new ArrayList<>(node_current_path);
                    double cost = node.getAirport().getParkingCost() + node_current_cost;
                    node_next_path.set(node_next_path.size()-1, "PARK");
                    node_next_path.add(String.valueOf(cost));
                    next_time_step_hashmap.put(node, node_next_path);
                }
                else {
                    ArrayList<String> node_next_path = next_time_step_hashmap.get(node);
                    double cost = node_current_cost + node.getAirport().getParkingCost();
                    if (cost < Double.parseDouble(node_next_path.get(node_next_path.size()-1))){
                        ArrayList<String> new_path = new ArrayList<>(node_current_path);
                        new_path.set(new_path.size()-1, "PARK");
                        new_path.add(String.valueOf(cost));
                        next_time_step_hashmap.put(node, new_path);
                    }
                }
            }
            HashMap<Node, ArrayList<String>> next__time_step_hashmap = time_step_hashmaps.get(i+1);
            for (Node node : next__time_step_hashmap.keySet()){
                if (!visited_nodes.contains(node)){
                    visited_nodes.add(node);
                }
            }

        }
        HashMap<Node, ArrayList<String>> last_time_step_hashmap = time_step_hashmaps.get(number_of_time_steps);
        if (last_time_step_hashmap.get(node2) == null){
            return "No possible solution.";
        }
        else{
            ArrayList<String> min_path = last_time_step_hashmap.get(node2);
            double min_cost = Double.parseDouble(min_path.get(min_path.size()-1));
            for (int i = 0; i < number_of_time_steps; i++){
                HashMap<Node, ArrayList<String>> time_step_hashmap1 = time_step_hashmaps.get(i);
                ArrayList<String> time_step_path = time_step_hashmap1.get(node2);
                if (time_step_path != null){
                    double time_step_cost = Double.parseDouble(time_step_path.get(time_step_path.size()-1));
                    if (time_step_cost < min_cost){
                        min_path = time_step_path;
                        min_cost = time_step_cost;
                    }
                }
            }

            StringBuilder output = new StringBuilder();
            for (int i = 0; i < min_path.size()-1; i++){
                output.append(min_path.get(i)).append(" ");
            }
            double total_cost = Double.parseDouble(min_path.get(min_path.size()-1));
            String costString = String.format("%.5f", total_cost).replace(",", ".");
            output.append(costString);
            return output.toString();
        }
    }

    private long get_duration(Node node1, Node node2, String plane_model) {
        double distance = node1.outgoing_edges.get(node2).getDistance();
        switch (plane_model) {
            case "Carreidas 160" -> {
                if (distance <= 175) {
                    return 6 * 3600;
                } else if (distance <= 350) {
                    return 12 * 3600;
                } else {
                    return 18 * 3600;
                }
            }
            case "Orion III" -> {
                if (distance <= 1500) {
                    return 6 * 3600;
                } else if (distance <= 3000) {
                    return 12 * 3600;
                } else {
                    return 18 * 3600;
                }
            }
            case "Skyfleet S570" -> {
                if (distance <= 500) {
                    return 6 * 3600;
                } else if (distance <= 1000) {
                    return 12 * 3600;
                } else {
                    return 18 * 3600;
                }
            }
            case null, default -> {
                if (distance <= 2500) {
                    return 6 * 3600;
                } else if (distance <= 5000) {
                    return 12 * 3600;
                } else {
                    return 18 * 3600;
                }
            }
        }
    }

    private String DijkstraTask1(Node node1, Node node2, long departureTime) {
        HashMap<Node, Double> costMap = new HashMap<>();
        HashMap<Node, Node> previousMap = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(costMap::get));
        costMap.put(node1, 0.0);
        queue.add(node1);
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current == node2) {
                break;
            }
            for (Node neighbor : current.adjList) {
                double cost = costMap.get(current) + current.outgoing_edges.get(neighbor).getDistance() + 300 * weatherMultiplier.get(neighbor.getAirport().getAirfieldName() + departureTime) * weatherMultiplier.get(current.getAirport().getAirfieldName() + departureTime);
                if (costMap.get(neighbor) == null || cost < costMap.get(neighbor)) {
                    costMap.put(neighbor, cost);
                    previousMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }
        StringBuilder output = new StringBuilder();
        Node current = node2;
        while (current != node1) {
            output.insert(0, current.getAirport().getAirportCode() + " ");
            current = previousMap.get(current);
        }
        output.insert(0, node1.getAirport().getAirportCode() + " ");
        double cost = costMap.get(node2);
        String costString = String.format("%.5f", cost).replace(",", ".");
        output.append(costString);
        return output.toString();

    }

}