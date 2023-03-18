import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {    
    public static void main(String[] args) {
        String host = "localhost";
        int port = 8888;

        try {
            Socket socket = new Socket(host, port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            /////////////////////////////////////////////////////////////
            ServerConnection serverCon = new ServerConnection(socket);
            //START THE SERVERCON
            new Thread(serverCon).start();
            /////////////////////////////////////////////////////////////


            
            
            try (Scanner scanner = new Scanner(System.in)) {
                while (true) {

                    System.out.println("Menu:");
                    System.out.println("1. Register");
                    System.out.println("2. Login");
                    System.out.println("3. Exit");
                    System.out.print("Choose an option: ");
                    int option = 0;
                    //TRY CATCH TO HANDLE IF THE USER ENTERED CHAR FOR THE INT INPUT
                    try{
                        option = scanner.nextInt();
                    }catch (InputMismatchException e) {
                        e.printStackTrace();
                        System.err.println("Entered value is not an integer");
                      }
                    scanner.nextLine();

                    if (option == 1) {
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();

                        out.println("REGISTER");
                        out.println(name);
                        out.println(username);
                        out.println(password);

                        String response = in.readLine();
                        System.out.println(response);
                    } else if (option == 2) {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();

                        out.println("LOGIN");
                        out.println(username);
                        out.println(password);

                        String response = in.readLine();
                        //String response2 = in.readLine();

                        //System.out.println(response);
                        //System.out.println(response2);
                        if (response.equals("USER LOGGED IN SUCCESSFULLY")){
                            System.out.println();
                            System.out.println("Choose mode: ");
                            System.out.println("1.Single Player.");
                            System.out.println("2.Multiplayer.");                            
                            
                            //SEND GAMEMODE TO CLIENT HANDLER
                            String choice = scanner.nextLine();
                            out.println(choice);
                            
                            


                            


                        }
                    } else if (option == 3) {
                        out.println("EXIT");
                        break;
                    } else {
                        System.out.println("Invalid option.");
                    }
                }
            }

            in.close();
            out.close();
            //socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
