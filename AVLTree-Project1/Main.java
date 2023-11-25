import java.io.*;
import java.util.*;


public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Main <input_file> <output_file>");
            return;
        }

        String inputFileName = args[0];
        String outputFileName = args[1];

        Tree tree = new Tree();

        try (Scanner scanner = new Scanner(new File(inputFileName));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFileName))) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" ");
                if (parts.length >= 3) {
                    if (parts[0].equals("MEMBER_IN")) {
                        String name = parts[1];
                        double gms = Double.parseDouble(parts[2]);
                        tree.insert(gms, name);
                    }
                    else if (parts[0].equals("MEMBER_OUT")) {
                        String name = parts[1];
                        double gms = Double.parseDouble(parts[2]);
                        tree.delete(gms, name, true);
                    }
                    else if (parts[0].equals("INTEL_TARGET")){
                        String name1 = parts[1];
                        double gms1 = Double.parseDouble(parts[2]);
                        String name2 = parts[3];
                        double gms2 = Double.parseDouble(parts[4]);
                        tree.intel_target(gms1, name1, gms2, name2);
                    }
                    else if (parts[0].equals("INTEL_RANK")){
                        String name = parts[1];
                        double gms = Double.parseDouble(parts[2]);
                        tree.intel_rank(gms, name);
                    }
                } else if (parts.length == 2) {
                    String name = parts[0];
                    double gms = Double.parseDouble(parts[1]);
                    tree.insert(gms, name);
                } else if (parts[0].equals("INTEL_DIVIDE")) {
                    tree.intel_divide();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> events = tree.getLog();

        try (FileWriter logWriter = new FileWriter(outputFileName)) {
            for (String event : events) {
                logWriter.write(event + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
