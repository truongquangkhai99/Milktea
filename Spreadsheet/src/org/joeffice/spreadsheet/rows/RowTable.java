package org.joeffice.spreadsheet.rows;

import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class RowTable extends JTable {

    private JTable dataTable;

    public RowTable(JTable dataTable) {
        this.dataTable = dataTable;
        TableModel rowModel = createRowTableModel();
        setModel(rowModel);
        LookAndFeel.installColorsAndFont(this, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");

        getColumnModel().getColumn(0).setHeaderValue("");
        getColumnModel().getColumn(0).setPreferredWidth(40);
        Dimension d = getPreferredScrollableViewportSize();
        d.width = getPreferredSize().width;
        setPreferredScrollableViewportSize(d);
        setRowHeight(dataTable.getRowHeight());
        setDefaultRenderer(String.class, new RowHeadersRenderer()); // This doesn't work!
        getColumnModel().getColumn(0).setCellRenderer(new RowHeadersRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader corner = getTableHeader();
        corner.setReorderingAllowed(false);
        corner.setResizingAllowed(false);

        // Add listener for setRowHeight from the data table
    }

    private TableModel createRowTableModel() {
        DefaultTableModel rowTableModel = new DefaultTableModel(dataTable.getRowCount(), 1) {
            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return "" + (rowIndex + 1);
            }

            @Override
            public String getColumnName(int column) {
                return "";
            }

            @Override
            public int getRowCount() {
                return dataTable.getRowCount();
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        return rowTableModel;
    }
}
