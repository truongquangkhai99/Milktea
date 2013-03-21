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
package org.joeffice.wordprocessor.nb;

import java.io.File;
import java.io.IOException;
import javax.swing.text.Document;

import org.joeffice.desktop.file.OfficeDataObject;
import org.joeffice.desktop.ui.OfficeTopComponent;
import org.joeffice.wordprocessor.writer.DocxWriter;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

/**
 * The DataObject for docx documents.
 *
 * @author Anthony Goubard - Japplis
 */
@Messages({
    "LBL_Docx_LOADER=Files of Docx"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_Docx_LOADER",
        mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        extension = {"docx", "DOCX"})
@DataObject.Registration(
        mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        iconBase = "org/joeffice/wordprocessor/wordp-16.png",
        displayName = "#LBL_Docx_LOADER",
        position = 350) // 250 to enable as editor, 350 to disable
@ActionReferences({
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.wordprocessingml.document/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.wordprocessingml.document/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.wordprocessingml.document/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.wordprocessingml.document/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.wordprocessingml.document/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.wordprocessingml.document/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.wordprocessingml.document/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.wordprocessingml.document/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.wordprocessingml.document/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class DocxDataObject extends OfficeDataObject {

    public DocxDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException {
        super(pf, loader);
        getCookieSet().add(DocxEditorSupport.class, this);
    }

    @Override
    public <T extends Node.Cookie> T createCookie(Class<T> type) {
        if (type.isAssignableFrom(DocxEditorSupport.class)) {
            return (T) new DocxEditorSupport(this, getLookup());
        }
        return super.createCookie(type);
    }

    @Override
    public OfficeTopComponent open(OfficeDataObject dataObject) {
        //return new WordpTopComponent(dataObject);
        return null;
    }

    @Override
    public void save(File file) throws IOException {
        Document doc = (Document) getDocument();
        DocxWriter writer = new DocxWriter(doc);
        writer.write(file.getAbsolutePath());
    }
}
