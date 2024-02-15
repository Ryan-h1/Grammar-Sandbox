package ca.uwo.cs3342.sandbox;

import java.util.ArrayList;
import java.util.List;

public class ParseTreeNode {
  private Symbol symbol;
  private final List<ParseTreeNode> children;

  public ParseTreeNode(Symbol symbol) {
    this.symbol = symbol;
    this.children = new ArrayList<>();
  }

  public void addChild(ParseTreeNode child) {
    children.add(child);
  }

  public List<ParseTreeNode> getChildren() {
    return children;
  }

  public void setSymbol(Symbol symbol) {
    this.symbol = symbol;
  }

  public Symbol getSymbol() {
    return symbol;
  }

  @Override
  public String toString() {
    return symbol.toString();
  }
}
