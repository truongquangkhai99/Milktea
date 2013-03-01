package org.joeffice.desktop.file;

import java.io.IOException;
import javax.swing.text.Document;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.cookies.SaveCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;
import org.openide.windows.TopComponent;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class OfficeOpenSaveSupport extends OpenSupport implements OpenCookie, SaveCookie, CloseCookie {

    public OfficeOpenSaveSupport(MultiDataObject.Entry fileEntry) {
        super(fileEntry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        //return new TopComponent((OfficeDataObject) entry.getDataObject());
        return null;
    }

    @Override
    public void save() throws IOException {
        OfficeDataObject officeDataObject = (OfficeDataObject) entry.getDataObject();
        /* Document doc;
        synchronized (officeDataObject) {
            //synchronize access to the content field
            doc = content;
            officeDataObject.setContent(null);
        }
        FileObject fo = officeDataObject.getPrimaryFile();
        DocxWriter writer = new DocxWriter(doc);
        writer.write(FileUtil.toFile(fo).getAbsolutePath());*/
    }
}
