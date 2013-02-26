package org.joeffice.spreadsheet;

import static javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT;
import static javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS;
import static javax.swing.JScrollPane.UPPER_LEFT_CORNER;
import static javax.swing.JLayeredPane.DEFAULT_LAYER;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joeffice.spreadsheet.renderer.CellRenderer;
import org.joeffice.spreadsheet.renderer.RowHeaderRenderer;
import org.joeffice.spreadsheet.tablemodel.SheetTableModel;

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
        sheets = new JTabbedPane(JTabbedPane.BOTTOM, SCROLL_TAB_LAYOUT);
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
        layers.add(table, new Integer(DEFAULT_LAYER), 0);
        JScrollPane scrolling = new JScrollPane(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
        scrolling.setViewportView(layers);
        scrolling.setColumnHeaderView(table.getTableHeader());
        JTable rowHeaders = getRowHeaders(table);
        scrolling.setRowHeaderView(rowHeaders);
        scrolling.setCorner(UPPER_LEFT_CORNER, rowHeaders.getTableHeader());

        JPanel sheetPanel = new JPanel(new BorderLayout());
        sheetPanel.add(scrolling);
        return sheetPanel;
    }

    public JTable createTable(Sheet sheet) {
        SheetTableModel sheetTableModel = new SheetTableModel(sheet);
        JTable table = new JTable(sheetTableModel);
        table.setDefaultRenderer(Cell.class, new CellRenderer());
        return table;
    }

    // From http://www.chka.de/swing/table/row-headers/RowHeaderTable.java
    public JTable getRowHeaders(JTable sheetTable) {
        DefaultTableModel rowTableModel = new DefaultTableModel(sheetTable.getRowCount(), 1) {
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return "" + (rowIndex + 1);
            }

            @Override
            public String getColumnName(int column) {
                return "";
            }
        };

        JTable rowHeaders = new JTable(rowTableModel);
        LookAndFeel.installColorsAndFont(rowHeaders, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");

        rowHeaders.getColumnModel().getColumn(0).setHeaderValue("");
        rowHeaders.getColumnModel().getColumn(0).setPreferredWidth(40);
        Dimension d = rowHeaders.getPreferredScrollableViewportSize();
        d.width = rowHeaders.getPreferredSize().width;
        rowHeaders.setPreferredScrollableViewportSize(d);
        rowHeaders.setRowHeight(sheetTable.getRowHeight());
        rowHeaders.setDefaultRenderer(String.class, new RowHeaderRenderer());

        JTableHeader corner = rowHeaders.getTableHeader();
        corner.setReorderingAllowed(false);
        corner.setResizingAllowed(false);

        return rowHeaders;
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
