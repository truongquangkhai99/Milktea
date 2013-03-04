/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.desktop.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

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
public final class OpenInSystemAction implements ActionListener {

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
