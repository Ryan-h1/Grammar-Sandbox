package ca.uwo.cs3342.sandbox;

import java.util.*;

public class Main {
  public static void main(String[] args) {
    GrammarFactory factory = new GrammarFactory();

    // Non-terminal symbols
    factory.createSymbol("program", false);
    factory.createSymbol("exp", false);
    factory.createSymbol("term_tail", false);
    factory.createSymbol("term", false);
    factory.createSymbol("factor_tail", false);
    factory.createSymbol("factor", false);
    factory.createSymbol("ε", false, true);

    // Terminal symbols
    factory.createSymbol("id", true);
    factory.createSymbol("=", true);
    factory.createSymbol("+", true);
    factory.createSymbol("*", true);
    factory.createSymbol("(", true);
    factory.createSymbol(")", true);
    factory.createSymbol("$$", true);

    // Productions
    factory.createProduction("program", "exp", "$$");
    factory.createProduction("exp", "id", "=", "exp");
    factory.createProduction("exp", "term", "term_tail");
    factory.createProduction("term_tail", "+", "term", "term_tail");
    factory.createProduction("term_tail", "ε");
    factory.createProduction("term", "factor", "factor_tail");
    factory.createProduction("factor_tail", "*", "factor", "factor_tail");
    factory.createProduction("factor_tail", "ε");
    factory.createProduction("factor", "(", "exp", ")");
    factory.createProduction("factor", "id");

    for (Production production : factory.getProductions()) {
      System.out.println(production);
    }

    LLGrammar grammar = new LLGrammar(factory);

    for (Symbol symbol : grammar.getSymbols()) {
      System.out.println("FIRST(" + symbol.name + ")=" + symbol.firstSet.toString());
//      System.out.println("FOLLOW(" + symbol.name + ")=" + symbol.followSet.toString());
    }
  }
}
