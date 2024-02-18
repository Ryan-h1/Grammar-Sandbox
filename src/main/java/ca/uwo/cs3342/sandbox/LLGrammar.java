package ca.uwo.cs3342.sandbox;

import java.util.LinkedHashSet;
import java.util.List;

public class LLGrammar extends Grammar implements GrammarConstants {

  public LLGrammar(Grammar grammar) throws IllegalArgumentException {
    super(grammar.symbolsMap, grammar.productions);

    calculateFirstSets();
    calculateFollowSets();
    calculatePredictSets();

    if (!validate()) {
      printAllSets();
      throw new IllegalArgumentException("The grammar is not LL(1)");
    }
  }

  public void printAllSets() {
    printFirstSets();
    System.out.println();
    printFollowSets();
    System.out.println();
    printPredictSets();
  }

  public void printFirstSets() {
    for (Symbol symbol : this.getSymbols()) {
      if (!symbol.isTerminal && !symbol.isEpsilon) {
        System.out.println("FIRST(" + symbol.name + ") = " + symbol.firstSet);
      }
    }
  }

  public void printFollowSets() {
    for (Symbol symbol : this.getSymbols()) {
      if (!symbol.isTerminal && !symbol.isEpsilon) {
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
    boolean progress;
    do {
      progress = false;
      for (Production<Symbol> p : productions) {
        List<Symbol> rhs = p.rightHandSide;
        for (int i = 0; i < rhs.size(); i++) {
          Symbol B = rhs.get(i);
          if (!B.isTerminal() && !B.isEpsilon()) {
            LinkedHashSet<Symbol> followB = B.followSet;
            int originalSize = followB.size();

            if (i < rhs.size() - 1) {
              List<Symbol> beta = rhs.subList(i + 1, rhs.size());
              LinkedHashSet<Symbol> firstBeta = string_FIRST(beta);
              firstBeta.remove(this.symbolsMap.get(EPSILON)); // Remove ε if present.
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
        p.predictSet.remove(this.symbolsMap.get(EPSILON)); // Remove ε
        p.predictSet.addAll(A.followSet); // PREDICT(A -> α) = (FIRST(α) - {ε}) ∪ FOLLOW(A)
      }
    }
  }

  private boolean string_EPS(List<Symbol> symbols) {
    // If the list of symbols is empty, it implicitly means ε
    if (symbols.isEmpty()) return true;

    // Check if all symbols can derive ε
    for (Symbol s : symbols) {
      if (!s.isEpsilon && !s.firstSet.contains(this.symbolsMap.get(EPSILON))) {
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
      if (!s.firstSet.contains(this.symbolsMap.get(EPSILON))) {
        break;
      }
      // Remove ε as it only applies if all preceding symbols can derive ε
      result.remove(this.symbolsMap.get(EPSILON));
    }
    // If all symbols can derive ε, we add ε at the end
    if (string_EPS(symbols)) {
      result.add(this.symbolsMap.get(EPSILON));
    }
    return result;
  }

  /**
   * Validates if the grammar is LL(1) by checking that all predict sets with productions with the
   * same LHS are disjoint.
   *
   * @return true if the grammar is LL(1), false otherwise
   */
  private boolean validate() {
    for (Symbol symbol : this.getSymbols()) {
      if (!symbol.isTerminal()) {
        // Group productions by their left-hand side
        List<Production<Symbol>> productionsWithSameLHS =
            productions.stream().filter(p -> p.leftHandSide.equals(symbol)).toList();

        for (int i = 0; i < productionsWithSameLHS.size(); i++) {
          for (int j = i + 1; j < productionsWithSameLHS.size(); j++) {
            Production<Symbol> prod1 = productionsWithSameLHS.get(i);
            Production<Symbol> prod2 = productionsWithSameLHS.get(j);

            // Check for intersection in PREDICT sets
            LinkedHashSet<Symbol> intersection = new LinkedHashSet<>(prod1.predictSet);
            intersection.retainAll(prod2.predictSet);
            if (!intersection.isEmpty()) {
              // If there is an intersection, the grammar is not LL(1)
              return false;
            }
          }
        }
      }
    }
    // If no intersections are found, the grammar is LL(1)
    return true;
  }
}
