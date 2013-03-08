package org.joeffice.desktop.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Underline the selected text.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office",
        id = "org.joeffice.desktop.actions.UnderlineAction")
@ActionRegistration(
        iconBase = "org/joeffice/desktop/actions/text_underline.png",
        displayName = "#CTL_UnderlineAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 2250),
    @ActionReference(path = "Shortcuts", name = "D-U")
})
@Messages("CTL_UnderlineAction=Underline")
public final class UnderlineAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
