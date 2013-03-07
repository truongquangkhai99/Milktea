package org.joeffice.spreadsheet.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import org.joeffice.spreadsheet.SpreadsheetTopComponent;
import org.joeffice.spreadsheet.tablemodel.SheetTableModel;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Insert a row below the selected rows.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office/Spreadsheet",
        id = "org.joeffice.spreadsheet.actions.InsertRowsAction")
@ActionRegistration(
        displayName = "#CTL_InsertRowsAction")
@Messages("CTL_InsertRowsAction=Insert row(s)")
public final class InsertRowsAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent currentTopComponent = TopComponent.getRegistry().getActivated();
        if (currentTopComponent instanceof SpreadsheetTopComponent) {
            JTable currentTable = ((SpreadsheetTopComponent) currentTopComponent).getSpreadsheetComponent().getSelectedSheet().getTable();
            int[] selectedRows = currentTable.getSelectedRows();
            // This is no necessary true as the database may use another model
            ((SheetTableModel) currentTable.getModel()).insertRows(1, selectedRows);
        }
    }
}
