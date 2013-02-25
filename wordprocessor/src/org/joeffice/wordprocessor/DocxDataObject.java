/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.wordprocessor;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.swing.text.Document;
import org.joeffice.wordprocessor.writer.DocxWriter;
import org.netbeans.core.spi.multiview.MultiViewElement;
import org.netbeans.core.spi.multiview.text.MultiViewEditorElement;
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
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

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
public class DocxDataObject extends MultiDataObject implements CookieSet.Factory {

    private Document content;

    private DocxOpenSupport opener;
    private DocxSaveCookie saver;

    public DocxDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException {
        super(pf, loader);
        registerEditor("application/vnd.openxmlformats-officedocument.wordprocessingml.document", true);
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

    /*void editorInitialized(MyEditor ed) {
        Opener op = getLookup().lookup(Opener.class);
        op.editor = ed;
    }*/

    @Override
    protected Node createNodeDelegate() {
        return new DocxDataNode(this, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    @MultiViewElement.Registration(
            displayName = "#LBL_Docx_EDITOR",
            iconBase = "org/joeffice/wordprocessor/wordp-16.png",
            mimeType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED,
            preferredID = "Docx",
            position = 1000)
    @Messages("LBL_Docx_EDITOR=Source")
    public static MultiViewEditorElement createEditor(Lookup lkp) {
        return new MultiViewEditorElement(lkp);
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
