import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ArrayList<User> users;
    private ArrayList<ClientHandler> clients;
    PrintWriter out;
    BufferedReader in;
    String userName;
    String password;

    public ClientHandler(Socket clientSocket, ArrayList<User> users,ArrayList<ClientHandler> clients ) throws IOException {
        this.clientSocket = clientSocket;
        this.users = users;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
    }

    // public void sendMessage(String message) {
    //     out.println(message);
    // }

    // public void broadcastMessage(String message) {
    //     for (ClientHandler client : clients) {
    //         client.sendMessage(message);
    //     }
    // }

    private void outToAll(String msg) {
        for(ClientHandler aClient : clients){
            aClient.out.println(msg);
        }
    }

    @Override
    public void run() {
        try {

            clients.add(this);
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
                        outToAll("USER LOGGED IN SUCCESSFULLY");
                        this.userName = username;
                        this.password = password;
                        
                        //String gameMode = in.readLine();
                        outToAll("I AM ALIVEEEEE");
    
                        
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
                    outToAll("ALL USERS EXIT NOW");
                    break;
                } else {
                    out.println("Invalid request.");
                }
            }

           
            //clients.remove(this);
            
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
