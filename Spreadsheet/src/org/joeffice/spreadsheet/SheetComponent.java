package org.joeffice.spreadsheet;

import static javax.swing.JLayeredPane.DEFAULT_LAYER;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS;
import static javax.swing.ScrollPaneConstants.UPPER_LEFT_CORNER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.joeffice.spreadsheet.renderer.CellRenderer;
import org.joeffice.spreadsheet.rows.JScrollPaneAdjuster;
import org.joeffice.spreadsheet.rows.JTableRowHeaderResizer;
import org.joeffice.spreadsheet.rows.RowTable;
import org.joeffice.spreadsheet.tablemodel.SheetTableModel;

/**
 * Component that displays one sheet.
 *
 * @author Anthony Goubard - Japplis
 */
public class SheetComponent extends JPanel {

    public static final short EXCEL_COLUMN_WIDTH_FACTOR = 256;
    public static final int UNIT_OFFSET_LENGTH = 7;

    private SpreadsheetComponent spreadsheetComponent;

    private JLayeredPane layers;
    private JTable sheetTable;

    public SheetComponent(Sheet sheet, SpreadsheetComponent spreadsheetComponent) {
        this.spreadsheetComponent = spreadsheetComponent;
        initComponent(sheet);
    }

    private void initComponent(Sheet sheet) {
        sheetTable = createTable(sheet);
        layers = createSheetLayers(sheetTable);

        JScrollPane scrolling = new JScrollPane(VERTICAL_SCROLLBAR_ALWAYS, HORIZONTAL_SCROLLBAR_ALWAYS);
        scrolling.setViewportView(layers);
        scrolling.setColumnHeaderView(sheetTable.getTableHeader());
        JTable rowHeaders = createRowHeaders(sheetTable);
        scrolling.setRowHeaderView(rowHeaders);
        scrolling.setCorner(UPPER_LEFT_CORNER, rowHeaders.getTableHeader());
        new JTableRowHeaderResizer(scrolling).setEnabled(true);
        new JScrollPaneAdjuster(scrolling);
        scrolling.getVerticalScrollBar().setUnitIncrement(16);

        setLayout(new BorderLayout());
        add(scrolling);
    }

    public JTable createTable(Sheet sheet) {
        SheetTableModel sheetTableModel = new SheetTableModel(sheet);
        JTable table = new SheetTable(sheetTableModel);

        table.setDefaultRenderer(Cell.class, new CellRenderer());
        //TableCellEditor editor = new DefaultCellEditor(new JTextField());
        TableCellEditor editor = new org.joeffice.spreadsheet.editor.CellEditor();
        // table.setDefaultEditor(Cell.class, editor);
        int columnsCount = sheetTableModel.getColumnCount();
        for (int i = 0; i < columnsCount; i++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(i);
            tableColumn.setCellRenderer(new CellRenderer());
            tableColumn.setCellEditor(editor);
            int widthUnits = sheet.getColumnWidth(i);
            tableColumn.setPreferredWidth(widthUnitsToPixel(widthUnits));
        }

        int rowCount = sheetTableModel.getRowCount();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            Row row = sheet.getRow(rowIndex);
            if (row != null) {
                table.setRowHeight(rowIndex, (int) sheet.getRow(rowIndex).getHeightInPoints());
            }
        }

        table.setAutoscrolls(true);
        table.setFillsViewportHeight(true);
        JLabel tableHeader = (JLabel) table.getTableHeader().getDefaultRenderer();
        tableHeader.setHorizontalAlignment(SwingConstants.CENTER);

        // XXX This is OK for one block but it doesn't work for 2 blocks, also selecting row no longer works
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        table.setCellSelectionEnabled(true);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(true);

        table.setDragEnabled(true);
        table.setDropMode(DropMode.ON_OR_INSERT);
        return table;
    }

    // From http://www.chka.de/swing/table/row-headers/RowHeaderTable.java Christian Kaufhold
    public JTable createRowHeaders(JTable sheetTable) {
        JTable rowHeaders = new RowTable(sheetTable);
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

    // From http://apache-poi.1045710.n5.nabble.com/Excel-Column-Width-Unit-Converter-pixels-excel-column-width-units-td2301481.html
    public static int widthUnitsToPixel(int widthUnits) {
        int pixels = (widthUnits / EXCEL_COLUMN_WIDTH_FACTOR) * UNIT_OFFSET_LENGTH;

        int offsetWidthUnits = widthUnits % EXCEL_COLUMN_WIDTH_FACTOR;
        pixels += Math.round((float) offsetWidthUnits / ((float) EXCEL_COLUMN_WIDTH_FACTOR / UNIT_OFFSET_LENGTH));

        return pixels;
    }

    public SpreadsheetComponent getSpreadsheetComponent() {
        return spreadsheetComponent;
    }
}
