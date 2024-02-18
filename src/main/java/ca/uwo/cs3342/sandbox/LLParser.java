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

  /***
   * Parses the input tokens using the LL(1) parse table
   *
   * @param inputTokens List of input tokens
   * @return Parse tree root node
   * @throws RuntimeException if the input tokens do not form a valid parse tree
   */
  public ParseTreeNode parse(List<Symbol> inputTokens) throws RuntimeException {
    if (grammar.getEndOfInputSymbol() == null) {
      throw new RuntimeException("End of input symbol not set");
    }

    if (inputTokens.get(inputTokens.size() - 1) == grammar.getEndOfInputSymbol()) {
      inputTokens.remove(inputTokens.size() - 1);
      if (EnvironmentConstants.DEBUG) {
        System.out.println(
            "The end of input symbol was already present in the input tokens and was removed");
      }
    }

    Deque<Symbol> stack = new ArrayDeque<>();
    Deque<ParseTreeNode> parseTreeStack = new ArrayDeque<>();
    stack.push(grammar.getEndOfInputSymbol());
    stack.push(grammar.getStartSymbol());
    inputTokens.add(grammar.getEndOfInputSymbol());

    ParseTreeNode root = new ParseTreeNode(grammar.getStartSymbol());
    parseTreeStack.push(root);

    int tokenIndex = 0;

    while (!stack.isEmpty()) {
      if (EnvironmentConstants.DEBUG) {
        ParseTreeVisualizer.printParseTree(root);
      }

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
          throw new RuntimeException(
              "Syntax Error: Unexpected symbol "
                  + currentToken
                  + " with current top symbol "
                  + topSymbol);
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
            node.addChild(new ParseTreeNode(grammar.getEpsilonSymbol())); // Add epsilon node
          } else {
            // Create child nodes for all symbols in the production rule
            List<ParseTreeNode> childNodes = new ArrayList<>();
            for (Symbol symbol : rhs) {
              childNodes.add(new ParseTreeNode(symbol));
            }

            // Add child nodes to the current node in the order they appear
            for (ParseTreeNode childNode : childNodes) {
              node.addChild(childNode);
            }

            // Push symbols onto the stack in reverse order for correct processing
            for (int i = childNodes.size() - 1; i >= 0; i--) {
              ParseTreeNode childNode = childNodes.get(i);
              stack.push(childNode.getSymbol());
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

  /***
   * Prints the LL(1) parse table
   */
  public void printParseTable() {
    ParseTableVisualizer.visualize(grammar, parseTable);
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
