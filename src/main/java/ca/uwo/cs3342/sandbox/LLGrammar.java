package ca.uwo.cs3342.sandbox;

import java.util.LinkedHashSet;
import java.util.List;

public class LLGrammar extends Grammar implements GrammarConstants {

  public LLGrammar(Grammar grammar) throws IllegalArgumentException {
    super(grammar.symbolsMap, grammar.productions);

    if (!validate()) {
      throw new IllegalArgumentException("The grammar is not LL(1)");
    }
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
