import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 6) {
            System.out.println("Usage: java Main <airports-csv> <directions-csv> <weather-csv> <missions-in> <task1-out> <task2-out>");
            return;
        }
        String airportsCsv = args[0];
        String directionsCsv = args[1];
        String weatherCsv = args[2];
        String missionsIn = args[3];
        String task1Out = args[4];
        String task2Out = args[5];

        ArrayList<String> task1_outs = new ArrayList<>();
        ArrayList<String> task2_outs = new ArrayList<>();

        Airline_graph<Node> currentGraph = new Airline_graph<>();
        try (Scanner scanner = new Scanner(new File(airportsCsv))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String airportCode = parts[0];
                String airfieldName = parts[1];
                double latitude = Double.parseDouble(parts[2]);
                double longitude = Double.parseDouble(parts[3]);
                int parkingCost = Integer.parseInt(parts[4]);
                Airport airport = new Airport(airportCode, airfieldName, latitude, longitude, parkingCost);
                Node node = new Node(airport);
                currentGraph.addNode(node);
            }
        }

        try (Scanner scanner = new Scanner(new File(directionsCsv))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String fromAirportCode = parts[0];
                String toAirportCode = parts[1];
                currentGraph.addEdge(fromAirportCode, toAirportCode);
            }
        }

        try (Scanner scanner = new Scanner(new File(weatherCsv))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                String airfieldName = parts[0];
                long timestamp = Long.parseLong(parts[1]);
                int weatherCode = Integer.parseInt(parts[2]);
                currentGraph.addWeather(airfieldName, timestamp, weatherCode);
            }
        }

        try (Scanner scanner = new Scanner(new File(missionsIn))) {
            String plane_model = scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                String fromAirportCode = parts[0];
                String toAirportCode = parts[1];
                long departureTime = Long.parseLong(parts[2]);
                long deadlineTime = Long.parseLong(parts[3]);
                ArrayList <String> outputs = currentGraph.addMission(plane_model, fromAirportCode, toAirportCode, departureTime, deadlineTime);
                task1_outs.add(outputs.get(0));
                task2_outs.add(outputs.get(1));
            }
        }

        try (PrintWriter writer = new PrintWriter(task1Out)) {
            try (PrintWriter writer2 = new PrintWriter(task2Out)) {
                for (int i = 0; i < task1_outs.size(); i++) {
                    writer.println(task1_outs.get(i));
                    writer2.println(task2_outs.get(i));
                }
            }
        }
    }
}