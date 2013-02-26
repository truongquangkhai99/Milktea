package org.joeffice.spreadsheet;

import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 * Cookie for opening (and closing) the xlsx files.
 * In this file the data object is associated with the top element used to visualize the document.
 *
 * @author Anthony Goubard - Japplis
 */
public class XlsxOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public XlsxOpenSupport(MultiDataObject.Entry xlsxEntry) {
        super(xlsxEntry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        return new SpreadsheetTopComponent((XlsxDataObject) entry.getDataObject());
    }
}
