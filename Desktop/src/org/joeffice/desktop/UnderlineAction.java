/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.desktop;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit",
        id = "org.joeffice.desktop.UnderlineAction")
@ActionRegistration(
        iconBase = "org/joeffice/desktop/text_underline.png",
        displayName = "#CTL_UnderlineAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 2250),
    @ActionReference(path = "Shortcuts", name = "D-U")
})
@Messages("CTL_UnderlineAction=Underline")
public final class UnderlineAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
