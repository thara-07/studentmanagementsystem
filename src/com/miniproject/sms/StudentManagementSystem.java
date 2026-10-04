package com.miniproject.sms;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.io.*;

//Classes & Abstraction
abstract class User 
{
    // Common user properties
}

class Student extends User 
{
    int id;
    String name;
    String department;
    int year;
    double cgpa;
    Student(int id, String name, String department, int year, double cgpa) 
    {
        this.id = id;
        this.name = name;
        this.department = department;
        this.year = year;
        this.cgpa = cgpa;
    }
}

//Inheritance
class UGStudent extends Student 
{
    UGStudent(int id, String name, String department, int year, double cgpa) 
    {
        super(id, name, department, year, cgpa);
    }
}

//Interfaces
interface StudentOperations 
{
    void addStudent(Student s);
    Student searchStudent(int id);
    void deleteStudent(int id);
}

// Main GUI Class (Event-Driven & Polymorphism)
public class StudentManagementSystem<T> extends JFrame implements StudentOperations 
{
    JTextField id, name, year, cgpa;
    
    // FIXED: Added  to JComboBox
    JComboBox departmentCombo; 
    JTextArea output;

    // Arrays
    String[] deptArray = {"CSE", "IT", "ECE", "EEE", "MECH", "CIVIL"};

    // Collections Framework
    ArrayList<Student> students = new ArrayList<>();

    StudentManagementSystem() 
    {
        setTitle("Student Management System ");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(30, 30, 30)); 

        //Form Panel 
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(new Color(30, 30, 30));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        formPanel.add(createDarkLabel("Student ID:"));
        id = new JTextField();
        formPanel.add(id);
        formPanel.add(createDarkLabel("Name:")); 
        name = new JTextField();
        formPanel.add(name);
        formPanel.add(createDarkLabel("Department:"));
        departmentCombo = new JComboBox<>(deptArray);
        formPanel.add(departmentCombo);
        formPanel.add(createDarkLabel("Year:"));
        year = new JTextField();
        formPanel.add(year);
        formPanel.add(createDarkLabel("CGPA:"));
        cgpa = new JTextField();
        formPanel.add(cgpa);
        add(formPanel, BorderLayout.NORTH);

        //Buttons Panel 
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(30, 30, 30));
        JButton addButton = new JButton("Add");
        JButton searchButton = new JButton("Search");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton displayButton = new JButton("Display");
        JButton saveButton = new JButton("Save 💾");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(addButton); buttonPanel.add(searchButton); 
        buttonPanel.add(updateButton); buttonPanel.add(deleteButton); 
        buttonPanel.add(displayButton); buttonPanel.add(saveButton); 
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.CENTER);

        //Output Area 
        output = new JTextArea(10, 55);
        output.setBackground(Color.BLACK); 
        output.setForeground(Color.PINK);
        output.setFont(new Font("Consolas", Font.PLAIN, 14));
        output.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(output);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        scrollPane.setBackground(Color.white);
        add(scrollPane, BorderLayout.SOUTH);

        //Action Listeners (Event-Driven Programming) 
        addButton.addActionListener(e -> {
            try 
            { 
                // Exception Handling
                int i = Integer.parseInt(id.getText());
                
                // String handling
                String n = name.getText().trim().toUpperCase();
                String d = (String) departmentCombo.getSelectedItem();
                int y = Integer.parseInt(year.getText());
                double c = Double.parseDouble(cgpa.getText());
                if (n.isEmpty()) 
                    throw new Exception("Name cannot be empty!");
                if (c < 0 || c > 10)
                     throw new Exception("CGPA must be between 0 and 10");
                addStudent(new UGStudent(i,n,d,y,c));
                JOptionPane.showMessageDialog(this, "Student Added Successfully!");
                showMessage(i); 
                clearFields();
            } 
            catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(this, "Invalid Input: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        searchButton.addActionListener(e -> {
            try 
            {
                int i = Integer.parseInt(id.getText());
                Student s = searchStudent(i);
                if (s != null) 
                {
                    name.setText(s.name);
                    departmentCombo.setSelectedItem(s.department);
                    year.setText(String.valueOf(s.year));
                    cgpa.setText(String.valueOf(s.cgpa));
                } 
                else 
                {
                    JOptionPane.showMessageDialog(this, "Student Not Found 🕵️‍♂️");
                }
            } 
            catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(this, "Enter Valid Student ID");
            }
        });

        updateButton.addActionListener(e -> {
            try 
            {
                int i = Integer.parseInt(id.getText());
                Student s = searchStudent(i);
                if (s != null) 
                {
                    s.name = name.getText().trim().toUpperCase();
                    s.department = (String) departmentCombo.getSelectedItem();
                    s.year = Integer.parseInt(year.getText());
                    s.cgpa = Double.parseDouble(cgpa.getText());
                    JOptionPane.showMessageDialog(this, "Student Updated! ✨");
                } 
                else 
                {
                    JOptionPane.showMessageDialog(this, "Student Not Found");
                }
            } 
            catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(this, "Invalid Input");
            }
        });

        deleteButton.addActionListener(e -> {
            try 
            {
                int i = Integer.parseInt(id.getText());
                if (searchStudent(i) != null) 
                {
                    deleteStudent(i);
                    JOptionPane.showMessageDialog(this, "Student Deleted 🗑️");
                    clearFields();
                } 
                else 
                {
                    JOptionPane.showMessageDialog(this, "Student Not Found");
                }
            } 
            catch (Exception ex) 
            {
                JOptionPane.showMessageDialog(this, "Enter Valid Student ID");
            }
        });

        displayButton.addActionListener(e -> {
            output.setText("--- Student Records ---\n");
            for (Student s : students) 
            {
                output.append(String.format("ID: %-5d | Name: %-15s | Dept: %-5s | Year: %-2d | CGPA: %.2f\n", 
                              s.id, s.name, s.department, s.year, s.cgpa));
            }
        });

        // Multi-Threading
        saveButton.addActionListener(e -> {
            new Thread(this::saveStudents).start(); 
        });

        clearButton.addActionListener(e -> clearFields());

        setVisible(true);
    }

    private JLabel createDarkLabel(String text) 
    {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    private void clearFields() 
    {
        id.setText(""); name.setText(""); 
        departmentCombo.setSelectedIndex(0);
        year.setText(""); cgpa.setText("");
    }

    // Thread Synchronization 
    public synchronized void addStudent(Student s) 
    { 
        students.add(s); 
    }

    public synchronized Student searchStudent(int id) 
    {
        return students.stream().filter(s -> s.id == id).findFirst().orElse(null); 
    }

    public synchronized void deleteStudent(int id) 
    {
        students.removeIf(s -> s.id == id);
    }

    // File I/O 
    synchronized void saveStudents() 
    {
        try (FileWriter f = new FileWriter("students.txt")) 
        { 
            for (Student s : students) 
            {
                f.write(s.id + "," + s.name + "," + s.department + "," + s.year + "," + s.cgpa + "\n");
            }
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "Records Saved! 🚀"));
        } 
        catch (Exception e) 
        {
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this, "File Error ❌"));
        }
    }

    // Generic Programming: generic method with its own type parameter <T>
    <T> void showMessage(T value) 
    {
        System.out.println(value);
    }

    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(StudentManagementSystem<Integer>::new);
    }
}