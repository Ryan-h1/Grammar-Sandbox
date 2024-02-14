package ca.uwo.cs3342.sandbox;

import java.util.*;

public class GrammarForm {
  private final LinkedHashSet<Production<String>> productions;

  GrammarForm() {
    this.productions = new LinkedHashSet<>();
  }

  public void addProduction(String lhs, String... rhsSymbols) {
    List<String> rightHandSide = new ArrayList<>();
    Collections.addAll(rightHandSide, rhsSymbols);
    productions.add(new Production<>(lhs, rightHandSide, productions.size() + 1));
  }

  public List<Production<String>> getProductions() {
    return new ArrayList<>(productions);
  }
}
