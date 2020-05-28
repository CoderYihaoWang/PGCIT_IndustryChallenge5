import java.util.*;

class Strategy {
    private Set<String> possibleCodes;
    private String guess;
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
        if (guess == null) {
            guess = getRandomCode();
            return guess;
        }
        Iterator<String> iterator = possibleCodes.iterator();
        String tempGuess = "";
        while (iterator.hasNext()) {
            String possibleGuess = iterator.next();
            int[] guessMatch = matchCodeAndGuess(possibleGuess, guess);
            if (guessMatch[0] != m[0] || guessMatch[1] != m[1]) {
                iterator.remove();
            } else {
                tempGuess = possibleGuess;
            }
        }
        guess = tempGuess;
        return guess;
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