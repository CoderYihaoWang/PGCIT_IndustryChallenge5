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
            guess = getRandomCode(true);
            return guess;
        }
        Iterator<String> iterator = possibleCodes.iterator();
        String tempGuess = "";
        while (iterator.hasNext()) {
            String possibleGuess = iterator.next();
            int[] guessMatch = matchCodeAndGuess(possibleGuess, guess, 0, 0, 0);
            if (guessMatch[0] != m[0] || guessMatch[1] != m[1]) {
                iterator.remove();
            } else {
                tempGuess = possibleGuess;
            }
        }
        guess = tempGuess;
        return guess;
    }

    public String getRandomCode(boolean isNumber) {
        List<String> randomArray = Arrays.asList((isNumber ? "0123456789" : "ABCDE").split(""));
        Collections.shuffle(randomArray);
        return String.join("", randomArray.subList(0, 4));
    }

    private int[] matchCodeAndGuess(String guess, String code, int i, int bulls, int cows) {
        return i == code.length() ? new int[]{bulls, cows} : guess.charAt(i) == code.charAt(i) ? matchCodeAndGuess(guess, code, i + 1, bulls + 1, cows)
                : guess.contains(code.charAt(i) + "") ? matchCodeAndGuess(guess, code, i + 1, bulls, cows + 1)
                : matchCodeAndGuess(guess, code, i + 1, bulls, cows);
    }

    public String getCode(String s) {
        return "memory".equals(s) ? getCodeWithMemory(s) : getCodeWithoutMemory(s);
    }

    private String getCodeWithoutMemory(String s) {
        return "numbers".equals(s) ? getRandomCode(true) : getRandomCode(false);
    }

    private String getCodeWithMemory(String s) {
        String code = getRandomCode(true);
        return memory.add(code) ? code : getCodeWithMemory(s);
    }
}