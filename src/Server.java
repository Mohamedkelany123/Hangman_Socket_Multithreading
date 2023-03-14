import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static final int PORT = 8888;
    private static final Map<String, String> users = new HashMap<>();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        System.out.println("Server started.");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("New client connected: " + clientSocket);

            Thread clientThread = new Thread(() -> {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                    while (true) {
                        String message = in.readLine();
                        if (message.equals("REGISTER")) {
                            register(in, out);
                        } else if (message.equals("LOGIN")) {
                            login(in, out);
                        } else if (message.equals("LOGOUT")) {
                            logout(out);
                        } else if (message.equals("VIEW_USERS")) {
                            viewUsers(out);
                        } else {
                            out.println("Invalid command.");
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            clientThread.start();
        }
    }

    private static void register(BufferedReader in, PrintWriter out) throws IOException {
        String name = in.readLine();
        String username = in.readLine();
        String password = in.readLine();

        if (users.containsKey(username)) {
            out.println("Username already taken.");
        } else {
            users.put(username, password);
            out.println("Registration successful.");
        }
    }

    private static void login(BufferedReader in, PrintWriter out) throws IOException {
        String username = in.readLine();
        String password = in.readLine();

        if (users.containsKey(username) && users.get(username).equals(password)) {
            out.println("Login successful.");
        } else {
            out.println("Invalid username or password.");
        }
    }

    private static void logout(PrintWriter out) {
        out.println("Logout successful.");
    }

    private static void viewUsers(PrintWriter out) {
        if (users.isEmpty()) {
            out.println("No users registered.");
        } else {
            out.println("Registered users:");
            for (String username : users.keySet()) {
                out.println(username);
            }
        }
    }
}
