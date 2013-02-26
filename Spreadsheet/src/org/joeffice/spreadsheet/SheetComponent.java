package org.joeffice.spreadsheet;

import static javax.swing.JLayeredPane.DEFAULT_LAYER;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.UPPER_LEFT_CORNER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.joeffice.spreadsheet.renderer.CellRenderer;
import org.joeffice.spreadsheet.renderer.RowHeadersRenderer;
import org.joeffice.spreadsheet.tablemodel.SheetTableModel;

/**
 * Component that displays one sheet.
 *
 * @author Anthony Goubard - Japplis
 */
public class SheetComponent extends JPanel {

    private JLayeredPane layers;
    private JTable rowHeaders;
    private JTable sheetTable;

    public SheetComponent(Sheet sheet) {
        initComponent(sheet);
    }

    public void initComponent(Sheet sheet) {
        sheetTable = createTable(sheet);
        layers = createSheetLayers(sheetTable);

        JScrollPane scrolling = new JScrollPane(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
        scrolling.setViewportView(layers);
        scrolling.setColumnHeaderView(sheetTable.getTableHeader());
        rowHeaders = createRowHeaders(sheetTable);
        scrolling.setRowHeaderView(rowHeaders);
        scrolling.setCorner(UPPER_LEFT_CORNER, rowHeaders.getTableHeader());

        setLayout(new BorderLayout());
        add(scrolling);
    }

    public JTable createTable(Sheet sheet) {
        SheetTableModel sheetTableModel = new SheetTableModel(sheet);
        JTable table = new JTable(sheetTableModel);
        table.setDefaultRenderer(Cell.class, new CellRenderer());
        TableCellEditor editor = new DefaultCellEditor(new JTextField());
        table.setDefaultEditor(Cell.class, editor);
        return table;
    }

    // From http://www.chka.de/swing/table/row-headers/RowHeaderTable.java Christian Kaufhold
    public JTable createRowHeaders(JTable sheetTable) {
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
        rowHeaders.setDefaultRenderer(String.class, new RowHeadersRenderer());

        JTableHeader corner = rowHeaders.getTableHeader();
        corner.setReorderingAllowed(false);
        corner.setResizingAllowed(false);

        return rowHeaders;
    }

    // From http://stackoverflow.com/questions/6663591/jtable-inside-jlayeredpane-inside-jscrollpane-how-do-you-get-it-to-work
    public JLayeredPane createSheetLayers(final JTable table) {
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
        return layers;
    }
}
