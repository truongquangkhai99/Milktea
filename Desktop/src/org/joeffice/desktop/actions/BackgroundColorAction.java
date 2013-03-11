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
import javax.swing.AbstractAction;

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
@ActionReference(path = "Menu/Edit", position = 1560)
@Messages("CTL_BackgroundColorAction=Background Color")
public final class BackgroundColorAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO implement action body
    }
}
