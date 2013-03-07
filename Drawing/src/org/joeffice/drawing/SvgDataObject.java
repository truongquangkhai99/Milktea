/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.drawing;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.batik.dom.util.DOMUtilities;
import org.joeffice.desktop.file.OfficeDataObject;
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
import org.openide.util.NbBundle.Messages;
import org.w3c.dom.Document;

/**
 * Data object for .svg files.
 *
 * @author Anthony Goubard - Japplis
 */
@Messages({
    "LBL_Svg_LOADER=Files of Svg"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_Svg_LOADER",
        mimeType = "image/svg+xml",
        extension = {"svg"})
@DataObject.Registration(
        mimeType = "image/svg+xml",
        iconBase = "org/joeffice/drawing/drawing-16.png",
        displayName = "#LBL_Svg_LOADER",
        position = 300)
@ActionReferences({
    @ActionReference(
            path = "Loaders/image/svg+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/image/svg+xml/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/image/svg+xml/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/image/svg+xml/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/image/svg+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/image/svg+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/image/svg+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/image/svg+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/image/svg+xml/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class SvgDataObject extends OfficeDataObject implements CookieSet.Factory {

    private DrawingOpenSupport opener;
    private SvgSaveCookie saver;
    private Document content;

    public SvgDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        cookies.add(DrawingOpenSupport.class, this);
        cookies.add(SvgSaveCookie.class, this);
    }

    @Override
    public <T extends Node.Cookie> T createCookie(Class<T> type) {
        if (type.isAssignableFrom(DrawingOpenSupport.class)) {
            if (opener == null) {
                opener = new DrawingOpenSupport(getPrimaryEntry());
            }
            return (T) opener;
        }
        if (type.isAssignableFrom(SvgSaveCookie.class)) {
            if (saver == null) {
                saver = new SvgSaveCookie();
            }
            return (T) saver;
        }
        return null;
    }

    @Override
    public void setContent(Object document) {
        this.content = (Document) document;
        if (document != null) {
            setModified(true);
            getCookieSet().add(saver);
        } else {
            setModified(false);
            getCookieSet().remove(saver);
        }
    }

    /**
     * Cookie invoked when the file is saved.
     * Note that if the file is not edited, no save cookie is in the cookies set.
     */
    private class SvgSaveCookie implements SaveCookie {

        @Override
        public void save() throws IOException {
            Document svgRoot;
            synchronized (SvgDataObject.this) {
                //synchronize access to the content field
                svgRoot = content;
                setContent(null);
            }
            File svgFile = FileUtil.toFile(getPrimaryFile());
            try (FileWriter svgWriter  = new FileWriter(svgFile)) {
                DOMUtilities.writeDocument(svgRoot, svgWriter);
            }
        }
    }
}
