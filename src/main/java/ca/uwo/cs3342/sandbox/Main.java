package ca.uwo.cs3342.sandbox;

import java.util.ArrayList;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    GrammarForm grammarForm = new GrammarForm();

    grammarForm.addProduction("program", "stmt_list", "$$");
    grammarForm.addProduction("stmt_list", "stmt", "stmt_list");
    grammarForm.addProduction("stmt_list", "ε");
    grammarForm.addProduction("stmt", "id", ":=", "expr");
    grammarForm.addProduction("stmt", "read", "id");
    grammarForm.addProduction("stmt", "write", "expr");
    grammarForm.addProduction("expr", "term", "term_tail");
    grammarForm.addProduction("term_tail", "add_op", "term", "term_tail");
    grammarForm.addProduction("term_tail", "ε");
    grammarForm.addProduction("term", "factor", "factor_tail");
    grammarForm.addProduction("factor_tail", "mult_op", "factor", "factor_tail");
    grammarForm.addProduction("factor_tail", "ε");
    grammarForm.addProduction("factor", "(", "expr", ")");
    grammarForm.addProduction("factor", "id");
    grammarForm.addProduction("factor", "number");
    grammarForm.addProduction("add_op", "+");
    grammarForm.addProduction("add_op", "-");
    grammarForm.addProduction("mult_op", "*");
    grammarForm.addProduction("mult_op", "/");

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
    inputTokens.add(new Symbol(":=")); // We have no predict set with this token???
    inputTokens.add(new Symbol("number"));
    inputTokens.add(new Symbol("$$")); // (end of input symbol)

    ParseTreeNode.printParseTree(parser.parse(inputTokens));
  }
}
