import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class HangmanSinglePlayer {
    private static final String WORDS_FILE = "words.txt";
    private static int MAX_ATTEMPTS = 7;
    private String word;
    private String wordDisplay;
    private int attemptsLeft;
    private ArrayList<Character> wrongGuessedChars;
    private boolean gameWon;

    public HangmanSinglePlayer() {
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
        // for (int i = 0; i < word.length(); i++) {
        //     if(word.charAt(i)==' '){
        //         wordDisplay = wordDisplay.concat(" ");
        //     }else{
        //         wordDisplay = wordDisplay.concat("_");
        //     }
        // }
        wordDisplay = word.replaceAll("[A-Z]", "_ ");
        attemptsLeft = MAX_ATTEMPTS;
        //STORE ALL GUESSED CHARS SO THAT YOU DONT GUESS A CHAR AGAIN
        wrongGuessedChars = new ArrayList<Character>();
        gameWon = false;
    }

    public void singlePlay(ClientHandler clientHandler) throws IOException, InterruptedException {
        clientHandler.inGame = true;
        String input= " ";
        while (attemptsLeft > 0 && !gameWon) {
            //PRINT GAME STATUS
            clientHandler.out.println("Word: " + wordDisplay);
            
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
        clientHandler.out.println("The word was: " + word);
        
        if (gameWon) {
            clientHandler.out.println("SCORE: " + attemptsLeft);
            clientHandler.out.println("----------------------------------Congratulations, you won :) ----------------------------------");
            clientHandler.out.println("--");
            Thread.sleep(500);
            String score = "SinglePlayer: " + attemptsLeft + "--";
            clientHandler.getUser().setScoreHistory(score);
            clientHandler.inGame = false;
        }else if(input.equals("-")){
            clientHandler.out.println("SCORE: LOSER");
            clientHandler.out.println("----------------------------------EXITTING GAME NOW :| ----------------------------------");
            clientHandler.out.println("--");
            Thread.sleep(500);
            clientHandler.getUser().setScoreHistory("SinglePlayer:EXITTED--");
            clientHandler.inGame = false;
        } else {
            clientHandler.out.println("SCORE: " + attemptsLeft);
            clientHandler.out.println("----------------------------------Sorry, you lost :(----------------------------------");
            clientHandler.out.println("--");
            Thread.sleep(500);
            clientHandler.getUser().setScoreHistory("SinglePlayer:0--");
            clientHandler.inGame = false;
        }
    }
}
