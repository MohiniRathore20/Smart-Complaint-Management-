package service;

import model.Complaint;

import java.io.*;
import java.util.*;

public class FileService {

    private static final String COMPLAINTS_FILE = "resources/complaints.txt";

    // ✅ Save a complaint to the file
    public static void saveComplaintToFile(Complaint c) {
        try (PrintWriter out = new PrintWriter(new FileWriter(COMPLAINTS_FILE, true))) {
            String line = String.join(",",
                c.getId(),
                c.getTitle(),
                c.getDescription(),
                c.getSubmittedBy(),
                c.getPriority(),
                c.getStatus(),
                c.getAssignedDepartment() != null ? c.getAssignedDepartment() : "",
                c.getAssignedTo() != null ? c.getAssignedTo() : ""
            );
            out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Load all complaints from file
    public static List<Complaint> loadComplaintsFromFile() {
        List<Complaint> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(COMPLAINTS_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 5) {
                    Complaint c = new Complaint(parts[0], parts[1], parts[2], parts[3], parts[4]);
                    if (parts.length > 5) c.setStatus(parts[5]);
                    if (parts.length > 6) c.setAssignedDepartment(parts[6]);
                    if (parts.length > 7) c.setAssignedTo(parts[7]);
                    list.add(c);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ✅ Load users from a text file
    public static Map<String, String[]> loadUsersFromFile(String path) {
        Map<String, String[]> users = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", 3); // username,password,role
                if (parts.length == 3) {
                    users.put(parts[0], new String[]{parts[1], parts[2]});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
