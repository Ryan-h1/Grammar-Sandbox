package ca.uwo.cs3342.sandbox;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Production<T> {
  public T leftHandSide;
  public List<T> rightHandSide;
  public LinkedHashSet<T> predictSet;

  public Production(T lhs, List<T> rhs) {
    this.leftHandSide = lhs;
    this.rightHandSide = rhs;
    this.predictSet = new LinkedHashSet<>();
  }

  @Override
  public String toString() {
    return leftHandSide.toString()
        + " -> "
        + rightHandSide.stream().map(Object::toString).collect(Collectors.joining(" "));
  }
}
