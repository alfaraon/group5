package motorph;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class MainGUI extends JFrame {
    private JTextField txtName, txtPosition, txtSalary;
    private JButton btnAdd, btnDelete, btnLoad;
    private JTable table;
    private DefaultTableModel model;

    public MainGUI() {
        setTitle("MotorPH Employee Management");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblName = new JLabel("Name:");
        lblName.setBounds(30, 20, 80, 25);
        add(lblName);
        txtName = new JTextField();
        txtName.setBounds(100, 20, 150, 25);
        add(txtName);

        JLabel lblPosition = new JLabel("Position:");
        lblPosition.setBounds(30, 50, 80, 25);
        add(lblPosition);
        txtPosition = new JTextField();
        txtPosition.setBounds(100, 50, 150, 25);
        add(txtPosition);

        JLabel lblSalary = new JLabel("Salary:");
        lblSalary.setBounds(30, 80, 80, 25);
        add(lblSalary);
        txtSalary = new JTextField();
        txtSalary.setBounds(100, 80, 150, 25);
        add(txtSalary);

        btnAdd = new JButton("Add");
        btnAdd.setBounds(270, 20, 100, 25);
        add(btnAdd);
        btnDelete = new JButton("Delete");
        btnDelete.setBounds(270, 50, 100, 25);
        add(btnDelete);
        btnLoad = new JButton("Load");
        btnLoad.setBounds(270, 80, 100, 25);
        add(btnLoad);

        model = new DefaultTableModel(new String[]{"Name", "Position", "Salary"}, 0);
        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(30, 130, 520, 200);
        add(scroll);

        btnAdd.addActionListener(e -> {
            String[] row = {txtName.getText(), txtPosition.getText(), txtSalary.getText()};
            model.addRow(row);
            FileHandler.writeEmployee(row);
        });

        btnDelete.addActionListener(e -> {
            int selected = table.getSelectedRow();
            if (selected >= 0) {
                FileHandler.deleteEmployee((String) model.getValueAt(selected, 0));
                model.removeRow(selected);
            }
        });

        btnLoad.addActionListener(e -> {
            model.setRowCount(0);
            for (String[] emp : FileHandler.readEmployees()) {
                model.addRow(emp);
            }
        });
    }
}