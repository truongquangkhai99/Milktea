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
import org.joeffice.spreadsheet.tablemodel.SheetTableModel;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Build",
        id = "org.joeffice.spreadsheet.actions.RemoveRowsAction")
@ActionRegistration(
        displayName = "#CTL_RemoveRowsAction")
@Messages("CTL_RemoveRowsAction=Remove selected rows")
public final class RemoveRowsAction extends AbstractAction {

    private JTable dataTable;

    public RemoveRowsAction() {
    }

    public RemoveRowsAction(JTable dataTable) {
        setDataTable(dataTable);
        putValue(NAME, "Remove selected row(s)");
    }

    public void setDataTable(JTable dataTable) {
        this.dataTable = dataTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int[] selectedRows = dataTable.getSelectedRows();

        // This is no necessary true as the database may use another model
        ((SheetTableModel) dataTable.getModel()).removeRows(selectedRows);
        dataTable.clearSelection();
    }
}
