package ca.uwo.cs3342.sandbox;

import java.util.*;
import java.util.stream.Collectors;

public class Grammar {
  protected LinkedHashMap<String, Symbol> symbolsMap;
  protected List<Production<Symbol>> productions;

  public Grammar(LinkedHashMap<String, Symbol> symbolsMap, List<Production<Symbol>> productions) {
    this.symbolsMap = symbolsMap;
    this.productions = productions;
  }

  public LinkedHashMap<String, Symbol> getSymbolsMap() {
    return symbolsMap;
  }

  public List<Symbol> getSymbols() {
    return new ArrayList<>(symbolsMap.values());
  }

  public List<Symbol> getTerminals() {
    return symbolsMap.values().stream().filter(Symbol::isTerminal).collect(Collectors.toList());
  }

  public List<Production<Symbol>> getProductions() {
    return productions;
  }

  public Symbol getStartSymbol() {
    return productions.get(0).leftHandSide;
  }

  public Symbol getEpsilonSymbol() {
    return symbolsMap.get(GrammarConstants.EPSILON);
  }

  public Symbol getEndOfInputSymbol() {
    return symbolsMap.get(GrammarConstants.END_OF_INPUT);
  }

  @Override
  public String toString() {
    return productions.stream().map(Production::toString).collect(Collectors.joining("\n"));
  }
}
