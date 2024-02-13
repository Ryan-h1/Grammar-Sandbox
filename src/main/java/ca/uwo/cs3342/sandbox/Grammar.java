package ca.uwo.cs3342.sandbox;

import java.util.*;

public abstract class Grammar {
  protected LinkedHashMap<String, Symbol> symbolsMap;
  protected List<Production> productions;

  public Grammar(LinkedHashMap<String, Symbol> symbolsMap, List<Production> productions) {
    this.symbolsMap = symbolsMap;
    this.productions = productions;
  }

  public List<Symbol> getSymbols() {
    return new ArrayList<>(symbolsMap.values());
  }

  public List<Production> getProductions() {
    return productions;
  }
}
