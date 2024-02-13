package ca.uwo.cs3342.sandbox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LLGrammar extends Grammar {

  public LLGrammar(GrammarFactory factory) {
    super(factory.getSymbolsMap(), factory.getProductions());

    calculateFirstSets();
    calculateFollowSets();
  }

  private void calculateFirstSets() {
    // Initialize FIRST sets for terminals
    for (Symbol symbol : this.getSymbols()) {
      if (!symbol.isNonterminal) {
        symbol.firstSet.add(symbol); // Use the Symbol instance itself
      }
    }

    boolean progress;
    do {
      progress = false;
      for (Production p : productions) {
        Symbol X = p.leftHandSide;
        List<Symbol> rhs = p.rightHandSide;

        // If X -> ε is a production, add ε to FIRST(X)
        if (rhs.isEmpty() || string_EPS(rhs)) {
          Symbol epsilonSymbol = this.symbolsMap.get("ε");
          if (epsilonSymbol != null && !X.firstSet.contains(epsilonSymbol)) {
            X.firstSet.add(epsilonSymbol);
            progress = true;
          }
        }

        // Apply the algorithm for FIRST set calculation
        Set<Symbol> rhsFirstSet = string_FIRST(rhs);
        if (X.firstSet.addAll(rhsFirstSet)) {
          progress = true;
        }
      }
    } while (progress);
  }

  private void calculateFollowSets() {
    if (!productions.isEmpty()) {
      Symbol startSymbol = productions.get(0).leftHandSide;

      // Ensure $ is represented as a Symbol and added to symbolsMap
      Symbol endOfInputSymbol = this.symbolsMap.get("$");
      if (endOfInputSymbol != null) {
        startSymbol.followSet.add(endOfInputSymbol);
      }
    }

    boolean progress;
    do {
      progress = false;
      for (Production p : productions) {
        Symbol A = p.leftHandSide;
        List<Symbol> rhs = p.rightHandSide;

        for (int i = 0; i < rhs.size(); i++) {
          Symbol B = rhs.get(i);
          if (B.isNonterminal) {
            // Apply the algorithm for FOLLOW set calculation
            // Case 1: Everything but the last symbol
            if (i + 1 < rhs.size()) {
              List<Symbol> beta = rhs.subList(i + 1, rhs.size());
              Set<Symbol> betaFirstSet = string_FIRST(beta);
              if (B.followSet.addAll(betaFirstSet)) {
                progress = true;
              }
            }

            // Case 2: Last symbol or when string_EPS(beta) is true
            if (i == rhs.size() - 1 || string_EPS(rhs.subList(i + 1, rhs.size()))) {
              if (B.followSet.addAll(A.followSet)) {
                progress = true;
              }
            }
          }
        }
      }
    } while (progress);
  }

  private boolean string_EPS(List<Symbol> symbols) {
    Symbol epsilonSymbol = this.symbolsMap.get("ε");
    for (Symbol s : symbols) {
      if (epsilonSymbol == null || !s.firstSet.contains(epsilonSymbol)) {
        return false;
      }
    }
    return true;
  }

  private Set<Symbol> string_FIRST(List<Symbol> symbols) {
    Set<Symbol> result = new HashSet<>();
    Symbol epsilonSymbol = this.symbolsMap.get("ε");
    for (Symbol s : symbols) {
      result.addAll(s.firstSet);
      if (epsilonSymbol == null || !s.firstSet.contains(epsilonSymbol)) {
        break;
      }
    }
    return result;
  }
}
