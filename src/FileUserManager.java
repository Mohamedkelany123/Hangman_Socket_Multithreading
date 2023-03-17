import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileUserManager {
    private static final String USER_DATA_FILE = "user_data.txt";

    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<User>();
        try {
            File file = new File(USER_DATA_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    String name = tokens[0];
                    String username = tokens[1];
                    String password = tokens[2];
                    boolean loggedIn = Boolean.parseBoolean(tokens[3]);
                    User user = new User(name, username, password, loggedIn);
                    users.add(user);
                }
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void saveUsers(ArrayList<User> users) {
        try {
            File file = new File(USER_DATA_FILE);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (User user : users) {
                String line = user.getName() + "," + user.getUsername() + "," + user.getPassword() + "," + user.isLoggedIn();
                writer.write(line);
                writer.newLine();
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
