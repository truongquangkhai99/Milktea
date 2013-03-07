package org.joeffice.tools;

import java.awt.BorderLayout;
import javax.swing.*;

public class JTableCellSelection {

    public static void showDemo(JComponent demo, String title) {
        JFrame mainFrame = new JFrame();
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setTitle(title);
        JPanel contentPanel = new JPanel(new BorderLayout());

        contentPanel.add(demo);
        
        mainFrame.add(contentPanel);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        JTable table = new JTable(10, 10);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setCellSelectionEnabled(true);
        table.addRowSelectionInterval(6, 7); // Select 2 lines
        showDemo(new JScrollPane(table), "Select a block and some rows");
    }
}