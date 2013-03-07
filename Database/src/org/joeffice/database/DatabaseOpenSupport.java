package org.joeffice.database;

import org.openide.cookies.CloseCookie;
import org.openide.cookies.OpenCookie;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.OpenSupport;
import org.openide.windows.CloneableTopComponent;

/**
 * Cookie for opening (and closing) the H2 database files (.h2.db).
 * In this file the data object is associated with the top element used to visualize the document.
 *
 * @author Anthony Goubard - Japplis
 */
public class DatabaseOpenSupport extends OpenSupport implements OpenCookie, CloseCookie {

    public DatabaseOpenSupport(MultiDataObject.Entry databaseEntry) {
        super(databaseEntry);
    }

    @Override
    protected CloneableTopComponent createCloneableTopComponent() {
        return new JDBCTopComponent((H2DataObject) entry.getDataObject());
    }
}
