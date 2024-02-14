package ca.uwo.cs3342.sandbox;

import java.util.LinkedHashSet;
import java.util.List;

public class LLGrammar extends Grammar implements GrammarConstants {
  Symbol epsilonSymbol;

  public LLGrammar(Grammar grammar) {
    super(grammar.symbolsMap, grammar.productions);

    this.epsilonSymbol = this.symbolsMap.get(EPSILON);

    calculateFirstSets();
    calculateFollowSets();
    calculatePredictSets();
  }

  public void printFirstSets() {
    for (Symbol symbol : this.getSymbols()) {
      if (!symbol.isTerminal) {
        System.out.println("FIRST(" + symbol.name + ") = " + symbol.firstSet);
      }
    }
  }

  public void printFollowSets() {
    for (Symbol symbol : this.getSymbols()) {
      if (!symbol.isTerminal) {
        System.out.println("FOLLOW(" + symbol.name + ") = " + symbol.followSet);
      }
    }
  }

  public void printPredictSets() {
    for (Production<Symbol> p : productions) {
      System.out.println("PREDICT(" + p + ") = " + p.predictSet);
    }
  }

  private void calculateFirstSets() {
    // Initialize FIRST sets for terminals and ε
    for (Symbol symbol : this.getSymbols()) {
      if (symbol.isTerminal || symbol.isEpsilon) {
        symbol.firstSet.add(symbol);
      }
    }

    boolean progress;
    do {
      progress = false;
      for (Production<Symbol> p : productions) {
        Symbol X = p.leftHandSide;
        List<Symbol> rhs = p.rightHandSide;

        // Before calculating FIRST(rhs), ensure that ε is correctly set for all symbols
        LinkedHashSet<Symbol> rhsFirstSet = string_FIRST(rhs);
        if (X.firstSet.addAll(rhsFirstSet)) {
          progress = true;
        }
      }
    } while (progress);
  }

  private void calculateFollowSets() {
    this.getSymbols().get(1).followSet.add(this.symbolsMap.get(END_OF_INPUT));

    boolean progress;
    do {
      progress = false;
      for (Production<Symbol> p : productions) {
        List<Symbol> rhs = p.rightHandSide;
        for (int i = 0; i < rhs.size(); i++) {
          Symbol B = rhs.get(i);
          if (!B.isTerminal) {
            LinkedHashSet<Symbol> followB = B.followSet;
            int originalSize = followB.size();

            if (i < rhs.size() - 1) {
              List<Symbol> beta = rhs.subList(i + 1, rhs.size());
              LinkedHashSet<Symbol> firstBeta = string_FIRST(beta);
              firstBeta.remove(epsilonSymbol); // Remove ε if present.
              followB.addAll(firstBeta);
            }

            if (i == rhs.size() - 1 || string_EPS(rhs.subList(i + 1, rhs.size()))) {
              followB.addAll(p.leftHandSide.followSet);
            }

            if (followB.size() > originalSize) {
              progress = true;
            }
          }
        }
      }
    } while (progress);
  }

  private void calculatePredictSets() {
    for (Production<Symbol> p : productions) {
      Symbol A = p.leftHandSide;
      List<Symbol> alpha = p.rightHandSide;

      if (!string_EPS(alpha)) { // If ε is not in FIRST(α)
        p.predictSet.addAll(string_FIRST(alpha)); // PREDICT(A -> α) = FIRST(α)
      } else { // If ε is in FIRST(α)
        p.predictSet.addAll(string_FIRST(alpha));
        p.predictSet.remove(epsilonSymbol); // Remove ε
        p.predictSet.addAll(A.followSet); // PREDICT(A -> α) = (FIRST(α) - {ε}) ∪ FOLLOW(A)
      }
    }
  }

  private boolean string_EPS(List<Symbol> symbols) {
    // If the list of symbols is empty, it implicitly means ε
    if (symbols.isEmpty()) return true;

    // Check if all symbols can derive ε
    for (Symbol s : symbols) {
      if (!s.isEpsilon && !s.firstSet.contains(epsilonSymbol)) {
        return false;
      }
    }
    return true;
  }

  private LinkedHashSet<Symbol> string_FIRST(List<Symbol> symbols) {
    LinkedHashSet<Symbol> result = new LinkedHashSet<>();
    for (Symbol s : symbols) {
      result.addAll(s.firstSet);
      // If ε is not in FIRST(s), we stop adding FIRST sets
      if (!s.firstSet.contains(epsilonSymbol)) {
        break;
      }
      // Remove ε as it only applies if all preceding symbols can derive ε
      result.remove(epsilonSymbol);
    }
    // If all symbols can derive ε, we add ε at the end
    if (string_EPS(symbols)) {
      result.add(epsilonSymbol);
    }
    return result;
  }
}
