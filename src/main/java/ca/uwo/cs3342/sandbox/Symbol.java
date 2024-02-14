package ca.uwo.cs3342.sandbox;

import java.util.LinkedHashSet;
import java.util.Objects;

public class Symbol {
  public boolean isTerminal;
  public String name;
  public LinkedHashSet<Symbol> firstSet;
  public LinkedHashSet<Symbol> followSet;
  public boolean isEpsilon;

  Symbol(String name, boolean isTerminal, boolean isEpsilon) {
    this.name = name;
    this.isTerminal = isTerminal;
    this.firstSet = new LinkedHashSet<>();
    this.followSet = new LinkedHashSet<>();
    this.isEpsilon = isEpsilon;
  }

  Symbol(String name, boolean isTerminal) {
    this(name, isTerminal, false);
  }

  Symbol(String name) {
    this(name, true);
  }

  public String getName() {
    return this.name;
  }

  public boolean isTerminal() {
    return this.isTerminal;
  }

  public boolean isEpsilon() {
    return this.isEpsilon;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Symbol symbol = (Symbol) o;
    return isTerminal == symbol.isTerminal && Objects.equals(name, symbol.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(isTerminal, name);
  }

  @Override
  public String toString() {
    return name;
  }
}
