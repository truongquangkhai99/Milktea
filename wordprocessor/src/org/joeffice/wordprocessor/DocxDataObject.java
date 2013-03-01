/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.wordprocessor;

import java.io.IOException;
import javax.swing.text.Document;
import org.joeffice.desktop.file.OfficeDataObject;
import org.joeffice.wordprocessor.writer.DocxWriter;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.filesystems.MIMEResolver;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
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
        position = 300)
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
public class DocxDataObject extends OfficeDataObject implements CookieSet.Factory {

    // The document currently edited
    private Document content;

    private DocxOpenSupport opener;
    private DocxSaveCookie saver;

    public DocxDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        cookies.add(DocxOpenSupport.class, this);
        cookies.add(DocxSaveCookie.class, this);
    }

    synchronized void setContent(Document document) {
        this.content = document;
        if (document != null) {
            setModified(true);
            getCookieSet().add(saver);
        } else {
            setModified(false);
            getCookieSet().remove(saver);
        }
    }

    @Override
    public <T extends Node.Cookie> T createCookie(Class<T> type) {
        if (type.isAssignableFrom(DocxOpenSupport.class)) {
            if (opener == null) {
                opener = new DocxOpenSupport(getPrimaryEntry());
            }
            return (T) opener;
        }
        if (type.isAssignableFrom(DocxSaveCookie.class)) {
            if (saver == null) {
                saver = new DocxSaveCookie();
            }
            return (T) saver;
        }
        return null;
    }

    /**
     * Cookie invoked when the file is saved.
     * Note that if the file is not edited, no save cookie is in the cookies set.
     */
    private class DocxSaveCookie implements SaveCookie {

        @Override
        public void save() throws IOException {
            Document doc;
            synchronized (DocxDataObject.this) {
                //synchronize access to the content field
                doc = content;
                setContent(null);
            }
            DocxWriter writer = new DocxWriter(doc);
            FileObject fo = getPrimaryFile();
            writer.write(FileUtil.toFile(fo).getAbsolutePath());
        }
    }
}
