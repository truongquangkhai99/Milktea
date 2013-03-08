/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.spreadsheet.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import org.joeffice.spreadsheet.SpreadsheetTopComponent;
import org.joeffice.spreadsheet.tablemodel.SheetTableModel;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

@ActionID(
        category = "Edit/Office/Spreadsheet",
        id = "org.joeffice.spreadsheet.actions.RemoveColumnAction")
@ActionRegistration(
        displayName = "#CTL_RemoveColumnAction")
@ActionReferences(value = {
    @ActionReference(path = "Office/Spreadsheet/Toolbar", position = 400)})
@Messages("CTL_RemoveColumnAction=Remove Column")
public final class RemoveColumnAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent currentTopComponent = TopComponent.getRegistry().getActivated();
        if (currentTopComponent instanceof SpreadsheetTopComponent) {
            JTable currentTable = ((SpreadsheetTopComponent) currentTopComponent).getSelectedTable();
            int selectedColumn = currentTable.getSelectedColumn();
            ((SheetTableModel) currentTable.getModel()).removeColumn(selectedColumn);
        }
    }
}
