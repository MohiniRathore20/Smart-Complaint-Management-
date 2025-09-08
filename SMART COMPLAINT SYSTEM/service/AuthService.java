package service;

import java.util.HashMap;
import java.util.Map;

public class AuthService {
    private static final Map<String, String> userPasswords = new HashMap<>();
    private static final Map<String, String> userRoles = new HashMap<>();

    static {
        Map<String, String[]> users = FileService.loadUsersFromFile("resources/users.txt");
        for (Map.Entry<String, String[]> entry : users.entrySet()) {
            userPasswords.put(entry.getKey(), entry.getValue()[0]);
            userRoles.put(entry.getKey(), entry.getValue()[1]);
        }
    }

    public static boolean authenticate(String username, String password, String role) {
        System.out.println("Authenticating: " + username + " | " + password + " | " + role);  // Debug
        return userPasswords.containsKey(username)
            && userPasswords.get(username).equals(password)
            && userRoles.get(username).equals(role);
    }
}
