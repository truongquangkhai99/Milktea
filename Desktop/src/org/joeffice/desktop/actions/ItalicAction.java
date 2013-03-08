/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.desktop.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.text.DataEditorSupport;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

/**
 * Change the font style to italic.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office",
        id = "org.joeffice.desktop.actions.ItalicAction")
@ActionRegistration(
        iconBase = "org/joeffice/desktop/actions/text_italic.png",
        displayName = "#CTL_ItalicAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1790),
    @ActionReference(path = "Toolbars/Font", position = 3200),
    @ActionReference(path = "Shortcuts", name = "D-I")
})
@Messages("CTL_ItalicAction=Italic")
public final class ItalicAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
        DataEditorSupport editorSupport = Lookup.getDefault().lookup(DataEditorSupport.class);
        JEditorPane[] openedPanes = editorSupport.getOpenedPanes();
        for (JEditorPane openedPane : openedPanes) {
            int startSelection = openedPane.getSelectionStart();
            int endSelection = openedPane.getSelectionEnd();
            int selectionLength = endSelection - startSelection;
            if (selectionLength > 0) {
                SimpleAttributeSet italicSet = new SimpleAttributeSet();
                StyleConstants.setItalic(italicSet, true);
                editorSupport.getDocument().setCharacterAttributes(startSelection, endSelection - startSelection, italicSet, false);
            } else {
                // todo
            }
        }
    }
}
