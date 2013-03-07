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

/**
 * Change the font weight to bold.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office",
        id = "org.joeffice.desktop.actions.BoldAction")
@ActionRegistration(
        iconBase = "org/joeffice/desktop/actions/text_bold.png",
        displayName = "#CTL_BoldAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1780),
    @ActionReference(path = "Toolbars/Font", position = 3100),
    @ActionReference(path = "Shortcuts", name = "D-B")
})
@Messages("CTL_BoldAction=Bold")
public final class BoldAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
