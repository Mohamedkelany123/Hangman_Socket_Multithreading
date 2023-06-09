import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileUserManager {
    private static final String USER_DATA_FILE = "user_data.txt";
    private static final String GAME_CONFIG = "game_configuration.txt";
    
    public static ArrayList<String> loadConfig() {
        ArrayList<String> configurations = new ArrayList<String>();
        try {
            File file = new File(GAME_CONFIG);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                if ((line = reader.readLine()) == null){
                    reader.close();
                    return configurations;
                } else{
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    String maxAttempts = tokens[0];
                    String minPlayers = tokens[1];
                    String maxPlayers = tokens[2];
                    
                    configurations.add(maxAttempts);
                    configurations.add(minPlayers);
                    configurations.add(maxPlayers);
                }
                reader.close();
            }
                
            } else {
                return configurations;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return configurations;
    }
    
    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<User>();

        
        try {
            File file = new File(USER_DATA_FILE);
            if (file.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;

                if ((line = reader.readLine()) == null){
                    reader.close();
                    return users;
                } else{
                while ((line = reader.readLine()) != null) {
                    String[] tokens = line.split(",");
                    String name = tokens[0];
                    String username = tokens[1];
                    String password = tokens[2];
                    boolean loggedIn = Boolean.parseBoolean(tokens[3]);
                    String scoreHistory = tokens[4];
                    
                    User user = new User(name, username, password, loggedIn, scoreHistory);
                    users.add(user);
                }
                reader.close();
            }
                
            } else {
                return users;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static void saveUsers(ArrayList<User> users) {
        try {
            final File file = new File(USER_DATA_FILE);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("0,0,0,true,0.0,0\n");
            for (User user : users) {
                
                String line = user.getName() + "," + user.getUsername() + "," + user.getPassword() + "," + user.isLoggedIn() + "," + user.getScoreHistory();
                
                
                writer.write(line);
                writer.newLine();
            }
            
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
