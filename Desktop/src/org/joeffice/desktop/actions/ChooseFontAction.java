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
        id = "org.joeffice.desktop.actions.ChooseFontAction")
@ActionRegistration(
        iconBase = "org/joeffice/desktop/actions/font.png",
        displayName = "#CTL_ChooseFontAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1470),
    @ActionReference(path = "Toolbars/Font", position = 3333)
})
@Messages("CTL_ChooseFontAction=Choose Font...")
public final class ChooseFontAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
