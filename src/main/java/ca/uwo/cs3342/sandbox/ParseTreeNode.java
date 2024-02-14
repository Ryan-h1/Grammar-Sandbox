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

  public static void printParseTree(ParseTreeNode root) {
    printParseTreeNode(root, 0); // Start printing from the root with level 0
  }

  private static void printParseTreeNode(ParseTreeNode node, int level) {
    // Print indentation based on the level (depth) of the current node
    for (int i = 0; i < level; i++) {
      System.out.print("  "); // Two spaces per level of depth
    }

    // Print the current node's symbol
    System.out.println(node.getSymbol());

    // Recursively print each child node, increasing the level (depth) by 1
    for (ParseTreeNode child : node.getChildren()) {
      printParseTreeNode(child, level + 1);
    }
  }
}
