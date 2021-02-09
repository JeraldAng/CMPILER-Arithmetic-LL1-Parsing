import javafx.util.Pair;
import java.util.HashMap;
import java.util.Stack;

public class SyntaxAnalyzer {
    public void parse(Stack<Token> origin_stack, HashMap<Pair<String, String>, String> LL1table) {
        Stack<String> g_stack = new Stack<>();          // current stack of grammar rules
        Stack<Token> i_stack = origin_stack;
        boolean invalid = true;
        g_stack.push("$");  // $ denotes the end of stack
        g_stack.push("S");  // S is the start symbol

        while(!g_stack.isEmpty()){
            if (!i_stack.isEmpty()) {
                if (g_stack.peek().equals(i_stack.peek().getTokenType().name())) {
                    g_stack.pop();
                    i_stack.pop();
                } else {
                    Pair<String, String> pair = new Pair<>(g_stack.pop(), i_stack.peek().getTokenType().name());

                    if (LL1table.get(pair) != null && !LL1table.get(pair).isEmpty()) {
                        String[] conversion = LL1table.get(pair).split(" ");

                        for (int x = conversion.length - 1; x >= 0; x--) {
                            if (!conversion[x].equals("ε")) {
                                g_stack.push(conversion[x]);
                            }
                        }
                    } else {
                        invalid = true;
                        break;
                    }
                }
            }
            else {
                if (g_stack.peek().equals("$")){
                    g_stack.pop();
                    invalid = false;
                    break;
                }
                else {
                    Pair<String, String> pair = new Pair<>(g_stack.pop(), "$");

                    if (LL1table.get(pair) != null) {
                        String[] conversion = LL1table.get(pair).split(" ");

                        for (int x = conversion.length - 1; x >= 0; x--) {
                            if (!conversion[x].equals("ε")) {
                                g_stack.push(conversion[x]);
                            }
                        }
                    } else {
                        invalid = true;
                        break;
                    }
                }
            }
        }
        if (invalid || !i_stack.isEmpty()){
            System.out.println(" - REJECT");
        }
        else{
            System.out.println(" - ACCEPT");
        }

    }
}
