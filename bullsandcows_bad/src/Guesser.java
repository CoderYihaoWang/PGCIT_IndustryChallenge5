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