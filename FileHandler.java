package motorph;

import java.io.*;
import java.util.*;

public class FileHandler {
    private static final String FILE_NAME = "employees.csv";

    public static void writeEmployee(String[] data) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(String.join(",", data));
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> readEmployees() {
        List<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line.split(","));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void deleteEmployee(String name) {
        List<String[]> all = readEmployees();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String[] emp : all) {
                if (!emp[0].equals(name)) {
                    bw.write(String.join(",", emp));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}