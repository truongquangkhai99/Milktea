/*
 * Copyright 2013 Japplis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joeffice.desktop.actions;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import javax.swing.AbstractAction;
import org.joeffice.desktop.ui.FontListTopComponent;
import org.joeffice.desktop.ui.Styleable;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Change font.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office",
        id = "org.joeffice.desktop.actions.ChooseFontAction")
@ActionRegistration(
        iconBase = "org/joeffice/desktop/actions/font.png",
        displayName = "#CTL_ChooseFontAction")
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 1510),
    @ActionReference(path = "Toolbars/Font", position = 3000)
})
@Messages("CTL_ChooseFontAction=Choose Font...")
public class ChooseFontAction extends AbstractAction {

    private Styleable styleable;

    public ChooseFontAction(Styleable styleable) {
        this.styleable = styleable;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        AttributedString attributes = new AttributedString("ChangeFont");
        String fontName = ae.getActionCommand();
        if (fontName == null || fontName.isEmpty()) {
            FontListTopComponent fontList = new FontListTopComponent();
            fontList.noSelectionListener();
            DialogDescriptor dialogDesc = new DialogDescriptor(fontList, "Choose font", true, DialogDescriptor.OK_CANCEL_OPTION, null, null);
            Dialog dialog = DialogDisplayer.getDefault().createDialog(dialogDesc);
            dialog.setVisible(true);
            fontName = fontList.getSelectedFontName();
        }
        if (fontName != null) {
            attributes.addAttribute(TextAttribute.FAMILY, fontName);
            styleable.setFontAttributes(attributes);
        }
    }
}
