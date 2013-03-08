/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * Change the font color.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office",
        id = "org.joeffice.desktop.actions.ForegroundColorAction")
@ActionRegistration(
        iconBase = "org/joeffice/desktop/actions/color_swatch.png",
        displayName = "#CTL_ForegroundColorAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1500),
    @ActionReference(path = "Toolbars/Font", position = 3300)
})
@Messages("CTL_ForegroundColorAction=Color")
public final class ForegroundColorAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
