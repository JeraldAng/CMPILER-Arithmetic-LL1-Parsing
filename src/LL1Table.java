import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class LL1Table {
    public HashMap<Pair<String, String>, String> createLL1Table(HashMap<String, ArrayList<String>> firstsetlist, HashMap<String, ArrayList<String>> followsetlist, HashMap<String, Rule> grammarRules, Token.TokenType[] tokenTypeList){
        HashMap<Pair<String, String>, String> table = new HashMap<>();

        grammarRules.entrySet().forEach(entry->{
            for (int x = 0; x < tokenTypeList.length; x++){
                if(!tokenTypeList[x].name().equals("ERR") && !tokenTypeList[x].name().equals("NEWLINE")) {
                    // create pair
                    Pair<String, String> pair = new Pair<>(entry.getKey(), tokenTypeList[x].name());
                    String value = "";

                    ArrayList<String> placeholder = firstsetlist.get(entry.getKey());

                    if (placeholder.contains(tokenTypeList[x].name())){
                        value = entry.getValue().RHS.get(0);

                        // assign the corresponding production to its respective terminal row
                        for(int r = 0; r < entry.getValue().RHS.size(); r++){
                            if (entry.getValue().RHS.get(r).contains(tokenTypeList[x].name())){
                                value = entry.getValue().RHS.get(r);
                            }
                            else if (firstsetlist.get(entry.getValue().RHS.get(r)) != null){
                                if (firstsetlist.get(entry.getValue().RHS.get(r)).contains(tokenTypeList[x].name()))
                                    value = entry.getValue().RHS.get(r);
                            }
                        }

                        if (entry.getValue().RHS.contains("ε")){
                            placeEpsilontoFollow(entry.getKey(), followsetlist, table);
                        }
                    }
                    table.putIfAbsent(pair, value);
                    table.putIfAbsent(new Pair<>(entry.getKey(), "$"), "");     // add a $ row
                }
            }
        });

        return table;
    }

    // if there is an epsilon in the first set, add epsilon in their corresponding follow set values
    public void placeEpsilontoFollow(String key, HashMap<String, ArrayList<String>> followsetlist, HashMap<Pair<String, String>, String> table) {
        for (int w = 0; w < followsetlist.get(key).size(); w++) {
            table.put(new Pair<>(key, followsetlist.get(key).get(w)), "ε");
        }
    }
}
