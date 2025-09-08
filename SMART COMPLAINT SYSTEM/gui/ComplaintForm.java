package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ComplaintForm extends JFrame {
    // Define colors for UI consistency
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel blue
    private static final Color LABEL_COLOR = new Color(60, 60, 60);
    
    public ComplaintForm(String username) {
        setTitle("Submit New Complaint");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        
        // Create main panel with border layout
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        setContentPane(mainPanel);
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("New Complaint", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(LABEL_COLOR);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(8, 1, 0, 10));
        formPanel.setBackground(BACKGROUND_COLOR);
        formPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Form components
        JLabel titleFieldLabel = new JLabel("Complaint Title:");
        styleLabel(titleFieldLabel);
        
        JTextField titleField = new JTextField();
        styleTextField(titleField);
        
        JLabel descLabel = new JLabel("Description:");
        styleLabel(descLabel);
        
        JTextArea descArea = new JTextArea();
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descArea.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 8, 8, 8)));
        JScrollPane scrollPane = new JScrollPane(descArea);
        scrollPane.setPreferredSize(new Dimension(450, 150));
        
        JLabel deptLabel = new JLabel("Department:");
        styleLabel(deptLabel);
        
        JComboBox<String> deptBox = new JComboBox<>(new String[]{"IT", "Hostel", "Maintenance"});
        styleComboBox(deptBox);
        
        JLabel priorityLabel = new JLabel("Priority:");
        styleLabel(priorityLabel);
        
        JComboBox<String> priorityBox = new JComboBox<>(new String[]{"Low", "Medium", "High"});
        styleComboBox(priorityBox);
        
        // Add components to form panel
        formPanel.add(titleFieldLabel);
        formPanel.add(titleField);
        formPanel.add(descLabel);
        formPanel.add(scrollPane);
        formPanel.add(deptLabel);
        formPanel.add(deptBox);
        formPanel.add(priorityLabel);
        formPanel.add(priorityBox);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JButton submitBtn = new JButton("Submit Complaint");
        styleButton(submitBtn, BUTTON_COLOR);
        
        JButton cancelBtn = new JButton("Cancel");
        styleButton(cancelBtn, new Color(150, 150, 150));
        
        buttonPanel.add(submitBtn);
        buttonPanel.add(cancelBtn);
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Event handlers
        submitBtn.addActionListener(e -> {
            String title = titleField.getText().trim();
            String desc = descArea.getText().trim();
            
            if (title.isEmpty() || desc.isEmpty()) {
                showError("All fields are required.");
                return;
            }
            
            String id = UUID.randomUUID().toString();
            String dept = deptBox.getSelectedItem().toString();
            String priority = priorityBox.getSelectedItem().toString();
            String status = "New";
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            
            String complaint = String.join("|",
                    id, username, title, desc.replace("\n", "\\n"), dept, priority, status, timestamp);
            
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("complaints.txt", true))) {
                bw.write(complaint + "\n");
                JOptionPane.showMessageDialog(
                    this, 
                    "Your complaint has been submitted successfully!", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
                showError("Error saving complaint. Please try again.");
            }
        });
        
        cancelBtn.addActionListener(e -> dispose());
        
        setResizable(false);
        setVisible(true);
    }
    
    // Helper methods for styling components
    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(LABEL_COLOR);
    }
    
    private void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)),
            new EmptyBorder(8, 8, 8, 8)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ((JComponent) comboBox.getRenderer()).setBorder(new EmptyBorder(5, 5, 5, 5));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });
    }
    
    private void showError(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
}