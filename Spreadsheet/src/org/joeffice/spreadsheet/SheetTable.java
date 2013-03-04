package org.joeffice.spreadsheet;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class SheetTable extends JTable {

    public SheetTable(TableModel tableModel) {
        super(tableModel);
    }

    @Override
    public void setRowHeight(int row, int rowHeight) {
        super.setRowHeight(row, rowHeight);
        // Fire the row changed
        firePropertyChange("singleRowHeight", -1, row);
    }
}
