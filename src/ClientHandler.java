import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ArrayList<User> users;

    public ClientHandler(Socket clientSocket, ArrayList<User> users) {
        this.clientSocket = clientSocket;
        this.users = users;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = null;
            while ((request = in.readLine()) != null) {
                if (request.equals("REGISTER")) {
                    String name = in.readLine();
                    String username = in.readLine();
                    String password = in.readLine();

                    boolean userExists = false;
                    for (User user : users) {
                        if (user.getUsername().equals(username)) {
                            userExists = true;
                            break;
                        }
                    }

                    if (userExists) {
                        out.println("Username already exists.");
                    } else {
                        User user = new User(name, username, password);
                        Server.addUser(user);
                        out.println("User registered successfully.");
                    }
                } else if (request.equals("LOGIN")) {
                    String username = in.readLine();
                    String password = in.readLine();

                    boolean userExists = false;
                    for (User user : users) {
                        if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                            userExists = true;
                            break;
                        }
                    }

                    if (userExists) {
                        out.println("Login successful.");
                    } else {
                        out.println("Invalid username or password.");
                    }
                } else if (request.equals("EXIT")) {
                    break;
                } else {
                    out.println("Invalid request.");
                }
            }

            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
