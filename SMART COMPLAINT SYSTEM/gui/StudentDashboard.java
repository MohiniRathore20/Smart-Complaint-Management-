package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class StudentDashboard extends JFrame {
    // Define colors for UI consistency with login frame
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel blue
    
    private final String username;
    
    public StudentDashboard(String username) {
        this.username = username;
        
        setTitle("Student Dashboard - " + username);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        
        // Create a main panel with border layout and padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);
        setContentPane(mainPanel);
        
        // Header panel with welcome message
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel welcome = new JLabel("Welcome, " + username + "!", JLabel.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        welcome.setForeground(new Color(50, 50, 50));
        welcome.setBorder(new EmptyBorder(0, 0, 20, 0));
        headerPanel.add(welcome, BorderLayout.CENTER);
        
        // Status panel below header
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(BACKGROUND_COLOR);
        JLabel statusLabel = new JLabel("Student Portal • Active Session");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);
        statusPanel.add(statusLabel);
        headerPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Main content panel with buttons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 50, 10, 50));
        
        JButton fileBtn = new JButton("File New Complaint");
        styleButton(fileBtn, BUTTON_COLOR);
        
        JButton viewBtn = new JButton("View My Complaints");
        styleButton(viewBtn, BUTTON_COLOR);
        
        buttonPanel.add(fileBtn);
        buttonPanel.add(viewBtn);
        
        // Footer panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(BACKGROUND_COLOR);
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        footerPanel.add(logoutBtn);
        
        // Add panels to main layout
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Event handlers
        fileBtn.addActionListener(e -> new ComplaintForm(username));
        viewBtn.addActionListener(e -> new ViewComplaints(username));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
        
        setResizable(false);
        setVisible(true);
    }
    
    // Helper method for styling buttons
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(12, 0, 12, 0));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });
    }
}