package ca.uwo.cs3342.sandbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class ParseTreeVisualizer extends JPanel {
  private static final int NODE_WIDTH = 60;
  private static final int NODE_HEIGHT = 30;
  private static final int LEVEL_GAP = 140;
  private static final int NODE_GAP = 16;
  private final ParseTreeNode root;
  private final Map<ParseTreeNode, Integer> subtreeWidths;
  private double zoomFactor = 1.0;

  /***
   * @param root The root of the parse tree to visualize
   */
  public ParseTreeVisualizer(ParseTreeNode root) {
    this.root = root;
    this.subtreeWidths = new HashMap<>();
    calculateSubtreeWidths(root);

    setPreferredSize(new Dimension((int) (subtreeWidths.get(root)), 800));
    JScrollPane scrollPane = new JScrollPane(this);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);

    JFrame frame = new JFrame("Parse Tree Visualization");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(scrollPane);
    frame.pack();
    frame.setVisible(true);

    // Center the horizontal scrollbar
    SwingUtilities.invokeLater(() -> {
      JScrollBar hBar = scrollPane.getHorizontalScrollBar();
      int scrollBarMax = hBar.getMaximum();
      int scrollBarWidth = hBar.getWidth();
      int scrollBarValue = (scrollBarMax - scrollBarWidth) / 2;
      hBar.setValue(scrollBarValue);
    });

    addMouseWheelListener(
        e -> {
          if (e.getPreciseWheelRotation() < 0) {
            zoomFactor *= 1.1; // Zoom in
          } else {
            zoomFactor /= 1.1; // Zoom out
          }
          revalidate();
          repaint();
        });
  }

  private int calculateSubtreeWidths(ParseTreeNode node) {
    int width = NODE_GAP;
    for (ParseTreeNode child : node.getChildren()) {
      width += calculateSubtreeWidths(child) + NODE_GAP; // Gap between nodes
    }
    width = Math.max(width, NODE_WIDTH); // Ensure minimum width for the node itself
    subtreeWidths.put(node, width);
    return width;
  }

  private void drawTree(Graphics g, ParseTreeNode node, int x, int y, int parentWidth) {
    int totalWidth = subtreeWidths.get(node);
    int childY = y + LEVEL_GAP;
    int childX =
        x
            - totalWidth / 2
            + NODE_WIDTH / 2; // Start drawing children from the left edge of their subtree

    for (ParseTreeNode child : node.getChildren()) {
      int childWidth = subtreeWidths.get(child);
      g.drawLine(x, y, childX + childWidth / 2, childY);
      drawTree(g, child, childX + childWidth / 2, childY, totalWidth);
      childX += childWidth + NODE_GAP;
    }

    // Draw the box for the node
    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(x - NODE_WIDTH / 2, y - NODE_HEIGHT / 2, NODE_WIDTH, NODE_HEIGHT);
    g.setColor(Color.BLACK);
    g.drawRect(x - NODE_WIDTH / 2, y - NODE_HEIGHT / 2, NODE_WIDTH, NODE_HEIGHT);
    if (node.getSymbol().isTerminal()) {
      g.setFont(g.getFont().deriveFont(Font.BOLD));
    } else {
      g.setFont(g.getFont().deriveFont(Font.ITALIC));
    }
    drawCenteredString(g, node.getSymbol().toString(), x, y);
  }

  private void drawCenteredString(Graphics g, String text, int x, int y) {
    FontMetrics fm = g.getFontMetrics();
    int textX = x - (fm.stringWidth(text) / 2);
    int textY = y - (fm.getHeight() / 2) + fm.getAscent();
    g.drawString(text, textX, textY);
  }

  /**
   * @param root The root of the parse tree to visualize
   */
  public static void visualize(ParseTreeNode root) {
    SwingUtilities.invokeLater(() -> new ParseTreeVisualizer(root));
  }

  /**
   * @param root The root of the parse tree to print
   */
  public static void printParseTree(ParseTreeNode root) {
    printParseTreeNode(root, 0); // Start printing from the root with level 0
  }

  private static void printParseTreeNode(ParseTreeNode node, int level) {
    // Print indentation based on the level (depth) of the current node
    for (int i = 0; i < level; i++) {
      if (i < level - 1) {
        System.out.print("|  "); // Print vertical line for tree structure
      } else {
        System.out.print("|--"); // Print branch to child node
      }
    }

    System.out.println(node.getSymbol());

    // Recursively print each child node, increasing the level (depth) by 1
    List<ParseTreeNode> children = node.getChildren();
    for (int i = 0; i < children.size(); i++) {
      printParseTreeNode(children.get(i), level + 1);
      if (i < children.size() - 1) {
        for (int j = 0; j < level; j++) {
          System.out.print("|  "); // Print vertical line between siblings
        }
        System.out.println("|"); // Branch from parent to next sibling
      }
    }
    // After all children are printed, print a vertical line for the last child
    if (!children.isEmpty()) {
      for (int i = 0; i < level; i++) {
        System.out.print("|  ");
      }
      System.out.println();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.scale(zoomFactor, zoomFactor); // Apply zoom factor
    int startingX =
        (int) ((getWidth() / 2) / zoomFactor); // Center of the component adjusted for zoom
    int startingY =
        (int) ((NODE_HEIGHT / 2) / zoomFactor); // A little offset from the top adjusted for zoom
    drawTree(g2, root, startingX, startingY, getWidth());
  }
}
