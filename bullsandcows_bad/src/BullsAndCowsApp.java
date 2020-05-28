import java.io.*;
import java.util.*;

public class BullsAndCowsApp {
    public static String output = "";

    public static void main(String[] args) {
        String secretCode = "";
        System.out.println("Welcome to Bulls and Cows");
        output += "Welcome to Bulls and Cows" + "\n";
        System.out.println("======================================");
        output += "======================================" + "\n";
        int typeOfCode = 1;

        while (true) {
            System.out.println("Please enter your secret code: (4 digits) ");
            output += "Please enter your secret code: (4 digits) " + "\n";
            secretCode = (new Scanner(System.in)).nextLine();
            output += secretCode + "\n";
            if (useIt(secretCode, typeOfCode)) {
                break;
            }
            System.out.println("Invalid secret code.");
            output += "Invalid secret code." + "\n";
        }
        String strategyCode = "numbers";
        Strategy strategy = new Strategy();
        System.out.println("======================================");
        output += "======================================" + "\n";
        List<String> guesses = new ArrayList<>();
        boolean readFromFile = false;
        while (true) {
            System.out.print("Do you want to: 1. use Keyboard, or 2. read from file? ");
            output += "Do you want to: 1. use Keyboard, or 2. read from file? ";
            try {
                int choice = Integer.parseInt((new Scanner(System.in)).nextLine());
                output += choice + "\n";
                if (choice == 1) {
                    break;
                }
                if (choice == 2) {
                    System.out.print("Please enter the file name: ");
                    output += "Please enter the file name: ";
                    String filename = (new Scanner(System.in)).nextLine();
                    output += filename + "\n";
                    guesses = new ArrayList<>();

                    try (BufferedReader bR = new BufferedReader(new FileReader(filename))) {
                        String guess;
                        while ((guess = bR.readLine()) != null) {
                            guesses.add(guess);
                        }
                    }

                    readFromFile = true;
                    break;
                }
                System.out.println("Invalid input. Please enter 1 or 2 only.");
                output += "Invalid input. Please enter 1 or 2 only." + "\n";
            } catch (NumberFormatException e) {
                // do nothing
            } catch (IOException e) {
                readFromFile = false;
                System.out.println("File cannot be found or something is wrong! Please try again.");
                output += "File cannot be found or something is wrong! Please try again." + "\n";
            }
        }
        System.out.println("======================================");
        output += "======================================" + "\n";
        System.out.println("Ready? Let's go!");
        output += "Ready? Let's go!" + "\n";
        Guesser computer = new Guesser("computer", null, strategy, strategyCode);
        Guesser player = new Guesser("human", secretCode, null, null);

        int count = 0;
        while (count < 7) {
            System.out.println("**********");
            output += "**********" + "\n";
            System.out.println("Round " + (count + 1));
            output += "Round " + (count + 1) + "\n";
            System.out.println("**********");
            output += "**********" + "\n";

            String guess = "";
            if (readFromFile) {
                guess = guesses.get(count);
                System.out.println("Your guess: " + guess);
                output += "Your guess: " + guess + "\n";
            } else {
                while (true) {
                    System.out.print("Your guess: ");
                    output += "Your guess: ";
                    guess = (new Scanner(System.in)).nextLine();
                    output += guess + "\n";
                    if (useIt(guess, typeOfCode)) {
                        break;
                    }
                    System.out.println("Invalid guess.");
                    output += "Invalid guess." + "\n";
                }
            }

            int[] playerMatch = computer.matchSecretCode(guess);
            if (playerMatch[0] == 4) {
                System.out.println("You win! :)");
                output += "You win! :)" + "\n";
                break;
            }
            String playerResult = player.informOfMatchResults(playerMatch);
            System.out.println(playerResult);
            output += playerResult + "\n";
            System.out.println("--------------------");
            output += "--------------------" + "\n";
            String computerGuess = computer.makeGuess();
            System.out.println("Computer guess: " + computerGuess);
            output += "Computer guess: " + computerGuess + "\n";
            int[] computerMatch = player.matchSecretCode(computerGuess);
            if (computerMatch[0] == 4) {
                System.out.println("Computer win! :)");
                output += "Computer win! :)" + "\n";
                break;
            }
            String computerResult = computer.informOfMatchResults(computerMatch);
            System.out.println(computerResult);
            output += computerResult + "\n";
            count++;
        }
        if (count == 7) {
            System.out.println("Draw!");
            output += "Draw!" + "\n";
        }
        System.out.println("======================================");
        output += "======================================" + "\n";
        output += "Thank you for playing! Goodbye!\n";
        while (true) {
            System.out.print("Do you want to save the results to a file? 1. Yes 2. No: ");
            try {
                int choice = Integer.parseInt((new Scanner(System.in)).nextLine());
                if (choice == 1) {
                    System.out.print("Please enter a file name: ");
                    String filename = (new Scanner(System.in)).nextLine();
                    try (BufferedWriter bW = new BufferedWriter(new FileWriter(filename))) {
                        bW.write(output);
                    }
                    break;
                }
                if (choice == 2) {
                    break;
                }
                System.out.println("Invalid input. Please enter 1 or 2 only.");
            } catch (NumberFormatException e) {
                // do nothing
            } catch (IOException e) {
                System.out.println("File not found. Please try again.");
            }
        }
        System.out.println("Thank you for playing! Goodbye!");
    }


    private static boolean useIt(String code, int typeOfCode) {
        String pattern = typeOfCode == 1 ? "([0-9]{4})$" : "([A-F]{4})$";

        boolean unique = true;

        for (int i = 0; i < code.length(); i++) {
            for (int j = i + 1; j < code.length(); j++) {
                if (code.charAt(i) == code.charAt(j)) {
                    unique = false;
                }
            }
        }

        return code.matches(pattern) && codeOk(code);
    }

    private static boolean codeOk(String code) {
        for (int i = 0; i < code.length(); i++) {
            for (int j = i + 1; j < code.length(); j++) {
                if (code.charAt(i) == code.charAt(j)) {
                    return false;
                }
            }
        }

        return true;
    }
}