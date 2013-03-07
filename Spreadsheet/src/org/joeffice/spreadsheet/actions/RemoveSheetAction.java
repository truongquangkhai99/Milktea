/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.spreadsheet.actions;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.joeffice.spreadsheet.SpreadsheetComponent;
import org.joeffice.spreadsheet.SpreadsheetTopComponent;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Remove the selected sheet.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office/Spreadsheet",
        id = "org.joeffice.spreadsheet.actions.RemoveSheetAction")
@ActionRegistration(
        displayName = "#CTL_RemoveSheetAction")
@Messages("CTL_RemoveSheetAction=Remove sheet")
public final class RemoveSheetAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        TopComponent currentTopComponent = TopComponent.getRegistry().getActivated();
        if (currentTopComponent instanceof SpreadsheetTopComponent) {
            SpreadsheetComponent spreadsheet = ((SpreadsheetTopComponent) currentTopComponent).getSpreadsheetComponent();
            spreadsheet.removeCurrentSheet();
        }
    }
}
