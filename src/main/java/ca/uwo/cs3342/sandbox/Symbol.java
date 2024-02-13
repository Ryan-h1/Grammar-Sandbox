package ca.uwo.cs3342.sandbox;

import java.util.LinkedHashSet;
import java.util.Objects;

public class Symbol {
  public boolean isNonterminal;
  public String name;
  public LinkedHashSet<Symbol> firstSet;
  public LinkedHashSet<Symbol> followSet;
  public boolean isEpsilon;

  Symbol(String name, boolean isNonterminal, boolean isEpsilon) {
    this.name = name;
    this.isNonterminal = isNonterminal;
    this.firstSet = new LinkedHashSet<>();
    this.followSet = new LinkedHashSet<>();
    this.isEpsilon = isEpsilon;
  }

  Symbol(String name, boolean isNonterminal) {
    this(name, isNonterminal, false);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Symbol symbol = (Symbol) o;
    return isNonterminal == symbol.isNonterminal && Objects.equals(name, symbol.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isNonterminal, name);
  }

  @Override
  public String toString() {
    return name;
  }
}
