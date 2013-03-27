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

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.AbstractAction;

import org.joeffice.desktop.file.OfficeDataObject;

import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "File",
        id = "org.joeffice.desktop.actions.OpenInSystemAction")
@ActionRegistration(
        displayName = "#CTL_OpenInSystemAction")
@ActionReferences({
    @ActionReference(path = "Loaders/application/vnd.ms-excel/Actions", position = 200),
    @ActionReference(path = "Loaders/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet/Actions", position = 200),
    @ActionReference(path = "Loaders/application/vnd.openxmlformats-officedocument.wordprocessingml.document/Actions", position = 200),
    @ActionReference(path = "Loaders/application/vnd.openxmlformats-officedocument.presentationml.presentation/Actions", position = 200),
    @ActionReference(path = "Loaders/image/svg+xml/Actions", position = 200)
})
@Messages("CTL_OpenInSystemAction=Open in System")
public class OpenInSystemAction extends AbstractAction {

    private final List<DataObject> context;

    public OpenInSystemAction(List<DataObject> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        Desktop desktop = Desktop.getDesktop();
        for (DataObject dataObject : context) {
            File file = FileUtil.toFile(((OfficeDataObject) dataObject).getPrimaryFile());
            try {
                desktop.open(file);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
