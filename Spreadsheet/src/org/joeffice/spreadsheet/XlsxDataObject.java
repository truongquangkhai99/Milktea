/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.spreadsheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
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
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.CookieSet;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.NbBundle.Messages;

@Messages({
    "LBL_Xlsx_LOADER=Files of Xlsx"
})
@MIMEResolver.ExtensionRegistration(
        displayName = "#LBL_Xlsx_LOADER",
        mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        extension = {"xlsx"})
@DataObject.Registration(
        mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
        iconBase = "org/joeffice/spreadsheet/spreadsheet-16.png",
        displayName = "#LBL_Xlsx_LOADER",
        position = 300)
@ActionReferences({
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.OpenAction"),
            position = 100,
            separatorAfter = 200),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CutAction"),
            position = 300),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.CopyAction"),
            position = 400,
            separatorAfter = 500),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet/Actions",
            id =
            @ActionID(category = "Edit", id = "org.openide.actions.DeleteAction"),
            position = 600),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.RenameAction"),
            position = 700,
            separatorAfter = 800),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.SaveAsTemplateAction"),
            position = 900,
            separatorAfter = 1000),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.FileSystemAction"),
            position = 1100,
            separatorAfter = 1200),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.ToolsAction"),
            position = 1300),
    @ActionReference(
            path = "Loaders/application/vnd.openxmlformats-officedocument.spreadsheetml.sheet/Actions",
            id =
            @ActionID(category = "System", id = "org.openide.actions.PropertiesAction"),
            position = 1400)
})
public class XlsxDataObject extends OfficeDataObject implements CookieSet.Factory {

    // The document currently edited
    private Workbook content;

    private XlsxOpenSupport opener;
    private XlsxSaveCookie saver;

    public XlsxDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException, IOException {
        super(pf, loader);
        CookieSet cookies = getCookieSet();
        cookies.add(XlsxOpenSupport.class, this);
        cookies.add(XlsxSaveCookie.class, this);
    }

    @Override
    public synchronized void setContent(Object workbook) {
        this.content = (Workbook) workbook;
        if (workbook != null) {
            setModified(true);
            getCookieSet().add(saver);
        } else {
            setModified(false);
            getCookieSet().remove(saver);
        }
    }

    @Override
    public <T extends Node.Cookie> T createCookie(Class<T> type) {
        if (type.isAssignableFrom(XlsxOpenSupport.class)) {
            if (opener == null) {
                opener = new XlsxOpenSupport(getPrimaryEntry());
            }
            return (T) opener;
        }
        if (type.isAssignableFrom(XlsxSaveCookie.class)) {
            if (saver == null) {
                saver = new XlsxSaveCookie();
            }
            return (T) saver;
        }
        return null;
    }

    /**
     * Cookie invoked when the file is saved.
     * Note that if the file is not edited, no save cookie is in the cookies set.
     */
    private class XlsxSaveCookie implements SaveCookie {

        @Override
        public void save() throws IOException {
            Workbook workbook;
            synchronized (XlsxDataObject.this) {
                //synchronize access to the content field
                workbook = content;
                setContent(null);
            }
            File xslxFile = FileUtil.toFile(getPrimaryFile());
            try (FileOutputStream xslxOutputStream  = new FileOutputStream(xslxFile)) {
                workbook.write(xslxOutputStream);
            }
        }
    }
}
