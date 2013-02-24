/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.desktop.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit",
        id = "org.joeffice.desktop.actions.ItalicAction")
@ActionRegistration(
        iconBase = "org/joeffice/desktop/actions/text_italic.png",
        displayName = "#CTL_ItalicAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1470),
    @ActionReference(path = "Toolbars/Font", position = 3333),
    @ActionReference(path = "Shortcuts", name = "D-I")
})
@Messages("CTL_ItalicAction=Italic")
public final class ItalicAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
