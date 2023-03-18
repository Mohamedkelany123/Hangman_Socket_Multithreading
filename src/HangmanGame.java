import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class HangmanGame {
    private static final String WORDS_FILE = "words.txt";
    private static final int MAX_ATTEMPTS = 6;
    private String word;
    private String wordDisplay;
    private int attemptsLeft;
    private ArrayList<Character> wrongGuessedChars;
    private boolean gameWon;

    public HangmanGame() {
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

    public void play() {
        Scanner scanner = new Scanner(System.in);
        while (attemptsLeft > 0 && !gameWon) {
            //PRINT GAME STATUS
            System.out.println("Word: " + wordDisplay);
            
            //PRINT NUMBER OF ATTEMPTS
            System.out.println("Attempts left: " + attemptsLeft);
            
            //PRINT GUESSED CHARS
            System.out.print("Guessed characters: ");
            for (char c : wrongGuessedChars) {
                System.out.print(c + " ");
            }
            System.out.println();

            //INOPUT CHAR FROM THE USER
            System.out.print("Guess a character or the full word: ");
            String input = scanner.nextLine().toUpperCase();

            //CHECK IF THE WORD IS ALL GUESSED IT WILL BE CONSIDERED CORRECT
            if (input.equals(word)) {
                gameWon = true;
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
                System.out.println();
                System.out.println("NEXT TIME ENTER EITHER 1 [CHAR] to guess a letter OR " + word.length() + " [CHAR] to guess the word.");
                System.out.println();
                attemptsLeft--;
            }
        }

        //GAME RESULT
        System.out.println("The word was: " + word);
        if (gameWon) {
            System.out.println("Congratulations, you won!");
        } else {
            System.out.println("Sorry, you lost.");
        }
        scanner.close();
    }

    public static void main(String[] args) {
        HangmanGame hangman = new HangmanGame();
        hangman.play();
    }
}
