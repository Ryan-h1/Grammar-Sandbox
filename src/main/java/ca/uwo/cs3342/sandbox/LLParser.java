package ca.uwo.cs3342.sandbox;

import java.util.*;

public class LLParser {

  private final LLGrammar grammar;
  private final Map<Symbol, Map<Symbol, Production<Symbol>>> parseTable;

  LLParser(LLGrammar grammar) {
    this.grammar = grammar;
    this.parseTable = new HashMap<>();

    constructParseTable();
  }

  public ParseTreeNode parse(List<Symbol> inputTokens) throws RuntimeException {
    Stack<Symbol> stack = new Stack<>();
    Stack<ParseTreeNode> parseTreeStack = new Stack<>();
    stack.push(grammar.getEndOfInputSymbol());
    stack.push(grammar.getStartSymbol());
    inputTokens.add(grammar.getEndOfInputSymbol());

    ParseTreeNode root = new ParseTreeNode(grammar.getStartSymbol());
    parseTreeStack.push(root);

    int tokenIndex = 0;

    while (!stack.isEmpty()) {
      Symbol topSymbol = stack.peek();

      // If the top of the stack is the end of input symbol and it matches the current token,
      // parsing is done
      if (topSymbol.equals(grammar.getEndOfInputSymbol())
          && topSymbol.equals(inputTokens.get(tokenIndex))) {
        break; // Successfully reached the end of input
      }

      Symbol currentToken = inputTokens.get(tokenIndex);

      if (grammar.getTerminals().contains(topSymbol)
          || topSymbol.equals(grammar.getEndOfInputSymbol())) {
        if (topSymbol.equals(currentToken)) {
          stack.pop(); // Match, move forward in input
          parseTreeStack.pop(); // Pop from parse tree stack as well
          tokenIndex++;
        } else {
          throw new RuntimeException("Syntax Error: Unexpected symbol " + currentToken);
        }
      } else { // Non-terminal
        Map<Symbol, Production<Symbol>> row = parseTable.get(topSymbol);
        if (row != null && row.containsKey(currentToken)) {
          Production<Symbol> production = row.get(currentToken);
          stack.pop(); // Pop the non-terminal
          ParseTreeNode node = parseTreeStack.pop();

          List<Symbol> rhs = production.getRightHandSide();
          if (rhs.isEmpty() || (rhs.size() == 1 && rhs.get(0).equals(grammar.getEpsilonSymbol()))) {
            // Handle epsilon production by not pushing anything onto the stack
            node.addChild(
                new ParseTreeNode(grammar.getEpsilonSymbol())); // Optionally add epsilon node
          } else {
            // Push production symbols in reverse order to stack
            for (int i = rhs.size() - 1; i >= 0; i--) {
              Symbol symbol = rhs.get(i);
              ParseTreeNode childNode = new ParseTreeNode(symbol);
              node.addChild(childNode);
              stack.push(symbol);
              parseTreeStack.push(childNode);
            }
          }
        } else {
          throw new RuntimeException(
              "Syntax Error: No rule to apply for " + topSymbol + " with token " + currentToken);
        }
      }
    }

    if (tokenIndex < inputTokens.size() - 1) { // Ensure parsing consumed all input
      throw new RuntimeException("Syntax Error: Extra input tokens");
    }

    return root; // Return the root of the parse tree
  }

  public void printParseTable() {
    // Determine the width for each column for formatting
    int startColumnWidth = 15;
    int columnWidth = 8;

    // Print the header row with input symbols
    System.out.print(String.format("%-" + startColumnWidth + "s", "State"));
    grammar
        .getTerminals()
        .forEach(terminal -> System.out.print(String.format("%-" + columnWidth + "s", terminal)));
    System.out.println();

    // Print each row of the parse table
    parseTable.forEach(
        (nonTerminal, row) -> {
          System.out.print(String.format("%-" + startColumnWidth + "s", nonTerminal));
          grammar
              .getTerminals()
              .forEach(
                  terminal -> {
                    Production<Symbol> production = row.get(terminal);
                    String output = production != null ? "P" + production.getId() : "-";
                    System.out.print(String.format("%-" + columnWidth + "s", output));
                  });
          System.out.println(); // New line after each row
        });
  }

  private void constructParseTable() {
    for (Production<Symbol> production : grammar.getProductions()) {
      for (Symbol terminal : grammar.getTerminals()) {
        if (production.predictSet.contains(terminal)) {
          parseTable.putIfAbsent(production.leftHandSide, new HashMap<>());
          parseTable.get(production.leftHandSide).put(terminal, production);
        }
      }
    }
  }
}
