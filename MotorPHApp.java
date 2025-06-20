package com.motorph;

import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class Employee implements Serializable {
    private String id;
    private String name;
    private String department;
    private String position;
    private double salary;

    public Employee(String id, String name, String department, String position, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.position = position;
        this.salary = salary;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public String getPosition() { return position; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return String.format("%s | %s | %s | %s | %.2f", id, name, department, position, salary);
    }
}

class EmployeeService {
    private List<Employee> employeeList = new ArrayList<>();
    private final String FILE_NAME = "employees.dat";

    public EmployeeService() {
        loadFromFile();
    }

    public void addEmployee(Employee emp) {
        employeeList.add(emp);
        saveToFile();
    }

    
    public Employee findEmployeeById(String id) {
        for (Employee emp : employeeList) {
            if (emp.getId().equals(id)) return emp;
        }
        return null;
    }

    public void updateEmployee(String id, String name, String dept, String pos, double salary) {
        Employee emp = findEmployeeById(id);
        if (emp != null) {
            employeeList.remove(emp);
            employeeList.add(new Employee(id, name, dept, pos, salary));
            saveToFile();
        }
    }

    public void deleteEmployee(String id) {
        Employee emp = findEmployeeById(id);
        if (emp != null) {
            employeeList.remove(emp);
            saveToFile();
        }
    }

    public List<Employee> getAllEmployees() {

        return employeeList;
    }

    private void saveToFile() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(employeeList);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error saving data: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            employeeList = (List<Employee>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            employeeList = new ArrayList<>();
        }
    }
}

public class MotorPHApp extends JFrame {
    private EmployeeService service = new EmployeeService();
    private JTextArea displayArea;

    public MotorPHApp() {
        setTitle("MotorPH Employee Application");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JButton createButton = new JButton("Create Employee");
        JButton viewButton = new JButton("View Employees");
        JButton updateButton = new JButton("Update Employee");
        JButton deleteButton = new JButton("Delete Employee");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(createButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        add(buttonPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        createButton.addActionListener(e -> showCreateDialog());
        viewButton.addActionListener(e -> showAllEmployees());
        updateButton.addActionListener(e -> showUpdateDialog());
        deleteButton.addActionListener(e -> showDeleteDialog());
    }

    private void showCreateDialog() {
        JTextField nameField = new JTextField();
        JTextField deptField = new JTextField();
        JTextField posField = new JTextField();
        JTextField salaryField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Department:")); panel.add(deptField);
        panel.add(new JLabel("Position:")); panel.add(posField);
        panel.add(new JLabel("Salary:")); panel.add(salaryField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Create Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                String dept = deptField.getText();
                String pos = posField.getText();
                double salary = Double.parseDouble(salaryField.getText());
                Employee emp = new Employee(UUID.randomUUID().toString(), name, dept, pos, salary);
                service.addEmployee(emp);
                JOptionPane.showMessageDialog(this, "Employee added successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAllEmployees() {
        StringBuilder sb = new StringBuilder();
        List<Employee> employees = service.getAllEmployees();
        if (employees.isEmpty()) {
            sb.append("No employee records found.");
        } else {
            for (Employee emp : employees) {
                sb.append(emp.toString()).append("\n");
            }
        }
        displayArea.setText(sb.toString());
    }

    
    private void showUpdateDialog() {
        String id = JOptionPane.showInputDialog(this, "Enter Employee ID to Update:");
        if (id == null || id.isEmpty()) return;

        Employee emp = service.findEmployeeById(id);
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "Employee not found.");
            return;
        }

        JTextField nameField = new JTextField(emp.getName());
        JTextField deptField = new JTextField(emp.getDepartment());
        JTextField posField = new JTextField(emp.getPosition());
        JTextField salaryField = new JTextField(String.valueOf(emp.getSalary()));

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:")); panel.add(nameField);
        panel.add(new JLabel("Department:")); panel.add(deptField);
        panel.add(new JLabel("Position:")); panel.add(posField);
        panel.add(new JLabel("Salary:")); panel.add(salaryField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Update Employee", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                String dept = deptField.getText();
                String pos = posField.getText();
                double salary = Double.parseDouble(salaryField.getText());
                service.updateEmployee(id, name, dept, pos, salary);
                JOptionPane.showMessageDialog(this, "Employee updated successfully.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid salary input.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDeleteDialog() {
        String id = JOptionPane.showInputDialog(this, "Enter Employee ID to Delete:");
        if (id == null || id.isEmpty()) return;

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete employee with ID: " + id + "?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteEmployee(id);
            JOptionPane.showMessageDialog(this, "Employee deleted successfully.");
        }
    }
public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MotorPHApp().setVisible(true);
        });
    }
}
