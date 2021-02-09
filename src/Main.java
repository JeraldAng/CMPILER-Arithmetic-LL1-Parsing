import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class Main {
    public static void main(String[] args)throws Exception {
        HashMap<String, Rule> grammarRules;
        GrammarParser grammarParser = new GrammarParser();

        BufferedReader br = new BufferedReader(new FileReader("grammar.txt"));  // read grammar text file
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            grammarRules = grammarParser.createRules(sb.toString());    // create grammar Rules per line
        } finally {
            br.close();
        }

        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        ArrayList<Token> tokenList = new ArrayList<>();
        BufferedReader br2 = new BufferedReader(new FileReader("input.txt"));  // read input text file
        try {
            StringBuilder sb = new StringBuilder();
            String line = br2.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br2.readLine();
            }
            tokenList.addAll(lexicalAnalyzer.process(sb.toString()));
        } finally {
            br2.close();
        }

        HashMap<String, ArrayList<String>> firstsetlist;
        HashMap<String, ArrayList<String>> followsetlist;

        FirstSet firstSet = new FirstSet();
        firstsetlist = firstSet.findFirstSet(grammarRules, lexicalAnalyzer.getTokenTypeList());

        System.out.println("First Sets: ");
        // print first set
        firstsetlist.entrySet().forEach(entry->{
            System.out.println(entry.getKey() + " " + entry.getValue());
        });

        System.out.println("\n" + "Follow Sets: ");
        FollowSet followSet = new FollowSet();
        followsetlist = followSet.findFollowSet(grammarRules, firstsetlist);

        // print follow set
        followsetlist.entrySet().forEach(entry->{
            System.out.println(entry.getKey() + " " + entry.getValue());
        });

        System.out.println("\n" + "LL(1) Table: ");
        LL1Table table = new LL1Table();
        HashMap<Pair<String, String>, String> LL1table = table.createLL1Table(firstsetlist, followsetlist, grammarRules, lexicalAnalyzer.getTokenTypeList());

        // print LL1 Table
        LL1table.entrySet().forEach(entry->{
            System.out.println("("+ entry.getKey().getKey() + "," + entry.getKey().getValue() + ") = " + entry.getValue());
        });

        System.out.println();

        Stack<Token> stack = new Stack<>();         // input for the parser class
        ArrayList<Token> list = new ArrayList<>();  // holder for input tokens

        // save the output into a text file

        System.out.println("Parsing finished! Please check your output.txt file.");
        File file = new File("output.txt");
        PrintStream stream = new PrintStream(file);
        System.setOut(stream);

        for (int i = 0; i < tokenList.size(); i++) {
            if (tokenList.get(i).tokenType.toString().equals("NEWLINE")) {  // if token is NEWLINE, parse the input chunk, remove NEWLINE token
                stack.addAll(list);

                for (int x = list.size() - 1; x >= 0; x--) {
                    if (list.get(x).getLexeme().equals("U")){
                        System.out.print(" ");
                        System.out.print(list.get(x).getLexeme());
                        System.out.print(" ");
                    }
                    else {
                        System.out.print(list.get(x).getLexeme());
                    }

                }

                list.clear();

                if (!stack.isEmpty()) {                         // do not parse empty inputs
                    SyntaxAnalyzer syntaxAnalyzer = new SyntaxAnalyzer();
                    syntaxAnalyzer.parse(stack, LL1table);
                    stack.clear();
                }
            } else {
                list.add(0, tokenList.get(i));      // put the corresponding tokens in a single input stream
            }
        }
    }
}
