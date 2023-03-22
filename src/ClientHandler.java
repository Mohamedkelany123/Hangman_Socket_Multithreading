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
    public boolean inGame;
    public PrintWriter out;
    public BufferedReader in;
    String userName;
    String passwd;
    User user;

    public ClientHandler(Socket clientSocket, ArrayList<User> users,ArrayList<ClientHandler> clients) throws IOException {
        this.clientSocket = clientSocket;
        this.users = users;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.inGame = false;
    }

    public void outToAll(ArrayList<ClientHandler> Clients, String msg) {
        for(ClientHandler aClient : Clients){
            aClient.out.println(msg);
        }
    }

    private void outToClient(String msg,String uName, String pass) {
        for(ClientHandler aClient : clients){
            if(aClient.userName.equals(uName) && aClient.passwd.equals(pass)){
                aClient.out.println(msg);
            }             
        }
    }

    public User getUser(){
        for(User u: users){
                 if(u.getUsername().equals(this.userName) && u.getPassword().equals(this.passwd)){
                     return u;
                 }
             }
             return null;
    }

    public void clientPrint(String msg){
        out.println(msg);
    }

    @Override
    public void run() {
        try {
            String request = null;
            // while ((request = in.readLine()) != null)
            while(true) {

                
                out.println("Menu:");
                out.println("1. Register");
                out.println("2. Login");
                out.println("3. Exit");
                out.println("Choose an option: ");
                request = in.readLine();

                if (request.equals("REGISTER")) {
                    out.println("Enter (name,Username,password) comma seperated:");
                    
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
                        User u = new User(name, username, password, false);
                        Server.addUser(u);

                        FileUserManager.saveUsers(users);


                        out.println("User registered successfully.");
                    }
                } 
                else if (request.equals("LOGIN")) {
                    out.println("Enter (Username,Password) comma seperated:");
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
                        this.userName = username;
                        this.passwd = password;
                        out.println("USER LOGGED IN SUCCESSFULLY");
                        Thread.sleep(300);

                        ////////////////////////////////////////////////////////////////////////////
                        //1-SINGLEPLAYER
                        //2-MULTIPLAYER
                        
                    while(true){
                        out.println();
                        out.println("Choose mode: ");
                        out.println("1.Single Player.");
                        out.println("2.Multiplayer.");
                        out.println("3.LogOut.");  
                        out.println("Enter Mode: ");  
                        Thread.sleep(100);
                        
                        String gameMode = in.readLine();
                        
                        if(gameMode.equals("1")){
                            out.println("------------- HANGMAN SINGLEPLAYER -------------");
                            out.println("RULES:");
                            out.println("-YOU HAVE TOTAL 10 WRONG ATTEMPTS");
                            out.println("-YOU CAN EITHER GUESS 1 [CHAR] OR THE FULL WORD");
                            out.println("-THE SCORE IS CALCULATED BY NUMBER OF WRONG ATTEMPTS LEFT [10 MAX SCORE]-[0 MIN SCORE]");
                            out.println("-WORD GUESSING ARE CASE INSENSITIVE");

                            HangmanSinglePlayer singlePlayer = new HangmanSinglePlayer();
                            singlePlayer.singlePlay(this);
                            FileUserManager.saveUsers(users);

                        } else if(gameMode.equals("2")){
                            out.println("------------- HANGMAN ONE VS ONE -------------");
                            out.println("RULES:");
                            out.println("-YOU HAVE TOTAL 10 WRONG ATTEMPTS");
                            out.println("-YOU CAN EITHER GUESS 1 [CHAR] OR THE FULL WORD");
                            out.println("-THE SCORE IS CALCULATED BY NUMBER OF WRONG ATTEMPTS LEFT [10 MAX SCORE]-[0 MIN SCORE]");
                            out.println("-WORD GUESSING ARE CASE INSENSITIVE");
                            out.println("-PLAYER WITH HEIGHEST SCORE WINS");
                            
                            //HangManMultiPlayer multiPlayer = new HangManMultiPlayer();
                            HangManMultiPlayer.OnevOne(this);
                            FileUserManager.saveUsers(users);

                        } else if(gameMode.equals("3")){
                            user = getUser();
                            user.setLoggedIn(false);
                            FileUserManager.saveUsers(users);
                            break;
                        }
                    }
                    
                    



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

           
            clients.remove(this);
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException e) {
            //e.printStackTrace();
            System.out.println("CLIENT EXITED: " + clientSocket);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            System.out.println("CLIENT CONNECTION ABORTED: " + clientSocket);
        }
    }
}
