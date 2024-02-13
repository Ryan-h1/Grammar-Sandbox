package ca.uwo.cs3342.sandbox;

import java.util.List;
import java.util.stream.Collectors;

public class Production {
  public Symbol leftHandSide;
  public List<Symbol> rightHandSide;

  Production(Symbol lhs, List<Symbol> rhs) {
    this.leftHandSide = lhs;
    this.rightHandSide = rhs;
  }

  @Override
  public String toString() {
    return leftHandSide.name
        + " -> "
        + rightHandSide.stream().map(s -> s.name).collect(Collectors.joining(" "));
  }
}
