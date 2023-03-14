import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private static final String HOST = "localhost";
    private static final int PORT = 12345;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(HOST, PORT);

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader consoleIn = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Welcome to the chat app!");

        while (true) {
            System.out.println("Enter a command (REGISTER, LOGIN, LOGOUT, VIEW_USERS):");
            String command = consoleIn.readLine();

            switch (command) {
                case "REGISTER":
                    System.out.println("Enter your name:");
                    String name = consoleIn.readLine();
                    System.out.println("Enter a username:");
                    String username = consoleIn.readLine();
                    System.out.println("Enter a password:");
                    String password = consoleIn.readLine();

                    out.println("REGISTER");
                    out.println(name);
                    out.println(username);
                    out.println(password);

                    System.out.println(in.readLine());
                    break;
                case "LOGIN":
                    System.out.println("Enter your username:");
                    username = consoleIn.readLine();
                    System.out.println("Enter your password:");
                    password = consoleIn.readLine();

                    out.println("LOGIN");
                    out.println(username);
                    out.println(password);

                    String response = in.readLine();

                    if (response.equals("User authenticated.")) {
                        System.out.println(response);

                        while (true) {
                            System.out.println("Enter a message (LOGOUT to exit):");
                            String message = consoleIn.readLine();

                            if (message.equals("LOGOUT")) {
                                out.println("LOGOUT");
                                System.out.println(in.readLine());
                                break;
                            } else {
                                System.out.println("Invalid command.");
                            }
                        }
                    } else {
                        System.out.println(response);
                    }

                    break;
                case "LOGOUT":
                    out.println("LOGOUT");
                    System.out.println(in.readLine());
                    break;
                case "VIEW_USERS":
                    out.println("VIEW_USERS");
                    System.out.println(in.readLine());
                    break;
                default:
                    System.out.println("Invalid command.");
                    break;
            }
        }
    }
}