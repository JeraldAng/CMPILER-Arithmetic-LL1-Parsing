// reference for removing arraylist duplicate values : https://www.geeksforgeeks.org/how-to-remove-duplicates-from-arraylist-in-java/

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class FollowSet {
    public HashMap<String, ArrayList<String>> findFollowSet(HashMap<String, Rule> grammarRules, HashMap<String, ArrayList<String>> firstsetlist){
        HashMap<String, ArrayList<String>> FollowSetList = new HashMap<>();
        String startRule = "S";

        grammarRules.entrySet().forEach(entryLHS->{                // go through each non terminal
            grammarRules.entrySet().forEach(entryRHS->{           // go through each RHS production rule
                for (int x = 0; x < entryRHS.getValue().RHS.size(); x++) {        // loop through RHS list
                    String[] prod = entryRHS.getValue().RHS.get(x).split(" ");

                    for (int t = 0; t < prod.length; t++){
                        if (prod[t].equals(entryLHS.getKey())) {    // if nonterminal is found in production
                            ArrayList<String> values = new ArrayList<>();

                            if (entryLHS.getKey().equals(startRule)){      // if it is a start symbol, add $
                                values.add("$");
                            }
                            char lastChar = entryRHS.getValue().RHS.get(x).charAt(entryRHS.getValue().RHS.get(x).length() - 1);

                            if ((lastChar + "").equals(entryLHS.getKey())) {       // check if nonterminal is at the end of the production
                                values.add(entryRHS.getKey());      // temporarily add the nonterminal symbol
                            }
                            else {
                                boolean epsilonNext = checkForEpsilon(grammarRules, prod, t);

                                if (epsilonNext && !(prod[t+1].equals(entryRHS.getKey()))){
                                    if(!entryRHS.getKey().equals(entryLHS.getKey())) {
                                        values.add(entryRHS.getKey());
                                    }
                                }

                                values.addAll(getFirstSet(firstsetlist, prod[t+1]));
                            }

                            values.removeIf(value -> value.equals("ε"));            // remove all instances of ε

                            if (FollowSetList.get(entryLHS.getKey()) != null)
                                values.addAll(FollowSetList.get(entryLHS.getKey()));

                            FollowSetList.put(entryLHS.getKey(), values);
                        }
                    }
                }
            });
        });

        final int[] counter = {0};

        FollowSetList.entrySet().forEach(entry->{
            for(int i = 0; i < entry.getValue().size(); i++){
                if(grammarRules.get(entry.getKey()) != null){
                    counter[0]++;
                }
            }
        });

        while (counter[0] > 0) {
            convertFollows(grammarRules, FollowSetList);      // since the traversing of grammar is not in order, convert the nonterminals
            counter[0]--;
        }

        return FollowSetList;
    }

    public ArrayList<String> getFirstSet (HashMap<String, ArrayList<String>> firstsetlist, String key){
        final ArrayList<String>[] values = new ArrayList[]{new ArrayList<>()};
          firstsetlist.entrySet().forEach(entry->{
            if(entry.getKey().equals(key)){
                 values[0] = entry.getValue();
            }
          });

          return values[0];
    }

    public void convertFollows(HashMap<String, Rule> grammarRules, HashMap<String, ArrayList<String>> FollowSetList){
          FollowSetList.entrySet().forEach(entryRHS->{
              FollowSetList.entrySet().forEach(entryLHS->{

                for (int j = 0; j < entryRHS.getValue().size(); j++) {
                    if (entryRHS.getValue().get(j).equals(entryLHS.getKey())) {
                            ArrayList<String> values;
                            values = entryRHS.getValue();

                            values.remove(entryRHS.getValue().get(j));

                            values.addAll(entryLHS.getValue());
                            values.remove(entryLHS.getKey());

                            values = removeDuplicates(values);
                            FollowSetList.put(entryRHS.getKey(), values);
                    }
                }
            });
          });
    }

    public static ArrayList<String> removeDuplicates(ArrayList<String> list)
    {
        // Create a new LinkedHashSet 
        Set<String> set = new LinkedHashSet<>();

        // Add the elements to set 
        set.addAll(list);

        // Clear the list 
        list.clear();

        // add the elements of set with no duplicates to the list
        list.addAll(set);

        // return the list 
        return list;
    }

    public boolean checkForEpsilon(HashMap<String, Rule> grammarRules, String[] prod, int index){
        boolean epsilonFound = true;

        for (int r = index + 1; r < prod.length; r++){
            if (grammarRules.get(prod[r]) != null){

                for (int s = 0; s < grammarRules.get(prod[r]).RHS.size(); s++){
                    if (!grammarRules.get(prod[r]).RHS.get(s).equals("ε")){
                        epsilonFound = false;
                    }
                    else{
                        epsilonFound = true;
                        break;
                    }
                }
                if(epsilonFound){
                    break;
                }
            }
            else {
                return false;
            }
        }
        return true;
    }
}
