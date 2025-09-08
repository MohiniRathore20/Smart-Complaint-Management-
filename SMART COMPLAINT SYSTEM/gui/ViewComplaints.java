package gui;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

public class ViewComplaints extends JFrame {
    // Define colors for UI consistency
    private static final Color BACKGROUND_COLOR = new Color(240, 240, 245);
    private static final Color BUTTON_COLOR = new Color(70, 130, 180); // Steel blue
    private static final Color TABLE_HEADER_COLOR = new Color(220, 230, 240);
    
    public ViewComplaints(String username) {
        setTitle("My Complaints");
        setSize(800, 500);
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
        JLabel titleLabel = new JLabel("Complaint History", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        // Create table model
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table cells non-editable
            }
        };
        
        model.addColumn("Title");
        model.addColumn("Description");
        model.addColumn("Department");
        model.addColumn("Priority");
        model.addColumn("Status");
        model.addColumn("Date");
        
        // Load complaint data
        try (BufferedReader reader = new BufferedReader(new FileReader("complaints.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length >= 8 && parts[1].equals(username)) {
                    Vector<String> row = new Vector<>();
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
        }
        
        // Create and style table
        JTable table = new JTable(model);
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
        columnModel.getColumn(0).setPreferredWidth(150); // Title
        columnModel.getColumn(1).setPreferredWidth(250); // Description
        columnModel.getColumn(2).setPreferredWidth(100); // Department
        columnModel.getColumn(3).setPreferredWidth(80);  // Priority
        columnModel.getColumn(4).setPreferredWidth(80);  // Status
        columnModel.getColumn(5).setPreferredWidth(140); // Date
        
        // Add scroll pane for table
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new LineBorder(new Color(200, 200, 200)));
        
        // Status panel - shows count of complaints
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(BACKGROUND_COLOR);
        JLabel countLabel = new JLabel(model.getRowCount() + " complaints found");
        countLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        countLabel.setForeground(Color.GRAY);
        statusPanel.add(countLabel);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        JButton refreshBtn = new JButton("Refresh");
        styleButton(refreshBtn, BUTTON_COLOR);
        
        JButton closeBtn = new JButton("Close");
        styleButton(closeBtn, new Color(150, 150, 150));
        
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
        refreshBtn.addActionListener(e -> {
            dispose();
            new ViewComplaints(username).setVisible(true);
        });
        
        closeBtn.addActionListener(e -> dispose());
        
        setVisible(true);
    }
    
    // Helper method to format date display
    private String formatDate(String isoDate) {
        // For simplicity, just returning the raw date string
        // You could further format this with DateTimeFormatter if needed
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
}