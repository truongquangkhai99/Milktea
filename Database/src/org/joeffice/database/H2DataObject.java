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
package org.joeffice.database;

import java.io.IOException;

import org.joeffice.desktop.file.OfficeDataObject;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 * The data object to deal with .h2.db (H2 database)  files.
 *
 * @author Anthony Goubard - Japplis
 */
@Messages({
    "LBL_H2_LOADER=Files of H2"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_H2_LOADER",
        mimeType = "application/h2",
        extension = {"db"}) // Netbeans Platform can't handle .h2.db as extension
@DataObject.Registration(
        mimeType = "application/h2",
        iconBase = "org/joeffice/database/database-16.png",
        displayName = "#LBL_H2_LOADER",
        position = 300)
@ActionReferences({
    @ActionReference(
            path = "Loaders/application/h2/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/application/h2/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/application/h2/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/application/h2/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/application/h2/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/application/h2/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/application/h2/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/application/h2/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/application/h2/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class H2DataObject extends OfficeDataObject implements CookieSet.Factory {

    private transient DatabaseOpenSupport opener;

    public H2DataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        cookies.add(DatabaseOpenSupport.class, this);
   }

    @Override
    public <T extends Node.Cookie> T createCookie(Class<T> type) {
        if (type.isAssignableFrom(DatabaseOpenSupport.class)) {
            if (opener == null) {
                opener = new DatabaseOpenSupport(getPrimaryEntry());
            }
            return (T) opener;
        }
        return null;
    }

    @Override
    public void setContent(Object document) {
        // Not editable, using auto-commit
    }
}
