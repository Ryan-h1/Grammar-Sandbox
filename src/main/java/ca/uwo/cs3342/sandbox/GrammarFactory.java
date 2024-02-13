package ca.uwo.cs3342.sandbox;

import java.util.*;

public class GrammarFactory {
  private LinkedHashMap<String, Symbol> symbolsMap = new LinkedHashMap<>();
  private List<Production> productions = new ArrayList<>();

  // Method to simplify symbol creation
  public Symbol createSymbol(String name, boolean isTerminal, boolean isEpsilon) {
    Symbol symbol = new Symbol(name, isTerminal, isEpsilon);
    symbolsMap.put(name, symbol);
    return symbol;
  }

  // Overloaded method for non-epsilon symbols
  public Symbol createSymbol(String name, boolean isTerminal) {
    return createSymbol(name, isTerminal, false);
  }

  // Method to simplify production creation
  public void createProduction(String lhs, String... rhsSymbols) {
    List<Symbol> rhs = new ArrayList<>();
    for (String symbolName : rhsSymbols) {
      rhs.add(symbolsMap.get(symbolName));
    }
    productions.add(new Production(symbolsMap.get(lhs), rhs));
  }

  public LinkedHashMap<String, Symbol> getSymbolsMap() {
    return symbolsMap;
  }

  public List<Symbol> getSymbols() {
    return new ArrayList<>(symbolsMap.values());
  }

  public List<Production> getProductions() {
    return productions;
  }
}
