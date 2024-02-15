package ca.uwo.cs3342.sandbox;

import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    GrammarForm grammarForm = new GrammarForm();

    // Add productions based on the grammar rules from the image
    grammarForm.addProduction("program", "exp", "$$");
    grammarForm.addProduction("exp", "id", "=", "exp");
    grammarForm.addProduction("exp", "term", "term_tail");
    grammarForm.addProduction("term_tail", "+", "term", "term_tail");
    grammarForm.addProduction("term_tail", "ε"); // ε represents an empty string or null production
    grammarForm.addProduction("term", "factor", "factor_tail");
    grammarForm.addProduction("factor_tail", "*", "factor", "factor_tail");
    grammarForm.addProduction("factor_tail", "ε"); // ε represents an empty string or null production
    grammarForm.addProduction("factor", "(", "exp", ")");
    grammarForm.addProduction("factor", "id");

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

    inputTokens.add(new Symbol("id"));
    inputTokens.add(new Symbol("="));
    inputTokens.add(new Symbol("id"));
    inputTokens.add(new Symbol("*"));
    inputTokens.add(new Symbol("("));
    inputTokens.add(new Symbol("id"));
    inputTokens.add(new Symbol("="));
    inputTokens.add(new Symbol("id"));
    inputTokens.add(new Symbol("+"));
    inputTokens.add(new Symbol("id"));
    inputTokens.add(new Symbol("*"));
    inputTokens.add(new Symbol("id"));
    inputTokens.add(new Symbol(")"));

    ParseTreeNode.printParseTree(parser.parse(inputTokens));
  }
}
