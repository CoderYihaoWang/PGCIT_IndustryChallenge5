import java.io.*;
import java.util.*;

public class MysteryApp {
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
            return true;
        }

        return true;
    }
}

class Guesser {
    private Strategy strategy;
    private String strategyCode;
    private int[] lastMatch;
    private char[] secretCode = new char[0];
    private String type;

    public Guesser(String type, String secretCode, Strategy strategy, String strategyCode) {
        this.type = type;
        if (type.equals("human")) {
            this.secretCode = secretCode.toCharArray();
        } else if (type.equals("computer")) {
            this.secretCode = strategy.getCode(strategyCode).toCharArray();
        }
        this.strategy = strategy;
        this.strategyCode = strategyCode;
        this.lastMatch = new int[] {0, 0};
    }

    public int[] matchSecretCode(String guess) {
        int bulls = 0;
        int cows = 0;
        for (int i = 0; i < secretCode.length; i++) {
            if (guess.charAt(i) == secretCode[i]) {
                bulls++;
            } else if (guess.contains(secretCode[i] + "")) {
                cows++;
            }
        }
        return new int[] {bulls, cows};
    }

    public String makeGuess() {
        if (strategyCode.equals("planned")) {
            return strategy.getCode(lastMatch);
        }
        return this.strategy.getCode("numbers");
    }

    public String informOfMatchResults(int[] match) {
        if (type.equals("human")) {
            return "Result: " + match[0] + (match[0] == 1 ? " bull" : " bulls") + " and " + match[1] + (match[1] == 1 ? " cow" : " cows");
        } else if (type.equals("computer")) {
            this.lastMatch = match;
            return "Result: " + match[0] + (match[0] == 1 ? " bull" : " bulls") + " and " + match[1] + (match[1] == 1 ? " cow" : " cows");
        }

        return null;
    }
}

class Strategy {
    private Set<String> possibleCodes;
    private String g;
    private Set<String> memory;

    public Strategy() {
        this.memory = new LinkedHashSet<>();
        this.possibleCodes = new HashSet<>();
        getPermutations(this.possibleCodes, "", "0123456789", 4);
    }

    private void getPermutations(Set<String> pr, String p, String s, int l) {
        int length = p.length();
        if (length == l) {
            pr.add(p);
        } else {
            for (int i = 0; i < s.length(); i++) {
                getPermutations(pr, p + s.charAt(i), s.substring(0, i) + s.substring(i + 1, s.length()), l);
            }
        }
    }

    public String getCode(int[] m) {
        if (g == null) {
            g = getRandomCode();
            return g;
        }
        Iterator<String> iterator = possibleCodes.iterator();
        String tempGuess = "";
        while (iterator.hasNext()) {
            String possibleGuess = iterator.next();
            int[] guessMatch = matchCodeAndGuess(possibleGuess, g);
            if (guessMatch[0] != m[0] || guessMatch[1] != m[1]) {
                iterator.remove();
            } else {
                tempGuess = possibleGuess;
            }
        }
        g = tempGuess;
        return g;
    }

    public String getRandomCode() {
        String randomCode = "";
        ArrayList<Integer> randomArray = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            randomArray.add(i);
        }

        Collections.shuffle(randomArray);

        for (int i = 0; i < 4; i++) {
            randomCode += randomArray.get(i);
        }
        return randomCode;
    }

    private int[] matchCodeAndGuess(String guess, String code) {
        int bulls = 0;
        int cows = 0;
        for (int i = 0; i < code.length(); i++) {
            if (guess.charAt(i) == code.charAt(i)) {
                bulls++;
            } else if (guess.contains(code.charAt(i) + "")) {
                cows++;
            }
        }
        return new int[] {bulls, cows};
    }

    public String getCode(String s) {
        if (s.equals("numbers")) {
            String randomCode = "";
            ArrayList<Integer> randomArray = new ArrayList<Integer>();
            for (int i = 0; i < 10; i++) {
                randomArray.add(i);
            }

            Collections.shuffle(randomArray);

            for (int i = 0; i < 4; i++) {
                randomCode += randomArray.get(i);
            }
            return randomCode;
        } else if (s.equals("letters")) {
            String randomCode = "";
            ArrayList<Character> randomArray = new ArrayList<>();
            for (char i = 65; i < 71; i++) {
                randomArray.add(i);
            }

            Collections.shuffle(randomArray);

            for (int i = 0; i < 4; i++) {
                randomCode += randomArray.get(i) + "";
            }
            return randomCode;
        } else if (s.equals("memory")) {
            String code = "";

            ArrayList<Integer> randomArray = new ArrayList<Integer>();
            for (int i = 0; i < 10; i++) {
                randomArray.add(i);
            }

            Collections.shuffle(randomArray);

            for (int i = 0; i < 4; i++) {
                code += randomArray.get(i);
            }

            while (!memory.add(code)) {
                code = "";

                ArrayList<Integer> randomArray2 = new ArrayList<Integer>();
                for (int i = 0; i < 10; i++) {
                    randomArray2.add(i);
                }

                Collections.shuffle(randomArray2);

                for (int i = 0; i < 4; i++) {
                    code += randomArray2.get(i);
                }
            }
            return code;
        }

        return null;
    }
}
