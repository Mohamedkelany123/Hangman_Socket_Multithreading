import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Random;

public class HangManMultiPlayer {
    private final String WORDS_FILE = "words.txt";
    private final int MAX_ATTEMPTS = 7;
    private String word;
    private String wordDisplay;
    private int attemptsLeftA;
    private int attemptsLeftB;
    private ArrayList<Character> wrongGuessedChars;
    private boolean gameWon;
    private static ArrayList<ClientHandler> A = new ArrayList<>();
    private static ArrayList<ClientHandler> B = new ArrayList<>();
    private static String teamA = "TEAM A";
    private static String teamB = "TEAM B";
    private static int membersNumber = 1;

    public HangManMultiPlayer() {
        //LOAD THE WORDS FROM THE FILE 
        ArrayList<String> words = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(WORDS_FILE));
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.toUpperCase());
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //SELECT RANDOM WORD FROM FILE
        Random random = new Random();
        //GETS A RANDOM WORDS INDEX
        int wordIndex = random.nextInt(words.size());
        //GETS THE WORD BY ITS INDEX
        word = words.get(wordIndex);
        
        //TO DISPLAY _ _ _ _ _ TO THE USER
        wordDisplay = word.replaceAll("[A-Z]", "_ ");
        attemptsLeftA = MAX_ATTEMPTS;
        attemptsLeftB = MAX_ATTEMPTS;

        //STORE ALL GUESSED CHARS SO THAT YOU DONT GUESS A CHAR AGAIN
        wrongGuessedChars = new ArrayList<Character>();
        gameWon = false;
    }


    public boolean checkIfTeamsAreReady(){
        if(A.size() == membersNumber && B.size() != membersNumber){
            A.get(0).outToAll(A, "TEAM [" + teamA + "] READYY - " + "WAINTING FOR TEAM ["+ teamB + "] TO GET READY");
            if(B.size() != 0){
                B.get(0).outToAll(B, "TEAM [" + teamA + "] READYY - " + "WAINTING FOR TEAM ["+ teamB + "] TO GET READY");
            }
                return false;
        }else if(A.size() != membersNumber && B.size() == membersNumber){
            if(A.size() != 0){
                A.get(0).outToAll(A, "TEAM [" + teamB + "] READYY - " + "WAINTING FOR TEAM ["+ teamA + "] TO GET READY");
            }
            B.get(0).outToAll(B, "TEAM [" + teamB + "] READYY - " + "WAINTING FOR TEAM ["+ teamA + "] TO GET READY");
            return false;
        }else if(A.size() == membersNumber && B.size() == membersNumber){
            A.get(0).outToAll(A, "[" + teamA + "] vs [" + teamB + "]  -  game starting now.......");
            B.get(0).outToAll(B, "[" + teamA + "] vs [" + teamB + "]  -  game starting now.......");
            return true;
        }
        return false;
    }

    public boolean checkStart(){
        if(A.size() == membersNumber && B.size() == membersNumber){
            return true;
        }
        return false;
    }

    public int getMembersNumber(){
        return membersNumber;
    }

    
    public boolean createTeam(ClientHandler clientHandler, String teamName, int membersNum){
        if(teamName.equals(teamA) || teamName.equals(teamB) ){
            clientHandler.out.println("--TEAM NAME ALREADY EXISTS--");
            return false; 
        }
        if(!teamA.equals("TEAM A") && !teamB.equals("TEAM B")){
            clientHandler.out.println("--2 TEAMS ALREADY CREATED--");
            return false;
        }


        if(teamA.equals("TEAM A") && A.size()==0){
            membersNumber = membersNum;
            clientHandler.out.println("---Team ["+ teamName + "] Created---");
            clientHandler.controllerIndex = 1;
            teamA = teamName;
            clientHandler.setTeamName(teamName);
            clientHandler.inGame = true;
            A.add(clientHandler);
            checkIfTeamsAreReady();
            return true;
        }else if(teamB.equals("TEAM B") && B.size()==0){
            teamB = teamName;
            clientHandler.setTeamName(teamName);
            clientHandler.out.println("---Team ["+ teamName + "] Created---");
            clientHandler.inGame = true;
            B.add(clientHandler);
            checkIfTeamsAreReady();
            return true;
        }else{
            return false;
        }
    }

    public boolean joinExistingTeam(ClientHandler clientHandler, String name){
        
        //CHECK TO IF NAME IS LIKE TEAMA'S NAME IF YES CHECK IF POSSIBLE TO ADD THEN ADD
        if(teamA.equals(name) ){
            if(A.size()==0){
                clientHandler.out.println("---NEED TO CREATE ATLEAST 1 TEAM---");
                return false;
            }
            if(A.size()<membersNumber){
                clientHandler.setTeamName(name);
                clientHandler.inGame = true;
                A.add(clientHandler);
                A.get(0).outToAll(A, "CLIENT ["+ clientHandler.userName + "] ADDED TO TEAM [" + name + "]");
                checkIfTeamsAreReady();
                return true;
            }
            clientHandler.out.println("TEAM [" + name + "] IS FULL");
            return false;
        //CHECK TO IF NAME IS LIKE TEAMB'S NAME IF YES CHECK IF POSSIBLE TO ADD THEN ADD
        }else if(teamB.equals(name)){
            if(B.size()<membersNumber){
                clientHandler.setTeamName(name);
                clientHandler.inGame = true;
                B.add(clientHandler);
                B.get(0).outToAll(B, "CLIENT ["+ clientHandler.userName + "] ADDED TO TEAM [" + name + "]");
                checkIfTeamsAreReady();
                return true;
            }
            clientHandler.out.println("---TEAM " + name + " IS FULL---");
            return false;
        }
        clientHandler.out.println("-----TEAM NOT FOUND-----");
        return false;
    }



    public boolean joinRandomTeam(ClientHandler clientHandler){
        if(B.size()<membersNumber){
            clientHandler.setTeamName(teamB);
            clientHandler.inGame = true;
            B.add(clientHandler);
            B.get(0).outToAll(B, "---CLIENT ["+ clientHandler.userName + "] ADDED TO TEAM [" + teamB + "]");
            checkIfTeamsAreReady();
            return true; 
        }else if(A.size() !=0 && A.size()<membersNumber){
            clientHandler.setTeamName(teamA);
            clientHandler.inGame = true;
            A.add(clientHandler);
            A.get(0).outToAll(A, "---CLIENT ["+ clientHandler.userName + "] ADDED TO TEAM [" + teamA + "]");
            checkIfTeamsAreReady();
            return true;
        }else if(A.size()==0){
            clientHandler.out.println("---NEED TO CREATE ATLEAST 1 TEAM---");
            return false;
        }
        clientHandler.out.println("---ALL TEAMS ARE FULL---");
        return false;
    }


    public void play() throws IOException, InterruptedException{
        ArrayList<ClientHandler> allPlayers = new ArrayList<>();

        for(int i=0; i<membersNumber ; i++){
            allPlayers.add(A.get(i));
            allPlayers.add(B.get(i));
        }

        for(int i=0 ; i<(membersNumber*2) ; i+=2){
            A.get(0).outToAll(allPlayers, "Team 1: " + allPlayers.get(i).userName);
        }
        for(int i=1 ; i<(membersNumber*2) ; i+=2){
            A.get(0).outToAll(allPlayers, "Team 2: " + allPlayers.get(i).userName);
        }


        teamA = "TEAM A";
        teamB = "TEAM B";
        membersNumber = 1;

        A.clear();
        B.clear();

        MultiPlay(allPlayers);

        allPlayers.get(0).outToAll(allPlayers, "--");
        Thread.sleep(500);

        for(ClientHandler c: allPlayers){
            c.inGame = false;
        }
        
        allPlayers.clear();


    }



    public void MultiPlay( ArrayList<ClientHandler> clients) throws IOException, InterruptedException {
        String input= " ";
        int index=0;
        int turn =0;
        ClientHandler clientHandler;
        for(ClientHandler c: clients){
             c.inGame = true;
        }
        try{
        while (attemptsLeftA > 0 && attemptsLeftB >0 && !gameWon) {

            clientHandler = clients.get(index);
            
            //PRINT GAME STATUS
            //clientHandler.out.println("Word: " + wordDisplay);
            clientHandler.outToAll(clients, "Word: " + wordDisplay);
            
            //PRINT NUMBER OF ATTEMPTS
            for (int i = 0; i < clients.size(); i+=2) {
                clients.get(i).out.println("Attempts left: " + attemptsLeftA);
            }
            for (int i = 1; i < clients.size(); i+=2) {
                clients.get(i).out.println("Attempts left: " + attemptsLeftB);
            }
            

            //PRINT GUESSED CHARS
            clientHandler.outToAll(clients,"Guessed characters: ");
            for (char c : wrongGuessedChars) {
                clientHandler.outToAll(clients,c + " "); 
            }
            clientHandler.outToAll(clients,"-------------"); 
            clientHandler.outToAll(clients," ");
            clientHandler.outToAll(clients,"Player Turn: " + clientHandler.userName);
            //INOPUT CHAR FROM THE USER
            clientHandler.out.println("Guess a character or the full word: ");
            clientHandler.out.println(">");
            input = clientHandler.in.readLine().toUpperCase();
            turn = index; 
            index = (index+1) % clients.size();

            //CHECK IF THE WORD IS ALL GUESSED IT WILL BE CONSIDERED CORRECT
            if (input.equals(word)) {
                gameWon = true;
                
                for(ClientHandler c: clients){
                    if(clientHandler.getTeamName().equals(c.getTeamName())){
                        c.setWon(true);
                        c.incrementScore(50);
                    }
                }
                //clientHandler.setWon(true);

                break;
            } else if(input.equals("-")){
                throw new SocketException();
            }

            // CHECK IF CHAR IS LENGTH 1 
            if (input.length() == 1) {
                char guessedChar = input.charAt(0);
                if (word.contains(String.valueOf(guessedChar))) {
                    //CHECK IF CHAT IS CORRECT
                    //wrongGuessedChars.add(guessedChar);
                    StringBuilder wordDisplayBuilder = new StringBuilder(wordDisplay);
                    int temp = 0;
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == guessedChar) {
                            wordDisplayBuilder.setCharAt((i * 2)+temp, guessedChar);
                        } else if(Character.isWhitespace(word.charAt(i))){
                            temp -= 1;
                        }
                    }
                    wordDisplay = wordDisplayBuilder.toString();
                    if (!wordDisplay.contains("_")) {
                        gameWon = true;
                        for(ClientHandler c: clients){
                            if(clientHandler.getTeamName().equals(c.getTeamName())){
                                c.setWon(true);
                                c.incrementScore(50);
                            }
                        }
                    }
                }else {
                    //GUSSED CHAR IS INCORRECT
                    wrongGuessedChars.add(guessedChar);
                    if(turn == 0 || turn == 2 || turn == 4 || turn == 6){
                        attemptsLeftA--;
                        if(attemptsLeftA == 0){
                            clients.get(index).setWon(true);
                            gameWon = true;
                            for(ClientHandler c: clients){
                                if(clients.get(index).getTeamName().equals(c.getTeamName())){
                                    c.setWon(true);
                                    c.incrementScore(50);
                                }
                            }
                        }
                       
                    }else{
                        attemptsLeftB--;
                        if(attemptsLeftB == 0){
                            clients.get(index).setWon(true);
                            gameWon = true;
                            for(ClientHandler c: clients){
                                if(clients.get(index).getTeamName().equals(c.getTeamName())){
                                    c.setWon(true);
                                    c.incrementScore(50);
                                }
                            }
                        }
                        
                    }
                }   
            }else if(input.length() != word.length() && input.length() != 1 ){
                clientHandler.out.println();
                clientHandler.out.println("NEXT TIME ENTER EITHER 1 [CHAR] to guess a letter OR " + word.length() + " [CHAR] to guess the word.");
                clientHandler.out.println();
                if(turn == 0 || turn == 2 || turn == 4 || turn == 6){
                    attemptsLeftA--;
                    if(attemptsLeftA == 0){
                        clients.get(index).setWon(true);
                        gameWon = true;
                        for(ClientHandler c: clients){
                            if(clients.get(index).getTeamName().equals(c.getTeamName())){
                                c.setWon(true);
                                c.incrementScore(50);
                            }
                        }
                    }
                }else{
                    attemptsLeftB--;
                    if(attemptsLeftB == 0){
                        clients.get(index).setWon(true);
                        gameWon = true;
                        for(ClientHandler c: clients){
                            if(clients.get(index).equals(c.getTeamName())){
                                c.setWon(true);
                                c.incrementScore(50);
                            }
                        }
                    }
                    
                }
            }
        }
    }catch(SocketException e){

    }
    
  

        //GAME RESULT
        clients.get(0).outToAll(clients, "The word was: " + word);
        
        if (gameWon) {
            for(ClientHandler c : clients){
                if(c.getWon() == true){
                    c.out.println("SCORE: +" + c.getScore());
                    c.out.println("--- Congratulations, you won :) ---");
                    String score = "MultiPlayer[" + clients.size()/2 + "v" + clients.size()/2 +"]: WON/" + c.getScore() + "--";
                    c.getUser().setScoreHistory(score);
                    c.setWon(false);
                }else{
                    c.incrementScore(-50);
                    c.out.println("SCORE: " + c.getScore());
                    c.out.println("--- Sorry, you lost :( ---");
                    String score = "MultiPlayer[" + clients.size()/2 + "v" + clients.size()/2 +"]: LOST/"+ c.getScore() + "--";
                    c.getUser().setScoreHistory(score);
                    c.setWon(false);
                }
                
            }
        }else if(input.equals("-")){
            for(ClientHandler c : clients){
                c.out.println("-- GAME EXITTING NOW :|  --");
                String score = "MultiPlayer[" + clients.size()/2 + "v" + clients.size()/2 +"]: EXITTED" + "--";
                c.getUser().setScoreHistory(score);
            }
        }else{
            for(ClientHandler c : clients){
                c.out.println("---- A CLIENT TERMINATED ----");
                    String score = "MultiPlayer[" + clients.size()/2 + "v" + clients.size()/2 +"]: TERMINATED" + "--";
                    c.getUser().setScoreHistory(score);
            }
        }
        }
    }
