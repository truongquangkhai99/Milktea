package org.joeffice.spreadsheet.actions;

import static javax.swing.Action.NAME;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JTable;
import org.joeffice.spreadsheet.SpreadsheetTopComponent;
import org.openide.windows.TopComponent;

/**
 * This action just changes the source component of the action event.
 *
 * @author Anthony Goubard - Japplis
 */
public class ClipboardAction extends AbstractAction {

    public ClipboardAction(String name) {
        putValue(NAME, name);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        TopComponent currentTopComponent = TopComponent.getRegistry().getActivated();
        if (currentTopComponent instanceof SpreadsheetTopComponent) {
            JTable currentTable = ((SpreadsheetTopComponent) currentTopComponent).getSelectedTable();
            String originalActionName = (String) getValue(NAME);
            originalActionName = originalActionName.substring(0, originalActionName.indexOf('-')); // paste-from-clipboard -> paste
            Action tableAction = currentTable.getActionMap().get(originalActionName);
            ActionEvent newEvent = new ActionEvent(currentTable, ae.getID(), ae.getActionCommand(), ae.getWhen(), ae.getModifiers());
            tableAction.actionPerformed(newEvent);
        }
    }
}
