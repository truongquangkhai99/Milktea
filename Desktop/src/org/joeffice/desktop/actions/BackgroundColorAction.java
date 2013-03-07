/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.desktop.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Change the background color.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office",
        id = "org.joeffice.desktop.actions.BackgroundColorAction")
@ActionRegistration(
        iconBase = "org/joeffice/desktop/actions/tag_blue_edit.png",
        displayName = "#CTL_BackgroundColorAction")
@ActionReference(path = "Menu/Edit", position = 1510)
@Messages("CTL_BackgroundColorAction=Background Color")
public final class BackgroundColorAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
