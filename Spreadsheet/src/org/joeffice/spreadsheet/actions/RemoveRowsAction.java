/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.spreadsheet.actions;

import static javax.swing.Action.NAME;
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
 * Remove the selected row(s).
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office/Spreadsheet",
        id = "org.joeffice.spreadsheet.actions.RemoveRowsAction")
@ActionRegistration(
        displayName = "#CTL_RemoveRowsAction")
@Messages("CTL_RemoveRowsAction=Remove selected rows")
public final class RemoveRowsAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent currentTopComponent = TopComponent.getRegistry().getActivated();
        if (currentTopComponent instanceof SpreadsheetTopComponent) {
            JTable currentTable = ((SpreadsheetTopComponent) currentTopComponent).getSpreadsheetComponent().getSelectedSheet().getTable();
            int[] selectedRows = currentTable.getSelectedRows();

            // This is no necessary true as the database may use another model
            ((SheetTableModel) currentTable.getModel()).removeRows(selectedRows);
            currentTable.clearSelection();
        }
    }
}
