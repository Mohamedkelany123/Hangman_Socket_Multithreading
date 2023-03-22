import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class HangManMultiPlayer {
    private static final String WORDS_FILE = "words.txt";
    private static final int MAX_ATTEMPTS = 10;
    private static String word;
    private static String wordDisplay;
    private static int attemptsLeft;
    private static ArrayList<Character> wrongGuessedChars;
    private static boolean gameWon;
    private static ArrayList<ClientHandler> OnevOne = new ArrayList<>();
    private ClientHandler clientHandler;
    private static int OnevOneCounter = 0;
    private static ArrayList<ClientHandler> TwovTwo = new ArrayList<>();

    public HangManMultiPlayer(ClientHandler clientHandler) {
        OnevOne.add(clientHandler);
        if(OnevOne.size() == 2){
            OnevOne.get(1).controllerIndex = 3;
        }
        this.clientHandler = clientHandler;
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
        attemptsLeft = MAX_ATTEMPTS;
        //STORE ALL GUESSED CHARS SO THAT YOU DONT GUESS A CHAR AGAIN
        wrongGuessedChars = new ArrayList<Character>();
        gameWon = false;
    }

    public void OnevOne() throws IOException, InterruptedException{
        while(OnevOne.size() <2){
            OnevOne.get(0).out.println("WAITING FOR PLAYER 2.....");
            Thread.sleep(500);
        }

        MultiPlay(OnevOne);

        //clientHandler.out.println("--");
        OnevOne.get(0).outToAll(OnevOne, "--");
        Thread.sleep(500);
        OnevOne.get(0).inGame = false;
        OnevOne.get(1).inGame = false;
    }



    public void MultiPlay( ArrayList<ClientHandler> clients) throws IOException, InterruptedException {
        String input= " ";
        int index=0;
        ClientHandler clientHandler;
        for(ClientHandler c: clients){
             c.inGame = true;
        }

        while (attemptsLeft > 0 && !gameWon) {
            if(index == 0){
                clientHandler = clients.get(index);
            }else{
                clientHandler = clients.get(index);
            }
        
            //PRINT GAME STATUS
            //clientHandler.out.println("Word: " + wordDisplay);
            clientHandler.outToAll(clients, "Word: " + wordDisplay);
            
            //PRINT NUMBER OF ATTEMPTS
            clientHandler.outToAll(clients,"Attempts left: " + attemptsLeft);

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
            index = (index+1) % 2;

            //CHECK IF THE WORD IS ALL GUESSED IT WILL BE CONSIDERED CORRECT
            if (input.equals(word)) {
                gameWon = true;
                clientHandler.won = true;
                break;
            } else if(input.equals("-")){
                break;
            }

            // CHECK IF CHAR IS LENGTH 1 
            if (input.length() == 1) {
                char guessedChar = input.charAt(0);
                if (word.contains(String.valueOf(guessedChar))) {
                    //CHECK IF CHAT IS CORRECT
                    wrongGuessedChars.add(guessedChar);
                    StringBuilder wordDisplayBuilder = new StringBuilder(wordDisplay);
                    for (int i = 0; i < word.length(); i++) {
                        if (word.charAt(i) == guessedChar) {
                            wordDisplayBuilder.setCharAt(i * 2, guessedChar);
                        }
                    }
                    wordDisplay = wordDisplayBuilder.toString();
                    if (!wordDisplay.contains("_")) {
                        gameWon = true;
                    }
                }else {
                    //GUSSED CHAR IS INCORRECT
                    wrongGuessedChars.add(guessedChar);
                    attemptsLeft--;
                } 
            }else if(input.length() != word.length() && input.length() != 1 ){
                clientHandler.out.println();
                clientHandler.out.println("NEXT TIME ENTER EITHER 1 [CHAR] to guess a letter OR " + word.length() + " [CHAR] to guess the word.");
                clientHandler.out.println();
                attemptsLeft--;
            }
        }
  

        //GAME RESULT
        //clientHandler.out.println("The word was: " + word);
        OnevOne.get(0).outToAll(clients, "The word was: " + word);
        
        if (gameWon) {
            for(ClientHandler c : clients){
                if(c.won == true){
                    c.out.println("----------------------------------Congratulations, you won :) ----------------------------------");
                    String score = "MultiPlayer: " + "WON" + "--";
                    c.getUser().setScoreHistory(score);
                }else{
                    c.out.println("----------------------------------Sorry, you lost :( ----------------------------------");
                    String score = "MultiPlayer: " + "LOST" + "--";
                    c.getUser().setScoreHistory(score);
                }
                
            }



            // clientHandler.out.println("----------------------------------Congratulations, you won!----------------------------------");
            // clientHandler.out.println("--");
            // String score = "MultiPlayer: " + attemptsLeft + "--";
            // clientHandler.getUser().setScoreHistory(score);
            // clientHandler.inGame = false;
            //return attemptsLeft;
        }
        //else if(input.equals("-")){
        //     clientHandler.out.println("----------------------------------EXITTING GAME NOW----------------------------------");
        //     //clientHandler.out.println("--");
        //     clientHandler.getUser().setScoreHistory("MultiPlayer:EXITTED--");
        //     clientHandler.inGame = false;
        //     //////////////////////////////////HANDLE IF ONE EXXITED////////////////////
        //    // return 100;
        // } else {
        //     clientHandler.out.println("----------------------------------Sorry, you lost.----------------------------------");
        //     //clientHandler.out.println("--");
        //     Thread.sleep(500);
        //     clientHandler.getUser().setScoreHistory("MultiPlayer:0--");
        //     for(ClientHandler c: clients){
        //         c.inGame = false;
        //     }
           // return 0;
        }
    }
