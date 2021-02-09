import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FirstSet {
    public HashMap<String, ArrayList<String>> findFirstSet(HashMap<String, Rule> grammarRules, Token.TokenType[] terminals){
        HashMap<String, ArrayList<String>> FirstSetList = new HashMap<>();
        List<Token.TokenType> terminalList = Arrays.asList(terminals);

        for (int t = 0; t < terminalList.size(); t++){
            if(!terminalList.get(t).name().equals("ERR") && !terminalList.get(t).name().equals("NEWLINE")) {                     // ERR is not a valid input
                ArrayList<String> placeholder = new ArrayList<>();
                placeholder.add(terminalList.get(t).toString());
                FirstSetList.put(terminalList.get(t).name(), placeholder);
            }
        }

        grammarRules.entrySet().forEach(entry->{
            ArrayList<String> values = new ArrayList<>();

            if (FirstSetList.get(entry.getKey()) == null){
                for(int i = 0; i < entry.getValue().RHS.size(); i++){
                    values.addAll(findFirstSetNonTerminal(grammarRules, entry.getValue().RHS.get(i)));
                }
                FirstSetList.put(entry.getKey(), values);
            }
        });

        return FirstSetList;
    }

    public ArrayList<String> findFirstSetNonTerminal(HashMap<String, Rule> grammarRules, String nonterminal){
        String[] terminal = nonterminal.split(" ");

        if (grammarRules.get(terminal[0]) == null){               // if symbol is not in the LHS, it is a terminal
            ArrayList<String> placeholder = new ArrayList<>();
            placeholder.add(terminal[0]);
            return placeholder;
        }
        else {                                                      // symbol is a nonterminal, do recursion
            ArrayList<String> values = new ArrayList<>();

            for (int y = 0; y < grammarRules.get(terminal[0]).RHS.size(); y++) {
                values.addAll(findFirstSetNonTerminal(grammarRules, grammarRules.get(terminal[0]).RHS.get(y) + ""));
            }

            return values;
        }
    }
}
