package service;

import model.Complaint;
import java.util.*;

public class ComplaintManager {
    private static ComplaintManager instance;
    private final List<Complaint> complaints;

    private ComplaintManager() {
        complaints = FileService.loadComplaintsFromFile(); // ✅ Load from file
    }

    public static ComplaintManager getInstance() {
        if (instance == null) instance = new ComplaintManager();
        return instance;
    }

    public void fileComplaint(Complaint c) {
        complaints.add(c);
        FileService.saveComplaintToFile(c); // ✅ Save to file
    }

    public List<Complaint> getAllComplaints() {
        return complaints;
    }

    public void updateComplaintStatus(String complaintId, String newStatus) {
        for (Complaint c : complaints) {
            if (c.getId().equals(complaintId)) {
                c.setStatus(newStatus);
                break;
            }
        }
        // NOTE: To reflect updates in file, we’d need to rewrite the whole file — optional!
    }
}
