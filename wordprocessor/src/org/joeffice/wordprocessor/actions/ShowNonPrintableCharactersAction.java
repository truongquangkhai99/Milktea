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
package org.joeffice.wordprocessor.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextPane;
import javax.swing.text.Document;

import org.joeffice.wordprocessor.WordpTopComponent;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action that will show (or hide) all the non printable characters of the editor.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "View/Office/Word",
        id = "org.joeffice.wordprocessor.actions.ShowNonPrintableCharactersAction")
@ActionRegistration(
        displayName = "#CTL_ShowNonPrintableCharactersAction")
@ActionReference(path = "Menu/Edit/Gimme More/Word", position = 700)
@Messages("CTL_ShowNonPrintableCharactersAction=Show Non Printable Characters")
public final class ShowNonPrintableCharactersAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ae) {
        JTextPane editor = WordpTopComponent.findCurrentTextPane();
        Document doc = editor.getDocument();
        boolean allCharactersShown = doc.getProperty("show paragraphs") != null;
        if (allCharactersShown) {
            doc.putProperty("show paragraphs", null);
        } else {
            doc.putProperty("show paragraphs", Boolean.TRUE);
        }
        editor.repaint();
    }
}
