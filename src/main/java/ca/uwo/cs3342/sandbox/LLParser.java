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
    Deque<Symbol> stack = new ArrayDeque<>();
    stack.push(grammar.getEndOfInputSymbol());
    stack.push(grammar.getStartSymbol());

    ListIterator<Symbol> inputIterator = inputTokens.listIterator();
    ParseTreeNode root =
        new ParseTreeNode(grammar.getStartSymbol()); // Starting point of the parse tree
    Stack<ParseTreeNode> nodeStack = new Stack<>();
    nodeStack.push(root);

    while (!stack.isEmpty()) {
      Symbol topStackSymbol = stack.peek();
      if (!inputIterator.hasNext()) {
        throw new RuntimeException("Unexpected end of input tokens.");
      }
      Symbol currentInputSymbol = inputIterator.next();

      ParseTreeNode.printParseTree(root);

      if (topStackSymbol.equals(grammar.getEpsilonSymbol())) {
        stack.pop();
        nodeStack.pop();
        inputIterator.previous(); // Unread the current input symbol
      } else if (topStackSymbol.isTerminal
          || topStackSymbol.equals(grammar.getEndOfInputSymbol())) {
        if (topStackSymbol.equals(currentInputSymbol)) {
          stack.pop(); // Matched a terminal, pop from the stack
          nodeStack.pop().setSymbol(currentInputSymbol); // Attach token to the tree node
        } else {
          // Handle parsing error - terminal mismatch
          throw new RuntimeException(
              "Syntax error: expected " + topStackSymbol + ", found " + currentInputSymbol);
        }
      } else {
        Map<Symbol, Production<Symbol>> row = parseTable.get(topStackSymbol);
        Production<Symbol> production = row.get(currentInputSymbol);

        if (production != null) {
          stack.pop(); // Pop the non-terminal from the stack
          ParseTreeNode parentNode = nodeStack.pop();

          // Reverse the RHS of the production to push onto the stack
          List<Symbol> productionRHS = new ArrayList<>(production.rightHandSide);
          Collections.reverse(productionRHS);

          for (Symbol symbol : productionRHS) {
            stack.push(symbol);
            ParseTreeNode childNode = new ParseTreeNode(symbol);
            parentNode.addChild(childNode);
            nodeStack.push(childNode);
          }
        } else {
          // Handle parsing error - no production found
          throw new RuntimeException(
              "Syntax error: no rule to apply for "
                  + topStackSymbol
                  + " with input "
                  + currentInputSymbol);
        }
      }
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
    // For each production A -> α in G
    for (Production<Symbol> production : grammar.getProductions()) {
      Symbol A = production.leftHandSide;
      LinkedHashSet<Symbol> predictSet = production.predictSet;

      // For each terminal a in PREDICT(A -> α)
      for (Symbol a : predictSet) {
        // If ε is in PREDICT(A -> α), add A -> α to M[A, b] for each b in FOLLOW(A)
        if (a.isEpsilon) {
          for (Symbol b : A.followSet) {
            addToParsingTable(A, b, production);
          }
        } else {
          // Otherwise, add A -> α to M[A, a]
          addToParsingTable(A, a, production);
        }
      }
    }
  }

  private void addToParsingTable(
      Symbol nonTerminal, Symbol terminal, Production<Symbol> production) {
    Map<Symbol, Production<Symbol>> row =
        parseTable.computeIfAbsent(nonTerminal, k -> new HashMap<>());
    row.put(terminal, production);
  }
}
