/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.spreadsheet.actions;

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
        id = "org.joeffice.spreadsheet.actions.InsertRowsAction")
@ActionRegistration(
        displayName = "#CTL_InsertRowsAction")
@Messages("CTL_InsertRowsAction=Insert row(s)")
public final class InsertRowsAction extends AbstractAction {

    private JTable dataTable;

    public InsertRowsAction() {
    }

    public InsertRowsAction(JTable dataTable) {
        setDataTable(dataTable);
        putValue(NAME, "Insert row(s)");
    }

    public void setDataTable(JTable dataTable) {
        this.dataTable = dataTable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO popup panel that asks for number of rows and before.

        int[] selectedRows = dataTable.getSelectedRows();
        // This is no necessary true as the database may use another model
        ((SheetTableModel) dataTable.getModel()).insertRows(1, selectedRows);
    }
}
