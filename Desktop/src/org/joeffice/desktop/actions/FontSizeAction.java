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

import java.awt.event.ActionEvent;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import org.joeffice.desktop.ui.Styleable;

import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.awt.ActionID;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 * Changes the size of the font for the selected text.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office",
        id = "org.joeffice.desktop.actions.FontSizeAction")
@ActionRegistration(
        displayName = "#CTL_FontSizeAction")
@Messages("CTL_FontSizeAction=Font Size")
public final class FontSizeAction extends AbstractAction {

    private Styleable styleable;

    public FontSizeAction(Styleable styleable) {
        this.styleable = styleable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AttributedString currentAttributes = styleable.getCommonFontAttributes();
        double defaultFontSize = 11.0;
        Map<AttributedCharacterIterator.Attribute,Object> fontSizeAttr =
                currentAttributes.getIterator(new TextAttribute[] { TextAttribute.SIZE }).getAttributes();
        if (!fontSizeAttr.isEmpty()) {
            defaultFontSize = (Double) fontSizeAttr.get(TextAttribute.SIZE);
        }
        String title = NbBundle.getMessage(getClass(), "CTL_FontSizeAction");
        SpinnerNumberModel fontSizeModel = new SpinnerNumberModel(defaultFontSize, 1.0, 99.0, 1.0);
        JSpinner spinner = new JSpinner(fontSizeModel);
        DialogDescriptor dialogDesc = new DialogDescriptor(spinner, title);
        Object dialogResult = DialogDisplayer.getDefault().notify(dialogDesc);
        if (dialogResult == DialogDescriptor.OK_OPTION) {
            AttributedString attributes = new AttributedString("FontSize");
            attributes.addAttribute(TextAttribute.SIZE, fontSizeModel.getNumber());
            styleable.setFontAttributes(attributes);
        }
    }
}
