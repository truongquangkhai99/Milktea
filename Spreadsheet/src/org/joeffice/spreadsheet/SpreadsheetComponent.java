package org.joeffice.spreadsheet;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class SpreadsheetComponent extends JPanel {

    private JTabbedPane sheets;

    public SpreadsheetComponent() {
        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());
        sheets = new JTabbedPane(JTabbedPane.BOTTOM, JTabbedPane.SCROLL_TAB_LAYOUT);
        add(sheets);
    }

    public JPanel createSheet(Sheet sheet) {
        final JTable table = createTable(sheet);

        // From http://stackoverflow.com/questions/6663591/jtable-inside-jlayeredpane-inside-jscrollpane-how-do-you-get-it-to-work
        JLayeredPane layers = new JLayeredPane() {
            @Override
            public Dimension getPreferredSize() {
                return table.getPreferredSize();
            }

            @Override
            public void setSize(int width, int height) {
                super.setSize(width, height);
                table.setSize(width, height);
            }

            @Override
            public void setSize(Dimension d) {
                super.setSize(d);
                table.setSize(d);
            }
        };
        // NB you must use new Integer() - the int version is a different method
        layers.add(table, new Integer(JLayeredPane.DEFAULT_LAYER), 0);
        JScrollPane scrolling = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrolling.setViewportView(layers);
        scrolling.setColumnHeaderView(table.getTableHeader());
        // TODO scrolling.setRowHeaderView(rowHeader);

        JPanel sheetPanel = new JPanel(new BorderLayout());
        sheetPanel.add(scrolling);
        return sheetPanel;
    }

    public JTable createTable(Sheet sheet) {
        JTable table = new JTable(10, 10);
        return table;
    }

    public void load(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = workbook.getSheetName(i);
            JPanel sheetPanel = createSheet(sheet);
            sheets.addTab(sheetName, sheetPanel);
        }
    }
}
