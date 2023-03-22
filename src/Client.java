import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

                    String option;
                    Thread.sleep(100);
                    //TRY CATCH TO HANDLE IF THE USER ENTERED CHAR FOR THE INT INPUT
                    if(serverCon.getCurrentResponse().equals("Choose an option: ")){
                        
                        option = scanner.nextLine();
                    

                    if (option.equals("1")) {
                        out.println("REGISTER");
                        Thread.sleep(100);
                        if(serverCon.getCurrentResponse().equals("Enter (name,Username,password) comma seperated:")){
                        
                        String[] arrOfStr;
                        while(true){
                            String userInformation = scanner.nextLine();
                            arrOfStr = userInformation.split(",", 5);
                            if(arrOfStr.length == 3){
                                break;
                            }else{
                                System.out.println("ENTER (NAME,USERNAME,PASSWORD) COMMA SEPERATED:");
                            }
                        }

                        String name = arrOfStr[0];
                        String username = arrOfStr[1];
                        String password = arrOfStr[2];

                        out.println(name);
                        out.println(username);
                        out.println(password);

                        //TO WAIT FOR SERVER RESPONSE
                        Thread.sleep(200);
                        }
                    } else if (option.equals("2")) {
                        out.println("LOGIN");
                        Thread.sleep(100);

                        String[] arrOfStr;
                        while(true){
                            String userInformation = scanner.nextLine();
                            arrOfStr = userInformation.split(",", 5);
                            if(arrOfStr.length == 2){
                                break;
                            }else{
                                System.out.println("ENTER (USERNAME,PASSWORD) COMMA SEPERATED:");
                            }
                        }
                        
                        String username = arrOfStr[0];
                        String password = arrOfStr[1];

                        out.println(username);
                        out.println(password);

                        //TO WAIT FOR SERVER RESPONSE
                        Thread.sleep(100);
                        String response = serverCon.getCurrentResponse();
                        if (response.equals("USER LOGGED IN SUCCESSFULLY")){
                            
                            while(true){
                            Thread.sleep(100);
                                if(serverCon.getCurrentResponse().equals("Enter Mode: ")){
                                    String choice = scanner.nextLine();
                                    if(choice.equals("1") || choice.equals("2")){
                                        out.println(choice);
                                        while(!serverCon.getCurrentResponse().equals("--")){
                                            Thread.sleep(100);
                                            String guess = serverCon.getCurrentResponse();
                                            if(guess.equals(">")){
                                                String wordOrChar = scanner.nextLine();
                                                out.println(wordOrChar);
                                                Thread.sleep(500);
                                            } 


                                        }
                                        serverCon.setCurrentMsg("blaaaaa");
                                    } else if(choice.equals("3")){
                                        out.println(choice);
                                        break;
                                    } else{
                                        System.out.println("ENTER CHOICES [1-3]:");
                                    }
                            }
                        }
                        }
                    } else if (option.equals("3")) {
                        out.println("EXIT");
                        break;
                    } else {
                        System.out.println("ENTER CHOICE BETWEEN [1-3]:");
                    }
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
