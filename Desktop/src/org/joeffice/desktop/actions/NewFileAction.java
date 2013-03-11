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
import java.io.File;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import org.joeffice.desktop.file.OfficeDataObject;
import org.openide.ErrorManager;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.cookies.InstanceCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
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
        Action a = findAction("Actions/Project/org-netbeans-modules-project-ui-NewFile$WithSubMenu.instance");
        a.setEnabled(true);
        a.actionPerformed(ae);
        /*JFileChooser newFileChooser = new JFileChooser();
        String defaultLocation = NbPreferences.forModule(NewFileAction.class).get("file.location", System.getProperty("user.home"));
        newFileChooser.setCurrentDirectory(new File(defaultLocation));
        newFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        addFileFilters(newFileChooser);
        newFileChooser.showSaveDialog(null);*/
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

    public Action findAction(String key) {
        FileObject fo = FileUtil.getConfigFile(key);
        if (fo != null && fo.isValid()) {
            try {
                DataObject dob = DataObject.find(fo);
                InstanceCookie ic = dob.getLookup().lookup(InstanceCookie.class);
                if (ic != null) {
                    Object instance = ic.instanceCreate();
                    if (instance instanceof Action) {
                        Action a = (Action) instance;
                        return a;
                    }
                }
            } catch (Exception e) {
                ErrorManager.getDefault().notify(ErrorManager.WARNING, e);
                return null;
            }
        }
        return null;
    }
}
