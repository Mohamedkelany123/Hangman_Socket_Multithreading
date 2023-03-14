import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static final int PORT = 12345;

    private static ArrayList<User> users = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);

        System.out.println("Server started on port " + PORT);

        while (true) {
            Socket clientSocket = serverSocket.accept();

            System.out.println("Client connected: " + clientSocket.getInetAddress());

            ClientHandler clientHandler = new ClientHandler(clientSocket, users);
            clientHandler.start();
        }
    }
}

class User {
    private final String name;
    private final String username;
    private final String password;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

class ClientHandler extends Thread {
    private final Socket clientSocket;
    private final ArrayList<User> users;

    public ClientHandler(Socket clientSocket, ArrayList<User> users) {
        this.clientSocket = clientSocket;
        this.users = users;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            while (true) {
                String command = in.readLine();

                switch (command) {
                    case "REGISTER":
                        String name = in.readLine();
                        String username = in.readLine();
                        String password = in.readLine();

                        if (isUserRegistered(username)) {
                            out.println("Username already exists.");
                        } else {
                            users.add(new User(name, username, password));
                            out.println("User registered successfully.");
                        }

                        break;
                    case "LOGIN":
                        username = in.readLine();
                        password = in.readLine();

                        if (isUserAuthenticated(username, password)) {
                            out.println("User authenticated.");
                        } else {
                            out.println("Invalid username or password.");
                        }

                        break;
                    case "LOGOUT":
                        out.println("User logged out.");
                        break;
                    case "VIEW_USERS":
                        StringBuilder response = new StringBuilder("Registered users:\n");

                        for (User user : users) {
                            response.append("Name: ").append(user.getName()).append(", Username: ").append(user.getUsername()).append("\n");
                        }

                        out.println(response.toString());
                        break;
                    default:
                        out.println("Invalid command.");
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error handling client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                System.out.println("Error closing client socket: " + e.getMessage());
            }
        }
    }

    private boolean isUserRegistered(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    private boolean isUserAuthenticated(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
       
