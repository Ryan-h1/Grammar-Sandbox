package ca.uwo.cs3342.sandbox;

import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    GrammarForm grammarForm = new GrammarForm();

    grammarForm.addProduction("program", "exp", "$$");
    grammarForm.addProduction("exp", "term", "exp_tail");
    grammarForm.addProduction("exp_tail", "=", "exp");
    grammarForm.addProduction("exp_tail", "term_tail");
    grammarForm.addProduction("term_tail", "+", "term", "term_tail");
    grammarForm.addProduction("term_tail", "ε");
    grammarForm.addProduction("term", "factor", "factor_tail");
    grammarForm.addProduction("factor_tail", "*", "factor", "factor_tail");
    grammarForm.addProduction("factor_tail", "ε");
    grammarForm.addProduction("factor", "(", "exp", ")");
    grammarForm.addProduction("factor", "id");

    Grammar grammar = new GrammarFactory().createNewGrammar(grammarForm);
    System.out.println(grammar + "\n");
    grammar.printAllSets();

    LLGrammar topDownGrammar = new LLGrammar(grammar);
    LLParser parser = new LLParser(topDownGrammar);

    System.out.println();
    parser.printParseTable();

    List<Symbol> inputTokens = new ArrayList<>();

    String[] input =
        new String[] {"id", "=", "id", "*", "(", "id", "=", "id", "+", "id", "*", "id", ")"};

    for (String token : input) {
      inputTokens.add(new Symbol(token));
    }

    ParseTreeNode root = parser.parse(inputTokens);
    ParseTreeVisualizer.visualize(root);
    ParseTreeVisualizer.printParseTree(root);
  }
}
