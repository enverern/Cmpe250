import java.io.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class project2 {
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Main <input_file1> <input_file2> <output_file>");
            return;
        }

        String inputFile1 = args[0];
        String inputFile2 = args[1];
        String outputFile = args[2];

        // create a company named lahmacun
        Company lahmacun = new Company();

        // read the employees from the first input file
        try (Scanner scanner = new Scanner(new File(inputFile1))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(",");
                // delete spaces
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }
                String city = data[0];
                String district = data[1];
                String name = data[2];
                String position = data[3];
                lahmacun.addemployee(city, district, name, position);
            }
        } catch (IOException e) {
            System.out.println("Error reading the first input file: " + e.getMessage());
            return;
        }
        // read the events from the second input file
        try (Scanner scanner = new Scanner(new File(inputFile2))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] data = line.split(":");
                // delete spaces
                for (int i = 0; i < data.length; i++) {
                    data[i] = data[i].trim();
                }
                if (data.length == 1){
                    lahmacun.reset_monthly_bonuses();
                } else if (Objects.equals(data[0], "ADD")) {
                    String emp_info = data[1];
                    String[] emp_data = emp_info.split(",");
                    for (int i = 0; i < emp_data.length; i++) {
                        emp_data[i] = emp_data[i].trim();
                    }
                    lahmacun.addemployee(emp_data[0], emp_data[1], emp_data[2], emp_data[3]);
                } else if (Objects.equals(data[0], "LEAVE")) {
                    String emp_info = data[1];
                    String[] emp_data = emp_info.split(",");
                    for (int i = 0; i < emp_data.length; i++) {
                        emp_data[i] = emp_data[i].trim();
                    }
                    lahmacun.remove_employee(emp_data[0], emp_data[1], emp_data[2]);
                } else if (Objects.equals(data[0], "PERFORMANCE_UPDATE")){
                    String emp_info = data[1];
                    String[] emp_data = emp_info.split(",");
                    for (int i = 0; i < emp_data.length; i++) {
                        emp_data[i] = emp_data[i].trim();
                    }
                    lahmacun.performanceUpdate(emp_data[0], emp_data[1], emp_data[2], Integer.parseInt(emp_data[3]));
                } else if (Objects.equals(data[0], "PRINT_MONTHLY_BONUSES")){
                    String[] emp_data = data[1].split(",");
                    for (int i = 0; i < emp_data.length; i++) {
                        emp_data[i] = emp_data[i].trim();
                    }
                    lahmacun.print_monthly_bonuses(emp_data[0], emp_data[1]);
                } else if (Objects.equals(data[0], "PRINT_OVERALL_BONUSES")){
                    String[] emp_data = data[1].split(",");
                    for (int i = 0; i < emp_data.length; i++) {
                        emp_data[i] = emp_data[i].trim();
                    }
                    lahmacun.print_overall_bonuses(emp_data[0], emp_data[1]);
                } else if (Objects.equals(data[0], "PRINT_MANAGER")){
                    String[] emp_data = data[1].split(",");
                    for (int i = 0; i < emp_data.length; i++) {
                        emp_data[i] = emp_data[i].trim();
                    }
                    lahmacun.print_manager(emp_data[0], emp_data[1]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the second input file: " + e.getMessage());
            return;
        }
        // get the events from the company
        ArrayList<String> events = lahmacun.getLog();
        // write the events to the output file
        try (PrintWriter writer = new PrintWriter(new File(outputFile))) {
            for (String event : events) {
                writer.println(event+"\r");
            }
        } catch (IOException e) {
            System.out.println("Error writing the output file: " + e.getMessage());
        }
    }
}