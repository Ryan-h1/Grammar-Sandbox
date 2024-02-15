package ca.uwo.cs3342.sandbox;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ParseTreeVisualizer extends JPanel {
  private ParseTreeNode root;
  private Map<ParseTreeNode, Integer> subtreeWidths;
  private final int nodeWidth = 80;
  private final int nodeHeight = 40;
  private final int levelGap = 50;
  private final int nodeGap = 10;

  public ParseTreeVisualizer(ParseTreeNode root) {
    this.root = root;
    this.subtreeWidths = new HashMap<>();
    calculateSubtreeWidths(root);
    setPreferredSize(new Dimension((int) (subtreeWidths.get(root) * 1.2), 800));
    JFrame frame = new JFrame("Parse Tree Visualization");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(this);
    frame.pack();
    frame.setVisible(true);
  }

  private int calculateSubtreeWidths(ParseTreeNode node) {
    int width = nodeGap;
    for (ParseTreeNode child : node.getChildren()) {
      width += calculateSubtreeWidths(child) + nodeGap; // Gap between nodes
    }
    width = Math.max(width, nodeWidth); // Ensure minimum width for the node itself
    subtreeWidths.put(node, width);
    return width;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    drawTree(g, root, getWidth() / 2, nodeHeight / 2, getWidth() / 2);
  }

  private void drawTree(Graphics g, ParseTreeNode node, int x, int y, int width) {
    int childY = y + levelGap;
    int childX = x - (width - nodeWidth) / 2; // Center the children

    for (ParseTreeNode child : node.getChildren()) {
      int childWidth = subtreeWidths.get(child);
      g.drawLine(x, y, childX + childWidth / 2, childY);
      drawTree(g, child, childX + childWidth / 2, childY, childWidth);
      childX += childWidth + nodeGap;
    }

    // Draw the box for the node
    g.setColor(Color.LIGHT_GRAY);
    g.fillRect(x - nodeWidth / 2, y - nodeHeight / 2, nodeWidth, nodeHeight);
    g.setColor(Color.BLACK);
    g.drawRect(x - nodeWidth / 2, y - nodeHeight / 2, nodeWidth, nodeHeight);
    drawCenteredString(g, node.getSymbol().toString(), x, y, nodeWidth, nodeHeight);
  }

  private void drawCenteredString(Graphics g, String text, int x, int y, int width, int height) {
    FontMetrics fm = g.getFontMetrics();
    int textX = x - (fm.stringWidth(text) / 2);
    int textY = y + (fm.getHeight() / 2) - fm.getAscent();
    g.drawString(text, textX, textY);
  }

  public static void visualize(ParseTreeNode root) {
    SwingUtilities.invokeLater(() -> new ParseTreeVisualizer(root));
  }
}
