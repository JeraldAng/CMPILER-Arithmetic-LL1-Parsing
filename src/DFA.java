import javafx.util.Pair;
import java.util.Stack;

// guide sources: https://www.cs.bgu.ac.il/~comp171/wiki.files/01-scanning.pdf
//                https://grrinchas.github.io/posts/dfa-in-java?fbclid=IwAR37iucBV6iKb1Gg_0hj7-nxDDOZ5fLUGbKjW_0RDbjRGoYj6Ho4m_XG7UM

public class DFA {
    private enum States {
        Q0(false), Q1(true), Q2(true), Q3(true), Q4(true), Q5(true), Q6(true),
        Q7(true), Q8(true), Q9(true),
        Qdead(false), Qbottom(false);

        final boolean accept;

        States(boolean accept) {
            this.accept = accept;
        }

        States line, lparen, rparen, plus, star, qmark, union, epsilon, number, letter;

        // transition states (source -> input -> destination)
        static {
            Q0.letter = Q1; Q0.number = Q1; Q0.star = Q2; Q0.plus = Q3; Q0.qmark = Q4; Q0.lparen = Q5; Q0.rparen = Q6;
            Q0.union = Q7; Q0.epsilon = Q8; Q0.line = Q9;
        }

        States transition(char symbol) {
            switch (symbol) {
                case '\n':
                    return this.line;
                case '(':
                    return this.lparen;
                case ')':
                    return this.rparen;
                case '+':
                    return this.plus;
                case '*':
                    return this.star;
                case '?':
                    return this.qmark;
                case 'U':
                    return this.union;
                case 'E':
                    return this.epsilon;
                case '0': case '1': case '2': case '3': case '4': case '5': case '6': case '7': case '8': case '9':
                    return this.number;
                case 'a': case 'b': case 'c': case 'd': case 'e': case 'f': case 'g': case 'h': case 'i': case 'j':
                case 'k': case 'l': case 'm': case 'n': case 'o': case 'p': case 'q': case 'r': case 's': case 't':
                case 'u': case 'v': case 'w': case 'x': case 'y': case 'z':
                    return this.letter;
                default:
                    return Qdead;       // Qdead if input is not in the alphabet
            }
        }
    }

    public String identifyState(String word) {
        // maximal munch code, return the final state
        Stack<Pair<States, Integer>> stack = new Stack<>();
        int i = 1;
        States state;

        // removed the while(true) loop as it only needs to traverse once for this case
            state = States.Q0;
            Pair<States, Integer> pair = new Pair<>(States.Qbottom, i);
            stack.push(pair);

            while (i <= word.length() && state.transition(word.charAt(i-1)) != null) {
                if (state.accept) {
                    stack.empty();
                }
                pair = new Pair<>(state, i);
                stack.push(pair);
                state = state.transition(word.charAt(i-1));

                i++;
            }

            while (!state.accept) {
                if (stack.isEmpty()){
                    return States.Qdead.toString();
                }

                state = stack.peek().getKey();
                i = stack.peek().getValue();
                stack.pop();

                if (state == States.Qbottom) {  // tokenization is impossible, thus a dead state
                    return States.Qdead.toString();
                }
            }

            if (i > word.length()) {
                return state.toString();
            }
            else       // hit a dead state if the length of input exceeds the length to the final state path
                return States.Qdead.toString();
    }
}
