import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String IP_ADDRESS = "localhost";
    private static final int PORT = 8888;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(IP_ADDRESS, PORT);
        System.out.println("Connected to server: " + socket);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Logout");
            System.out.println("4. View all users");
            BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));
            int choice = Integer.parseInt(consoleIn.readLine());
    
            switch (choice) {
                case 1:
                    System.out.print("Name: ");
                    String name = consoleIn.readLine();
                    System.out.print("Username: ");
                    String username = consoleIn.readLine();
                    System.out.print("Password: ");
                    String password = consoleIn.readLine();
                    out.println("REGISTER");
                    out.println(name);
                    out.println(username);
                    out.println(password);
                    System.out.println(in.readLine());
                    break;
                case 2:
                    System.out.print("Username: ");
                    username = consoleIn.readLine();
                    System.out.print("Password: ");
                    password = consoleIn.readLine();
                    out.println("LOGIN");
                    out.println(username);
                    out.println(password);
                    System.out.println(in.readLine());
                    break;
                case 3:
                    out.println("LOGOUT");
                    System.out.println(in.readLine());
                    break;
                case 4:
                    out.println("VIEW_USERS");
                    String response = in.readLine();
                    System.out.println(response);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
    }
