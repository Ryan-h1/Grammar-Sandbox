package ca.uwo.cs3342.sandbox;

import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    GrammarForm grammarForm = new GrammarForm();

    grammarForm.addProduction("S", "switch", "(", "E", ")", "{", "C", "}", "$$");
    grammarForm.addProduction("C", "K", "L");
    grammarForm.addProduction("L", "K", "L");
    grammarForm.addProduction("L", "ε");
    grammarForm.addProduction("K", "case", "E", ":", "S");
    grammarForm.addProduction("S", "stmt");
    grammarForm.addProduction("S", "ε");
    grammarForm.addProduction("E", "expr");

    LLGrammar grammar = new LLGrammar(new GrammarFactory().createNewGrammar(grammarForm));

    System.out.println(grammar + "\n");
    grammar.printFirstSets();
    System.out.println();
    grammar.printFollowSets();
    System.out.println();
    grammar.printPredictSets();

    LLParser parser = new LLParser(grammar);

    System.out.println();
    parser.printParseTable();

    List<Symbol> inputTokens = new ArrayList<>();

    String[] input =
        new String[] {
          "switch", "(", "expr", ")", "{",
          "case", "expr", ":",
          "case", "expr", ":", "stmt",
          "case", "expr", ":", "stmt",
          "case", "expr", ":",
          "case", "expr", ":",
          "case", "expr", ":", "stmt",
          "}"
        };

    for (String token : input) {
      inputTokens.add(new Symbol(token));
    }

    ParseTreeNode root = parser.parse(inputTokens);
    ParseTreeVisualizer.visualize(root);
    ParseTreeVisualizer.printParseTree(root);
  }
}
