package ca.uwo.cs3342.sandbox;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class Production<T> {

  public T leftHandSide;
  public List<T> rightHandSide;
  public LinkedHashSet<T> predictSet;

  public int id;

  public Production(T lhs, List<T> rhs) {
    this.leftHandSide = lhs;
    this.rightHandSide = rhs;
    this.predictSet = new LinkedHashSet<>();
  }

  public Production(T lhs, List<T> rhs, int id) {
    this(lhs, rhs);
    this.id = id;
  }

  public T getLeftHandSide() {
    return leftHandSide;
  }

  public void setLeftHandSide(T leftHandSide) {
    this.leftHandSide = leftHandSide;
  }

  public List<T> getRightHandSide() {
    return rightHandSide;
  }

  public void setRightHandSide(List<T> rightHandSide) {
    this.rightHandSide = rightHandSide;
  }

  public LinkedHashSet<T> getPredictSet() {
    return predictSet;
  }

  public void setPredictSet(LinkedHashSet<T> predictSet) {
    this.predictSet = predictSet;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return leftHandSide.toString()
        + " -> "
        + rightHandSide.stream().map(Object::toString).collect(Collectors.joining(" "));
  }
}
