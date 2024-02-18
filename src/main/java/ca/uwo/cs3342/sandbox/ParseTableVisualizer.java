package ca.uwo.cs3342.sandbox;

import java.util.Map;

public class ParseTableVisualizer {

  ParseTableVisualizer(Grammar grammar, Map<Symbol, Map<Symbol, Production<Symbol>>> parseTable) {
    this.printParseTable(grammar, parseTable);
  }

  public static void visualize(
      Grammar grammar, Map<Symbol, Map<Symbol, Production<Symbol>>> parseTable) {
    new ParseTableVisualizer(grammar, parseTable);
  }

  private void printParseTable(
      Grammar grammar, Map<Symbol, Map<Symbol, Production<Symbol>>> parseTable) {
    // Determine the width for each column for formatting
    int startColumnWidth = 15;
    int columnWidth = 8;

    // Print the header row with input symbols
    System.out.print(String.format("%-" + startColumnWidth + "s", "Nonterminal"));
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
}
