package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import service.AuthService;

public class LoginFrame extends JFrame {
    // Define colors for UI consistency
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel blue
    
    public LoginFrame() {
        setTitle("Campus Management System");
        setSize(400, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        
        // Create a main panel with border layout and padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        setContentPane(mainPanel);
        
        // Header
        JLabel headerLabel = new JLabel("User Login", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Form panel - keeping your GridLayout approach
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBackground(BACKGROUND_COLOR);
        
        // UI Components with better styling
        JTextField usernameField = new JTextField();
        styleTextField(usernameField);
        
        JPasswordField passwordField = new JPasswordField();
        styleTextField(passwordField);
        
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Student", "Faculty", "Admin", "Department"});
        styleComboBox(roleBox);
        
        JButton loginButton = new JButton("Login");
        styleButton(loginButton, BUTTON_COLOR);
        
        // Styled labels
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel roleLabel = new JLabel("Role:");
        styleLabel(usernameLabel);
        styleLabel(passwordLabel);
        styleLabel(roleLabel);
        
        // Add to layout - exactly as in your original code
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(roleLabel);
        formPanel.add(roleBox);
        formPanel.add(new JLabel(""));  // Empty label for spacing
        formPanel.add(loginButton);
        
        // Add components to main panel
        mainPanel.add(headerLabel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Login button action - kept exactly as in your original
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = roleBox.getSelectedItem().toString();
            
            System.out.println("INPUT — Username: " + username + " | Password: " + password + " | Role: " + role);
            
            if (AuthService.authenticate(username, password, role)) {
                dispose();  // Close login frame
                
                switch (role) {
                    case "Admin" -> new AdminDashboard().setVisible(true);
                    case "Department" -> {
                        // You can customize this mapping based on actual usernames or role-to-department logic
                        String departmentName = switch (username) {
                            case "dept1" -> "IT";
                            case "dept2" -> "Maintenance";
                            case "dept3" -> "Library";
                            default -> "General"; // fallback
                        };
                        new DepartmentDashboard(departmentName).setVisible(true);
                    }
                    case "Student" -> new StudentDashboard(username).setVisible(true);
                    case "Faculty" -> new FacultyDashboard(username).setVisible(true);
                    default -> JOptionPane.showMessageDialog(this, "Unknown role.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Set window properties
        setResizable(false);
        setVisible(true);
    }
    
    // Helper methods for styling components
    private void styleTextField(JTextField field) {
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 7, 5, 7)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    }
    
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(new EmptyBorder(8, 0, 8, 0));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });
    }
    
    private void styleLabel(JLabel label) {
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
    }
    
    // Main method for testing
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new LoginFrame());
    }
}