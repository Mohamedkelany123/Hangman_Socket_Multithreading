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
    private static ArrayList<ClientHandler> TwovTwo = new ArrayList<>();

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
        attemptsLeft = MAX_ATTEMPTS;
        //STORE ALL GUESSED CHARS SO THAT YOU DONT GUESS A CHAR AGAIN
        wrongGuessedChars = new ArrayList<Character>();
        gameWon = false;
    }

    public static void OnevOne(ClientHandler clientHandler) throws IOException, InterruptedException{
        clientHandler.clientPrint(clientHandler.userName);
        OnevOne.add(clientHandler);
        
        while(OnevOne.size() != 2){
            Thread.sleep(500);
            clientHandler.out.println("WAITING FOR PLAYER2 ........");
        }
        //}
        // for(int i=0;i<OnevOne.size();i++){
        //     OnevOne.get(i).clientPrint(OnevOne.get(i).userName + "-----------------["+ i + "]");
        // }

        clientHandler.outToAll(OnevOne, "---------------GAME STARTS NOW---------");
        clientHandler.outToAll(OnevOne, "PLAYER 1["+OnevOne.get(0).userName);
        clientHandler.outToAll(OnevOne, "PLAYER 2["+OnevOne.get(2).userName);
        //int playerOneScore = 
        //MultiPlay(OnevOne.get(1));
        //MultiPlay(OnevOne.get(0));
        
        //int playerTwoScore = MultiPlay(OnevOne.get(1));
        
        OnevOne.get(0).out.println("-PLAYER 1[" + OnevOne.get(0).userName +"] SCORE = " + OnevOne.size());
        OnevOne.get(1).out.println("-PLAYER 2[" + OnevOne.get(1).userName +"] SCORE = " + OnevOne.size());
        //Thread.sleep(2000);
        //OnevOne.get(0).clientPrint("-PLAYER 1[" + OnevOne.get(0).userName +"] SCORE = " );
        //clientHandler.out.println("-PLAYER 1[" + OnevOne.get(0).userName +"] SCORE = " );
        clientHandler.out.println("--");
        //OnevOne.get(1).outToAll(OnevOne, "-PLAYER 2[" + OnevOne.get(1).userName +"] SCORE = " + playerTwoScore);


        //if(playerOneScore>playerTwoScore){
            //OnevOne.get(0).out.println("---------------------------- YOU WONNNN :) ---------------------");
            //OnevOne.get(1).out.println("---------------------------- YOU LOST :( ---------------------");
        //} else if(playerOneScore<playerTwoScore){
           // OnevOne.get(1).out.println("---------------------------- YOU WONNNN :) ---------------------");
            //OnevOne.get(0).out.println("---------------------------- YOU LOST :( ---------------------");
        //} else{
         //   OnevOne.get(0).outToAll(OnevOne, "--------------------- TIE ----------------------");
        //}


        



    }



    public static void MultiPlay( ArrayList<ClientHandler> clients) throws IOException, InterruptedException {
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
            
            
            //PRINT NUMBER OF ATTEMPTS
            clientHandler.out.println("Attempts left: " + attemptsLeft);

            //PRINT GUESSED CHARS
            clientHandler.out.print("Guessed characters: ");
            for (char c : wrongGuessedChars) {
                clientHandler.out.print(c + " "); 
            }
            clientHandler.out.println();

            //INOPUT CHAR FROM THE USER
            clientHandler.out.println("Guess a character or the full word: ");
            clientHandler.out.println(">");
            input = clientHandler.in.readLine().toUpperCase();

            //CHECK IF THE WORD IS ALL GUESSED IT WILL BE CONSIDERED CORRECT
            if (input.equals(word)) {
                gameWon = true;
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
    }

        //GAME RESULT
        clientHandler.out.println("The word was: " + word);
        
        if (gameWon) {
            clientHandler.out.println("----------------------------------Congratulations, you won!----------------------------------");
            clientHandler.out.println("--");
            String score = "MultiPlayer: " + attemptsLeft + "--";
            clientHandler.getUser().setScoreHistory(score);
            clientHandler.inGame = false;
            //return attemptsLeft;
        }else if(input.equals("-")){
            clientHandler.out.println("----------------------------------EXITTING GAME NOW----------------------------------");
            //clientHandler.out.println("--");
            clientHandler.getUser().setScoreHistory("MultiPlayer:EXITTED--");
            clientHandler.inGame = false;
            //////////////////////////////////HANDLE IF ONE EXXITED////////////////////
           // return 100;
        } else {
            clientHandler.out.println("----------------------------------Sorry, you lost.----------------------------------");
            //clientHandler.out.println("--");
            Thread.sleep(500);
            clientHandler.getUser().setScoreHistory("MultiPlayer:0--");
            for(ClientHandler c: clients){
                c.inGame = false;
            }
           // return 0;
        }
    }
}
