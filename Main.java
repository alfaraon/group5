package com.motorph.app;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EmployeeService service = new EmployeeService();
        int choice;

        do {
            System.out.println("\n=== MotorPH Employee Menu ===");
            System.out.println("1. View Employees");
            System.out.println("2. Add New Employee");
            System.out.println("0. Exit");
            System.out.print("Enter your choice: ");
            while (!scanner.hasNextInt()) {
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next();
            }
            choice = scanner.nextInt(); scanner.nextLine();

            switch (choice) {
                case 1 -> service.viewEmployees();
                case 2 -> {
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter position: ");
                    String position = scanner.nextLine();
                    service.addEmployee(name, position);
                }
                case 0 -> System.out.println("👋 Goodbye!");
                default -> System.out.println("❌ Invalid option. Try again.");
            }
        } while (choice != 0);
    }
}
