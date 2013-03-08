/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.desktop.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.joeffice.desktop.file.OfficeDataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.util.NbPreferences;

// Wait for http://netbeans.org/bugzilla/show_bug.cgi?id=186943 to be released
@ActionID(
        category = "File",
        id = "org.joeffice.desktop.actions.NewFileAction")
@ActionRegistration(
        iconBase = "org/joeffice/desktop/actions/add.png",
        displayName = "#CTL_NewFileAction")
@ActionReference(path = "Menu/File", position = 90)
@Messages("CTL_NewFileAction=New File...")
public final class NewFileAction extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent ae) {
        JFileChooser newFileChooser = new JFileChooser();
        String defaultLocation = NbPreferences.forModule(NewFileAction.class).get("file.location", System.getProperty("user.home"));
        newFileChooser.setCurrentDirectory(new File(defaultLocation));
        newFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        addFileFilters(newFileChooser);
        newFileChooser.showSaveDialog(null);
    }

    private void addFileFilters(JFileChooser chooser) {
        // Utilities.actionsForPath(NAME);
        final Collection<? extends OfficeDataObject> possibleObjects = Lookup.getDefault().lookupAll(OfficeDataObject.class);
        for (final OfficeDataObject dataObject : possibleObjects) {
            FileFilter filter = new FileFilter() {

                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(dataObject.getPrimaryFile().getExt());
                }

                @Override
                public String getDescription() {
                    return dataObject.getNodeDelegate().getDisplayName();
                }

            };
            chooser.addChoosableFileFilter(filter);
        }
        chooser.setAcceptAllFileFilterUsed(true);
    }
}
