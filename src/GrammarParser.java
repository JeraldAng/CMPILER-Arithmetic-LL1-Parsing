import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class Rule {
    // rule representation. Capital letter = non-terminal. Small letter = terminal.
    public String LHS;         // the LHS rule
    public List<String> RHS = new ArrayList<>();   // the RHS rules
}

public class GrammarParser {
    public HashMap<String, Rule> createRules(String GrammarText){
        HashMap<String, Rule> rules = new HashMap<>();             // hashmap for all the grammar rules
        String[] productions = GrammarText.split(";\n");     // read production per line

        for (String p: productions) {
            if (p.contains(":")) {                              // extra spaces will be ignored
                String[] line = p.split(": ");            // arrow separates the LHS and RHS

                Rule X = new Rule();
                X.LHS = line[0];

                if (line[1].contains("|")) {
                    String[] prod = line[1].split(" \\| ");       // different productions are separated by the | symbol

                    X.RHS.addAll(Arrays.asList(prod));
                } else
                    X.RHS.add(line[1]);

                rules.put(line[0], X);
            }
        }

        return rules;           // returns the grammar productions
    }
}
