package org.joeffice.wordprocessor;

import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class DocxOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public DocxOpenSupport(MultiDataObject.Entry docxEntry) {
        super(docxEntry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        return new WordpTopComponent((DocxDataObject) entry.getDataObject());
    }
}
