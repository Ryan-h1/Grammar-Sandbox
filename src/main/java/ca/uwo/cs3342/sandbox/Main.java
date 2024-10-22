package ca.uwo.cs3342.sandbox;

import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    runProgram(getGrammarForm5(), getInput1());
  }

  private static void runProgram(GrammarForm grammarForm, String[] input) {
    Grammar grammar = new GrammarFactory().createNewGrammar(grammarForm);
    System.out.println(grammar.getTerminals() + "\n");
    System.out.println(grammar + "\n");
    grammar.printAllSets();

    LLGrammar topDownGrammar = new LLGrammar(grammar);
    LLParser parser = new LLParser(topDownGrammar);

    System.out.println();
    parser.printParseTable();

    List<Symbol> inputTokens = new ArrayList<>();

    for (String token : input) {
      inputTokens.add(new Symbol(token));
    }

    ParseTreeNode root = parser.parse(inputTokens);
    ParseTreeVisualizer.visualize(root);
    ParseTreeVisualizer.printParseTree(root);
  }

  private static GrammarForm getGrammarForm1() {
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
    return grammarForm;
  }

  private static GrammarForm getGrammarForm2() {
    GrammarForm grammarForm = new GrammarForm();

    grammarForm.addProduction("P", "A", "$$");
    grammarForm.addProduction("A", "a", "b");
    return grammarForm;
  }

  private static GrammarForm getGrammarForm3() {
    GrammarForm grammarForm = new GrammarForm();

    grammarForm.addProduction("P", "S");
    grammarForm.addProduction("S", "A", "C");
    grammarForm.addProduction("A", "a", "A", "b");
    grammarForm.addProduction("A", "ε");
    grammarForm.addProduction("C", "C", "c");
    grammarForm.addProduction("C", "ε");
    return grammarForm;
  }

  private static GrammarForm getGrammarForm4() {
    GrammarForm grammarForm = new GrammarForm();

    grammarForm.addProduction("S", "Expr", "$$");
    grammarForm.addProduction("Expr", "UnionExpr");
    grammarForm.addProduction("UnionExpr", "UnionExpr", "∪", "IntExpr");
    grammarForm.addProduction("UnionExpr", "IntExpr");
    grammarForm.addProduction("IntExpr", "IntExpr", "∩", "NegExpr");
    grammarForm.addProduction("IntExpr", "NegExpr");
    grammarForm.addProduction("NegExpr", "¬", "NegExpr");
    grammarForm.addProduction("NegExpr", "Base");
    grammarForm.addProduction("Base", "(", "Expr", ")");
    grammarForm.addProduction("Base", "id");

    return grammarForm;
  }

  private static GrammarForm getGrammarForm5() {
    GrammarForm grammarForm = new GrammarForm();

    grammarForm.addProduction("P", "E", "$$");
    grammarForm.addProduction("E", "(", "E", ")");
    grammarForm.addProduction("E", "V");
    grammarForm.addProduction("E", "S");  // Corrected this from epsilon to S
    grammarForm.addProduction("V", "E", "∪", "E");
    grammarForm.addProduction("V", "I");
    grammarForm.addProduction("I", "E", "∩", "E");
    grammarForm.addProduction("I", "N");
    grammarForm.addProduction("N", "-", "E");
    grammarForm.addProduction("N", "ε");  // Added epsilon (empty) production
    grammarForm.addProduction("S", "{", "a", ",", "b", ",", "...", ",", "z", ",", "∅", ",", "U", "}");
    grammarForm.addProduction("S", "ε");  // Added epsilon (empty) production

    return grammarForm;
  }

  private static String[] getInput1() {
    return new String[] {"id", "=", "id", "*", "(", "id", "=", "id", "+", "id", "*", "id", ")"};
  }

  private static String[] getInput2() {
    return "a b".split("\\s+");
  }
}
