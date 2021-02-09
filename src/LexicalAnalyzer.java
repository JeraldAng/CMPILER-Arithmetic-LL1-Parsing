import java.util.ArrayList;

class Token {
    enum TokenType {
        TERMINAL, STAR, PLUS, QMARK, OPENP, CLOSEP, UNION, EPSILON, NEWLINE, ERR
    }

    public TokenType tokenType;
    public String lexeme;

    public Token (String word){
        this.tokenType = identifyToken(word);
        this.lexeme = word;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getLexeme() {
        return lexeme;
    }

    // returns the corresponding token type identified for a string
    public TokenType identifyToken(String word) {

        DFA dfa = new DFA();
        // Creating array of string length
        char[] ch = new char[word.length()];

        // Copy character by character into array
        for (int x = 0; x < word.length(); x++) {
            ch[x] = word.charAt(x);
        }

        String final_state = dfa.identifyState(word);

        if (final_state.equals("Q1"))
            return TokenType.TERMINAL;
        else if (final_state.equals("Q2"))
            return TokenType.STAR;
        else if (final_state.equals("Q3"))
            return TokenType.PLUS;
        else if (final_state.equals("Q4"))
            return TokenType.QMARK;
        else if (final_state.equals("Q5"))
            return TokenType.OPENP;
        else if (final_state.equals("Q6"))
            return TokenType.CLOSEP;
        else if (final_state.equals("Q7"))
            return TokenType.UNION;
        else if (final_state.equals("Q8"))
            return TokenType.EPSILON;
        else if (final_state.equals("Q9"))
            return TokenType.NEWLINE;
        else
            return TokenType.ERR;
    }
}

class LexicalAnalyzer {
    public static String output = "";                                // output string to print

    public Token.TokenType[] getTokenTypeList(){
        return Token.TokenType.values();
    }

    public ArrayList<Token> process(String sourceCode){
        // scan the input
        String[] words = sourceCode.split(" ");     // split by whitespace
        ArrayList<Token> tokenList = new ArrayList<>();

        for (String w: words){
            if(!w.isEmpty()) {
                for (int i=0; i<w.length(); i++){
                    Token t = new Token(w.charAt(i) + "");
                    tokenList.add(t);
                }
            }
        }
        return tokenList;
    }
}

