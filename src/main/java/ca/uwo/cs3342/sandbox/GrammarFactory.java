package ca.uwo.cs3342.sandbox;

import java.util.*;
import java.util.stream.Collectors;

public class GrammarFactory implements GrammarConstants {
  private final LinkedHashMap<String, Symbol> symbolsMap = new LinkedHashMap<>();
  private final List<Production<Symbol>> productions = new ArrayList<>();

  GrammarFactory() {
    symbolsMap.put(EPSILON, new Symbol(EPSILON, false, true));
  }

  public Grammar createNewGrammar(GrammarForm grammarForm) {
    for (Production<String> production : grammarForm.getProductions()) {
      symbolsMap.put(production.leftHandSide, new Symbol(production.leftHandSide, false, false));
    }

    for (Production<String> production : grammarForm.getProductions()) {
      for (String symbolName : production.rightHandSide) {
        symbolsMap.putIfAbsent(symbolName, new Symbol(symbolName, true, false));
      }

      productions.add(
          new Production<>(
              symbolsMap.get(production.leftHandSide),
              production.rightHandSide.stream().map(symbolsMap::get).collect(Collectors.toList())));
    }

    return new Grammar(symbolsMap, productions);
  }
}
