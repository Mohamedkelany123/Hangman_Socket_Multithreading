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
    private int score = 0;
    public int controllerIndex = 0;
    private boolean won = false;
    private String teamName;
    private static int iter = 0;
    private static int teamSize = 0;


    public ClientHandler(Socket clientSocket, ArrayList<User> users,ArrayList<ClientHandler> clients, ArrayList<String> gameConfig) throws IOException {
        this.clientSocket = clientSocket;
        this.users = users;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        this.inGame = false;
    }


    public void setTeamName(String name){
        teamName = name;
    }

    public String getTeamName(){
        return teamName;
    }

    public void setWon(boolean state){
        won = state;
    }

    public boolean getWon(){
        return won;
    }

    public int getScore(){
        return score;
    }
    
    public void incrementScore(int s){
        score = s;
    }


    public void outToAll(ArrayList<ClientHandler> Clients, String msg) {
        for(ClientHandler aClient : Clients){
            aClient.out.println(msg);
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
                            out.println("---- HANGMAN SINGLEPLAYER ----");
                            out.println("RULES:");
                            out.println("-YOU HAVE TOTAL 7 WRONG ATTEMPTS");
                            out.println("-YOU CAN EITHER GUESS 1 [CHAR] OR THE FULL WORD");
                            out.println("-THE SCORE IS CALCULATED BY NUMBER OF WRONG ATTEMPTS LEFT [10 MAX SCORE]-[0 MIN SCORE]");
                            out.println("-WORD GUESSING ARE CASE INSENSITIVE");
                            out.println("-------------------------------");

                            HangmanSinglePlayer singlePlayer = new HangmanSinglePlayer();
                            singlePlayer.singlePlay(this);
                            FileUserManager.saveUsers(users);

                        } else if(gameMode.equals("2")){

                            out.println("---- HANGMAN MULTIPLAYER ----");
                            out.println("RULES:");
                            out.println("-EACH TEAM HAS TOTAL 7 WRONG ATTEMPTS");
                            out.println("-YOU CAN EITHER GUESS 1 [CHAR] OR THE FULL WORD");
                            out.println("-EACH PLAYER HAS HIS OWN TURN TO GUESS");
                            out.println("-FIRST TEAM TO GUESS THE WORD CORRECT OR OPPONENT TEAM RUNS OUT OF ATTEMPTS WINS");
                            out.println("-WINNING TEAM GETS [+50Pts]");
                            out.println("-LOOSING TEAM GETS [-50Pts]");
                            out.println("-WORD GUESSING ARE CASE INSENSITIVE");

                            out.println("--------------------------------");

                        HangManMultiPlayer hangManMultiPlayer= new HangManMultiPlayer();
                        while(true){
                            out.println("1.Create Team");
                            out.println("2.Join Existing Team");
                            out.println("3.Join Random Team");  
                            out.println("4.EXIT"); 
                            out.println("Enter Mode:");
                            Thread.sleep(100);
                            String mode = in.readLine();

                            //CREATE TEAM
                            if(mode.equals("1")){
                                out.println("Enter Team Name: ");
                                Thread.sleep(100);
                                String name = in.readLine();
                                //PUT INTO CONSIDERATION TO MAKE MAX 5 PLAYERS
                                if(iter == 0){
                                    out.println("Enter number of team members: ");
                                    Thread.sleep(100);
                                    String number = in.readLine();
                                    teamSize= Integer.parseInt(number);
                                    iter++;
                                }else{
                                    out.println("TEAM SIZE = "+ teamSize);
                                }

                                boolean created = hangManMultiPlayer.createTeam(this, name, teamSize);

                                ///////////////ME7TAGEEN NE7OT DE F LOOP
                                // out.println("Press (S) To start game:");
                                // String s = in.readLine();
                                //out.println("CONTROLLER= "+ this.controllerIndex);

                                if(created){
                                    //TO START GAME THE CONTROLLER IS THER ONE THAT CREATED THE FIRST TEAM
                                    while(inGame){
                                        Thread.sleep(400);
                                        
                                        if(hangManMultiPlayer.checkStart() && this.controllerIndex == 1){
                                            iter=0;
                                            teamSize=0;
                                            hangManMultiPlayer.play();
                                        }
                                    }
                                    FileUserManager.saveUsers(users);
                                    break;
                                }


                            //JOIN EXISTING TEAM
                            }else if(mode.equals("2")){
                                out.println("Enter Team Name: ");
                                Thread.sleep(100);
                                String name = in.readLine();

                                boolean value = hangManMultiPlayer.joinExistingTeam(this, name);
                                if(value == true){
                                    while(inGame){
                                        Thread.sleep(400);
                                    }
                                    break;
                                }

                            //JOIN RANDOM TEAM
                            }else if(mode.equals("3")){
                                boolean value = hangManMultiPlayer.joinRandomTeam(this);
                                
                                if(value == true){
                                    while(inGame){
                                        Thread.sleep(400);
                                    }
                                    break;
                                }

                            } else if(mode.equals("4")){
                                break;
                            }
                        }

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
