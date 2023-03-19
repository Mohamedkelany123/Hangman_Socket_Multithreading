import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {    
    public static void main(String[] args) throws InterruptedException {
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

                        //TO WAIT FOR SERVER RESPONSE
                        Thread.sleep(500);

                    } else if (option == 2) {
                        System.out.print("Enter username: ");
                        String username = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();

                        out.println("LOGIN");
                        out.println(username);
                        out.println(password);

                        //TO WAIT FOR SERVER RESPONSE
                        Thread.sleep(500);

                        String response = serverCon.getCurrentResponse();
                        if (response.equals("USER LOGGED IN SUCCESSFULLY")){
                            while(true){
                            
                            System.out.println();
                            System.out.println("Choose mode: ");
                            System.out.println("1.Single Player.");
                            System.out.println("2.Multiplayer.");
                            System.out.println("3.LogOut.");
                                                        
                            
                            
                            //SEND GAMEMODE TO CLIENT HANDLER
                            System.out.print("Enter Mode: ");
                            String choice = scanner.nextLine();
                            if(choice.equals("1") || choice.equals("2")){
                                out.println(choice);
                                //Thread.sleep(10000);
                                while(!serverCon.getCurrentResponse().equals("--")){
                                    Thread.sleep(200);
                                    //System.out.println();
                                    //System.out.println(serverCon.getCurrentResponse());
                                    //System.out.println();
                                    String guess = serverCon.getCurrentResponse();
                                    if(guess.equals(">")){
                                        //System.out.println("Guess a character or the full word: ");
                                        String wordOrChar = scanner.nextLine();
                                        out.println(wordOrChar);
                                        Thread.sleep(1000);
                                    } 


                                }
                                serverCon.setCurrentMsg("blaaaaa");
                            } else if(choice.equals("3")){
                                out.println(choice);
                                //Thread.sleep(1000);
                                break;
                            } else{
                                System.out.println("ENTER CHOICES [1-3]");
                            }
                            
                            


                            

                            }


                            


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
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
