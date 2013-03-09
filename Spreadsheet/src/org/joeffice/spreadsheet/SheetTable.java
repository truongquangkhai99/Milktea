package org.joeffice.spreadsheet;

import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * The JTable used to display data.
 * This class is only to fix bugs or improve existing functionalities.
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
