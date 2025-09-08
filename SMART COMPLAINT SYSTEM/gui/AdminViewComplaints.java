package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

public class AdminViewComplaints extends JFrame {
    // Define colors for UI consistency
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel blue
    private static final Color TABLE_HEADER_COLOR = new Color(220, 230, 240);

    private DefaultTableModel model;
    private JTable table;
    
    public AdminViewComplaints() {
        setTitle("All Complaints - Admin View");
        setSize(900, 600);
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
        JLabel titleLabel = new JLabel("All System Complaints", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Filter panel (for filtering complaints)
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        filterPanel.setBackground(BACKGROUND_COLOR);
        
        JLabel filterLabel = new JLabel("Filter by:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JComboBox<String> deptFilter = new JComboBox<>(new String[]{"All Departments", "IT", "Hostel", "Maintenance"});
        JComboBox<String> statusFilter = new JComboBox<>(new String[]{"All Status", "New", "In Progress", "Resolved"});
        JButton applyFilterBtn = new JButton("Apply Filter");
        styleButton(applyFilterBtn, BUTTON_COLOR);
        
        filterPanel.add(filterLabel);
        filterPanel.add(deptFilter);
        filterPanel.add(statusFilter);
        filterPanel.add(applyFilterBtn);
        
        headerPanel.add(filterPanel, BorderLayout.SOUTH);
        
        // Create table model with ID column shown to admin
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        model.addColumn("ID");
        model.addColumn("User");
        model.addColumn("Title");
        model.addColumn("Description");
        model.addColumn("Department");
        model.addColumn("Priority");
        model.addColumn("Status");
        model.addColumn("Date");
        
        loadAllComplaints();
        
        // Create and style table
        table = new JTable(model);
        table.setRowHeight(25);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Style table header
        JTableHeader header = table.getTableHeader();
        header.setBackground(TABLE_HEADER_COLOR);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        // Set column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(80);  // ID
        columnModel.getColumn(1).setPreferredWidth(80);  // User
        columnModel.getColumn(2).setPreferredWidth(150); // Title
        columnModel.getColumn(3).setPreferredWidth(200); // Description
        columnModel.getColumn(4).setPreferredWidth(100); // Department
        columnModel.getColumn(5).setPreferredWidth(80);  // Priority
        columnModel.getColumn(6).setPreferredWidth(80);  // Status
        columnModel.getColumn(7).setPreferredWidth(130); // Date
        
        // Add scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        // Status panel - shows count of complaints
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(BACKGROUND_COLOR);
        JLabel countLabel = new JLabel(model.getRowCount() + " complaints in system");
        countLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        countLabel.setForeground(Color.GRAY);
        statusPanel.add(countLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton updateStatusBtn = new JButton("Update Status");
        styleButton(updateStatusBtn, BUTTON_COLOR);
        
        JButton refreshBtn = new JButton("Refresh");
        styleButton(refreshBtn, BUTTON_COLOR);
        
        JButton closeBtn = new JButton("Close");
        styleButton(closeBtn, new Color(150, 150, 150));
        
        buttonPanel.add(updateStatusBtn);
        buttonPanel.add(refreshBtn);
        buttonPanel.add(closeBtn);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Footer panel containing status and buttons
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(BACKGROUND_COLOR);
        footerPanel.add(statusPanel, BorderLayout.WEST);
        footerPanel.add(buttonPanel, BorderLayout.EAST);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        // Event handlers
        updateStatusBtn.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Please select a complaint to update.", 
                    "No Selection", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String complaintId = (String) model.getValueAt(selectedRow, 0);
            updateComplaintStatus(complaintId);
        });
        
        refreshBtn.addActionListener(e -> {
            model.setRowCount(0); // Clear table
            loadAllComplaints();
            countLabel.setText(model.getRowCount() + " complaints in system");
        });
        
        closeBtn.addActionListener(e -> dispose());
        
        // Filter button action
        applyFilterBtn.addActionListener(e -> {
            String selectedDept = deptFilter.getSelectedItem().toString();
            String selectedStatus = statusFilter.getSelectedItem().toString();
            
            model.setRowCount(0); // Clear table
            loadFilteredComplaints(selectedDept, selectedStatus);
            countLabel.setText(model.getRowCount() + " complaints in system");
        });
        
        setVisible(true);
    }
    
    // Helper method to load all complaints without filtering by username
    private void loadAllComplaints() {
        try (BufferedReader reader = new BufferedReader(new FileReader("complaints.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 8) {
                    Vector<String> row = new Vector<>();
                    row.add(parts[0]);  // ID
                    row.add(parts[1]);  // Username
                    row.add(parts[2]);  // Title
                    row.add(parts[3].replace("\\n", "\n"));  // Description
                    row.add(parts[4]);  // Dept
                    row.add(parts[5]);  // Priority
                    row.add(parts[6]);  // Status
                    row.add(formatDate(parts[7]));  // Timestamp
                    model.addRow(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading complaints data.", 
                "Data Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Load complaints with department and status filters
    private void loadFilteredComplaints(String department, String status) {
        try (BufferedReader reader = new BufferedReader(new FileReader("complaints.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 8) {
                    boolean deptMatch = department.equals("All Departments") || parts[4].equals(department);
                    boolean statusMatch = status.equals("All Status") || parts[6].equals(status);
                    
                    if (deptMatch && statusMatch) {
                        Vector<String> row = new Vector<>();
                        row.add(parts[0]);  // ID
                        row.add(parts[1]);  // Username
                        row.add(parts[2]);  // Title
                        row.add(parts[3].replace("\\n", "\n"));  // Description
                        row.add(parts[4]);  // Dept
                        row.add(parts[5]);  // Priority
                        row.add(parts[6]);  // Status
                        row.add(formatDate(parts[7]));  // Timestamp
                        model.addRow(row);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Method to update complaint status
    private void updateComplaintStatus(String complaintId) {
        String[] statusOptions = {"New", "In Progress", "Resolved"};
        String selectedStatus = (String) JOptionPane.showInputDialog(
            this,
            "Select new status:",
            "Update Complaint Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statusOptions,
            statusOptions[0]
        );
        
        if (selectedStatus == null) return; // User canceled
        
        // Update the complaint in the file
        try {
            File inputFile = new File("complaints.txt");
            File tempFile = new File("complaints_temp.txt");
            
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
            
            String line;
            int updatedRow = -1;
            int currentRow = 0;
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 8 && parts[0].equals(complaintId)) {
                    // Replace status in the line
                    parts[6] = selectedStatus;
                    line = String.join("|", parts);
                    updatedRow = currentRow;
                }
                writer.write(line + "\n");
                currentRow++;
            }
            
            writer.close();
            reader.close();
            
            // Replace the original file
            if (!inputFile.delete()) {
                throw new IOException("Could not delete original file");
            }
            
            if (!tempFile.renameTo(inputFile)) {
                throw new IOException("Could not rename temp file");
            }
            
            // Update the table if a row was changed
            if (updatedRow != -1) {
                model.setValueAt(selectedStatus, updatedRow, 6);
                JOptionPane.showMessageDialog(this, 
                    "Complaint status updated successfully.", 
                    "Update Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error updating complaint status.", 
                "Update Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Helper method to format date display
    private String formatDate(String isoDate) {
        return isoDate.replace("T", " ").substring(0, 16);
    }
    
    // Helper method for styling buttons
    private void styleButton(JButton button, Color bgColor) {
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { button.setBackground(bgColor.darker()); }
            public void mouseExited(MouseEvent e) { button.setBackground(bgColor); }
        });
    }
    
    // For testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminViewComplaints());
    }
}