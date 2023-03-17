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
                        out.println("EROR [CANNOT REGISTER USERNAME ALREADY EXISTS]");
                    } else {
                        User user = new User(name, username, password, false);
                        Server.addUser(user);

                        FileUserManager.saveUsers(users);


                        out.println("User registered successfully.");
                    }
                } 
                else if (request.equals("LOGIN")) {
                    String username = in.readLine();
                    String password = in.readLine();
                    boolean userExists = false;
                    //TO LOGIN THE USER WE NEED TO FIND USERNAME AND PASS AND CHECK IF HE ALREADY LOGGEDIN BEFORE
                    for (User user : users) {
                        if (user.getUsername().equals(username) && user.getPassword().equals(password) && !user.isLoggedIn()) {
                            userExists = true;
                            user.setLoggedIn(true);
                            FileUserManager.saveUsers(users);
                            break;
                        }
                    }
                    if (userExists) {
                        out.println("Login successful.");
                    } else {
                        boolean uName = false;
                        boolean pass = false;
                        boolean logged = false;
                        for (User user : users){
                            if (user.isLoggedIn() && user.getUsername().equals(username) && user.getPassword().equals(password)){
                                logged = true;
                            } else if (user.getUsername().equals(username)){
                                uName = true;
                            } else if (user.getPassword().equals(password)) {
                                pass = true;
                            } 
                        }
                        //HANDLE THE USER LOGIN ERRORS
                        if (logged == true){
                            out.println("ERROR (USER ALREADY LOGGEDIN)");
                        }else if (uName == false) {
                            out.println("ERROR 404 (NOT FOUND)");
                        } else if(uName == true && pass == false){
                            out.println("ERROR 401 (UNAUTHORIZED)");
                        } else{
                            out.println("NEW ERROR CLIENTHANDLER LINE 74");
                        }
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
