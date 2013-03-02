package org.joeffice.drawing;

import org.joeffice.desktop.file.OfficeDataObject;
import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 * Cookie for opening (and closing) the drawing files (only svg supported now).
 * In this file the data object is associated with the top element used to visualize the document.
 *
 * @author Anthony Goubard - Japplis
 */
public class DrawingOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public DrawingOpenSupport(MultiDataObject.Entry entry) {
        super(entry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        return new DrawingTopComponent((OfficeDataObject) entry.getDataObject());
    }
}
