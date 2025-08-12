import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class StudentMarksReport extends JFrame implements ActionListener {
    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/studentdb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Nihal@_30";

    // GUI components
    private JTextField nameField, rollField;
    private JTextField[] marksFields;
    private JTextArea reportArea;
    private JButton generateBtn, exitBtn;
    private String[] subjectNames = {
        "Mobile Application Development with Java",
        "Software Engineering",
        "Artificial Intelligence",
        "Digital Image Processing",
        "Advance DBMS"
    };

    public StudentMarksReport() {
        setTitle("Student Marks Report System");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Initialize database
        initializeDatabase();

        // Input panel
        JPanel inputPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Enter Student Details"));

        nameField = new JTextField();
        rollField = new JTextField();
        marksFields = new JTextField[subjectNames.length];

        inputPanel.add(new JLabel("Student Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Roll Number:"));
        inputPanel.add(rollField);

        for (int i = 0; i < subjectNames.length; i++) {
            inputPanel.add(new JLabel(subjectNames[i] + ":"));
            marksFields[i] = new JTextField();
            inputPanel.add(marksFields[i]);
        }

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        generateBtn = new JButton("Generate Report");
        generateBtn.addActionListener(this);
        exitBtn = new JButton("Exit");
        exitBtn.addActionListener(this);
        buttonPanel.add(generateBtn);
        buttonPanel.add(exitBtn);

        // Report area
        reportArea = new JTextArea();
        reportArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(reportArea);

        // Add components to frame
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            String sql = "CREATE TABLE IF NOT EXISTS students (" +
                         "id INT PRIMARY KEY AUTO_INCREMENT, " +
                         "name VARCHAR(100) NOT NULL, " +
                         "roll_no VARCHAR(20) NOT NULL, " +
                         "subject1 INT, subject2 INT, subject3 INT, " +
                         "subject4 INT, subject5 INT, " +
                         "UNIQUE (roll_no))";
            stmt.executeUpdate(sql);
            
        } catch (SQLException e) {
            showMessage("Database initialization failed: " + e.getMessage());
        }
    }

    private void saveToDatabase(String name, String rollNo, int[] marks) {
        String sql = "INSERT INTO students (name, roll_no, subject1, subject2, subject3, subject4, subject5) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, rollNo);
            for (int i = 0; i < marks.length; i++) {
                pstmt.setInt(i + 3, marks[i]);
            }
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            showMessage("Error saving to database: " + e.getMessage());
        }
    }

    private void generateReport() {
        String name = nameField.getText().trim();
        String rollText = rollField.getText().trim();

        if (name.isEmpty() || rollText.isEmpty()) {
            showMessage("Please enter both name and roll number.");
            return;
        }

        int[] marks = new int[subjectNames.length];
        for (int i = 0; i < subjectNames.length; i++) {
            String markText = marksFields[i].getText().trim();
            if (markText.isEmpty()) {
                showMessage("Please fill all marks fields.");
                return;
            }
            try {
                marks[i] = Integer.parseInt(markText);
                if (marks[i] < 0 || marks[i] > 100) {
                    showMessage("Marks must be between 0 and 100.");
                    return;
                }
            } catch (NumberFormatException e) {
                showMessage("Invalid mark in subject: " + subjectNames[i]);
                return;
            }
        }

        // Save to database
        saveToDatabase(name, rollText, marks);

        // Calculate results
        int total = 0;
        for (int mark : marks) total += mark;
        float percentage = (total / (float)(subjectNames.length * 100)) * 100;

        String grade;
        if (percentage >= 90) grade = "A";
        else if (percentage >= 80) grade = "B";
        else if (percentage >= 70) grade = "C";
        else if (percentage >= 60) grade = "D";
        else grade = "F";

        // Generate report
        StringBuilder report = new StringBuilder();
        report.append("--- Student Report ---\n");
        report.append("Name : ").append(name).append("\n");
        report.append("Roll Number : ").append(rollText).append("\n\n");
        
        for (int i = 0; i < subjectNames.length; i++) {
            report.append(subjectNames[i]).append(" : ").append(marks[i]).append("\n");
        }
        
        report.append("\nTotal Marks : ").append(total).append("\n");
        report.append(String.format("Percentage : %.2f%%\n", percentage));
        report.append("Grade : ").append(grade).append("\n");

        reportArea.setText(report.toString());
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Alert", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == generateBtn) {
            generateReport();
        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new StudentMarksReport();
    }
}