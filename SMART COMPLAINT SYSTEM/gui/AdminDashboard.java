// package gui;

// import javax.swing.*;
// import javax.swing.border.*;
// import javax.swing.table.*;
// import java.awt.*;
// import java.awt.event.*;
// import java.util.List;

// import model.Complaint;
// import service.ComplaintManager;

// public class AdminDashboard extends JFrame {
//     // Define colors for UI consistency with other frames
//     private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
//     private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel blue
//     private static final Color HEADER_BG = new Color(230, 230, 235);
    
//     private JTable complaintTable;
//     private DefaultTableModel tableModel;
    
//     public AdminDashboard() {
//         setTitle("Administrator Dashboard");
//         setSize(850, 550);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLocationRelativeTo(null); // Center on screen
        
//         // Create main panel with border layout and padding
//         JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//         mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
//         mainPanel.setBackground(BACKGROUND_COLOR);
//         setContentPane(mainPanel);
        
//         // Header panel
//         JPanel headerPanel = new JPanel(new BorderLayout());
//         headerPanel.setBackground(BACKGROUND_COLOR);
        
//         JLabel heading = new JLabel("Admin Control Panel", JLabel.CENTER);
//         heading.setFont(new Font("Arial", Font.BOLD, 24));
//         heading.setForeground(new Color(50, 50, 50));
//         headerPanel.add(heading, BorderLayout.NORTH);
        
//         JLabel subHeading = new JLabel("System-wide Complaints Overview", JLabel.CENTER);
//         subHeading.setFont(new Font("Segoe UI", Font.PLAIN, 14));
//         subHeading.setBorder(new EmptyBorder(5, 0, 15, 0));
//         headerPanel.add(subHeading, BorderLayout.CENTER);
        
//         // Admin tools panel (top)
//         JPanel toolsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
//         toolsPanel.setBackground(BACKGROUND_COLOR);
        
//         JButton assignBtn = new JButton("Assign Department");
//         styleButton(assignBtn, BUTTON_COLOR);
        
//         JButton priorityBtn = new JButton("Set Priority");
//         styleButton(priorityBtn, BUTTON_COLOR);
        
//         JButton reportBtn = new JButton("Generate Report");
//         styleButton(reportBtn, new Color(100, 150, 100));
        
//         toolsPanel.add(assignBtn);
//         toolsPanel.add(priorityBtn);
//         toolsPanel.add(reportBtn);
//         headerPanel.add(toolsPanel, BorderLayout.SOUTH);
        
//         // Table setup
//         String[] columns = {"ID", "Title", "Status", "Priority", "Submitted By", "Department"};
//         tableModel = new DefaultTableModel(columns, 0) {
//             @Override
//             public boolean isCellEditable(int row, int column) {
//                 return false; // Make table read-only
//             }
//         };
        
//         complaintTable = new JTable(tableModel);
//         complaintTable.setRowHeight(25);
//         complaintTable.setShowVerticalLines(false);
//         complaintTable.setSelectionBackground(new Color(210, 230, 250));
//         complaintTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
//         complaintTable.getTableHeader().setBackground(HEADER_BG);
//         complaintTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
//         // Set column widths
//         TableColumnModel columnModel = complaintTable.getColumnModel();
//         columnModel.getColumn(0).setPreferredWidth(50);  // ID
//         columnModel.getColumn(1).setPreferredWidth(250); // Title
//         columnModel.getColumn(2).setPreferredWidth(80);  // Status
//         columnModel.getColumn(3).setPreferredWidth(80);  // Priority
        
//         JScrollPane scrollPane = new JScrollPane(complaintTable);
//         scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
//         // Bottom control panel
//         JPanel bottomPanel = new JPanel(new BorderLayout());
//         bottomPanel.setBackground(BACKGROUND_COLOR);
        
//         // Status filter panel
//         JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         filterPanel.setBackground(BACKGROUND_COLOR);
//         filterPanel.add(new JLabel("Filter:"));
        
//         String[] filterOptions = {"All", "Pending", "In Progress", "Resolved"};
//         JComboBox<String> filterBox = new JComboBox<>(filterOptions);
//         filterBox.setPreferredSize(new Dimension(120, 25));
//         filterPanel.add(filterBox);
        
//         // Button panel on right
//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//         buttonPanel.setBackground(BACKGROUND_COLOR);
        
//         JButton refreshBtn = new JButton("Refresh");
//         styleButton(refreshBtn, new Color(100, 100, 150));
        
//         JButton logoutBtn = new JButton("Logout");
//         styleButton(logoutBtn, new Color(150, 150, 150));
        
//         buttonPanel.add(refreshBtn);
//         buttonPanel.add(logoutBtn);
        
//         bottomPanel.add(filterPanel, BorderLayout.WEST);
//         bottomPanel.add(buttonPanel, BorderLayout.EAST);
        
//         // Status bar
//         JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
//         statusBar.setBackground(BACKGROUND_COLOR);
//         JLabel statusLabel = new JLabel("Ready");
//         statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
//         statusBar.add(statusLabel);
        
//         // Add everything to main panel
//         mainPanel.add(headerPanel, BorderLayout.NORTH);
//         mainPanel.add(scrollPane, BorderLayout.CENTER);
//         mainPanel.add(bottomPanel, BorderLayout.SOUTH);
//         mainPanel.add(statusBar, BorderLayout.PAGE_END);
        
//         // Load complaint data
//         loadComplaints();
        
//         // Button actions
//         refreshBtn.addActionListener(e -> loadComplaints());
//         logoutBtn.addActionListener(e -> {
//             dispose();
//             new LoginFrame().setVisible(true);
//         });
        
//         assignBtn.addActionListener(e -> assignDepartment());
//         priorityBtn.addActionListener(e -> setPriority());
//         reportBtn.addActionListener(e -> generateReport());
        
//         filterBox.addActionListener(e -> {
//             String filter = (String) filterBox.getSelectedItem();
//             if ("All".equals(filter)) {
//                 loadComplaints();
//             } else {
//                 filterComplaints(filter);
//             }
//             statusLabel.setText(tableModel.getRowCount() + " complaints shown • Filter: " + filter);
//         });
        
//         setVisible(true);
//     }
    
//     private void loadComplaints() {
//         tableModel.setRowCount(0);
        
//         List<Complaint> complaints = ComplaintManager.getInstance().getAllComplaints();
        
//         for (Complaint c : complaints) {
//             tableModel.addRow(new Object[]{
//                 c.getId(),
//                 c.getTitle(),
//                 c.getStatus(),
//                 c.getPriority(),
//                 c.getSubmittedBy(),
//                 c.getAssignedDepartment() != null ? c.getAssignedDepartment() : "Unassigned"
//             });
//         }
//     }
    
//     private void filterComplaints(String status) {
//         tableModel.setRowCount(0);
        
//         List<Complaint> complaints = ComplaintManager.getInstance().getAllComplaints();
        
//         for (Complaint c : complaints) {
//             if (c.getStatus().equalsIgnoreCase(status)) {
//                 tableModel.addRow(new Object[]{
//                     c.getId(),
//                     c.getTitle(),
//                     c.getStatus(),
//                     c.getPriority(),
//                     c.getSubmittedBy(),
//                     c.getAssignedDepartment() != null ? c.getAssignedDepartment() : "Unassigned"
//                 });
//             }
//         }
//     }
    
//     private void assignDepartment() {
//         int row = complaintTable.getSelectedRow();
//         if (row == -1) {
//             JOptionPane.showMessageDialog(this, "Please select a complaint first.", 
//                 "No Selection", JOptionPane.INFORMATION_MESSAGE);
//             return;
//         }
        
//         String complaintId = tableModel.getValueAt(row, 0).toString();
//         String[] departments = {"IT", "Maintenance", "Library", "Academic", "Finance", "Administration"};
        
//         String department = (String) JOptionPane.showInputDialog(this,
//             "Assign complaint to department:", "Assign Department",
//             JOptionPane.QUESTION_MESSAGE, null, departments, departments[0]);
            
//         if (department != null) {
//             // Here you would call a method to assign the department
//             // This is placeholder since AssignDepartment wasn't in your original code
//             JOptionPane.showMessageDialog(this, "Complaint #" + complaintId + 
//                 " assigned to " + department + " department.", "Success", JOptionPane.INFORMATION_MESSAGE);
            
//             // For demo purposes, let's just refresh
//             loadComplaints();
//         }
//     }
    
//     private void setPriority() {
//         int row = complaintTable.getSelectedRow();
//         if (row == -1) {
//             JOptionPane.showMessageDialog(this, "Please select a complaint first.", 
//                 "No Selection", JOptionPane.INFORMATION_MESSAGE);
//             return;
//         }
        
//         String complaintId = tableModel.getValueAt(row, 0).toString();
//         String[] priorities = {"Low", "Medium", "High", "Critical"};
        
//         String priority = (String) JOptionPane.showInputDialog(this,
//             "Set complaint priority:", "Set Priority",
//             JOptionPane.QUESTION_MESSAGE, null, priorities, priorities[0]);
            
//         if (priority != null) {
//             // Update the priority - you'd need to add this method to ComplaintManager
//             // ComplaintManager.getInstance().updateComplaintPriority(complaintId, priority);
            
//             JOptionPane.showMessageDialog(this, "Priority updated to " + priority, 
//                 "Success", JOptionPane.INFORMATION_MESSAGE);
                
//             // Refresh
//             loadComplaints();
//         }
//     }
    
//     private void generateReport() {
//         // This would generate a report of all complaints
//         JOptionPane.showMessageDialog(this,
//             "Report generation will be implemented in a future update.",
//             "Coming Soon", JOptionPane.INFORMATION_MESSAGE);
//     }
    
//     // Helper method for styling buttons
//     private void styleButton(JButton button, Color bgColor) {
//         button.setBackground(bgColor);
//         button.setForeground(Color.WHITE);
//         button.setFocusPainted(false);
//         button.setFont(new Font("Segoe UI", Font.BOLD, 12));
//         button.setBorder(new EmptyBorder(8, 15, 8, 15));
        
//         // Hover effect
//         button.addMouseListener(new MouseAdapter() {
//             public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
//             public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
//         });
//     }
// }

package gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class AdminDashboard extends JFrame {
    // Define colors for UI consistency
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel blue
    
    public AdminDashboard() {
        setTitle("Admin Dashboard");
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
        
        JLabel welcome = new JLabel("Administrator Control Panel", JLabel.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 24));
        welcome.setForeground(new Color(50, 50, 50));
        welcome.setBorder(new EmptyBorder(0, 0, 20, 0));
        headerPanel.add(welcome, BorderLayout.CENTER);
        
        // Status panel below header
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(BACKGROUND_COLOR);
        JLabel statusLabel = new JLabel("Admin Portal • Active Session");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(Color.GRAY);
        statusPanel.add(statusLabel);
        headerPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Main content panel with buttons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 0, 15));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 50, 10, 50));
        
        JButton viewAllBtn = new JButton("View All Complaints");
        styleButton(viewAllBtn, BUTTON_COLOR);
        
        JButton manageUsersBtn = new JButton("Manage Users");
        styleButton(manageUsersBtn, BUTTON_COLOR);
        
        JButton systemSettingsBtn = new JButton("System Settings");
        styleButton(systemSettingsBtn, BUTTON_COLOR);
        
        buttonPanel.add(viewAllBtn);
        buttonPanel.add(manageUsersBtn);
        buttonPanel.add(systemSettingsBtn);
        
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
        viewAllBtn.addActionListener(e -> new AdminViewComplaints().setVisible(true));
        
        manageUsersBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "User management will be implemented in future updates.", 
            "Coming Soon", JOptionPane.INFORMATION_MESSAGE));
        
        systemSettingsBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, 
            "System settings will be implemented in future updates.", 
            "Coming Soon", JOptionPane.INFORMATION_MESSAGE));
        
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