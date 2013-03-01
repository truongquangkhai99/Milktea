package org.joeffice.desktop.file;

import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.MultiFileLoader;
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class OfficeDataObject extends MultiDataObject {

    public OfficeDataObject(FileObject pf, MultiFileLoader loader) throws DataObjectExistsException {
        super(pf, loader);
    }

    @Override
    protected Node createNodeDelegate() {
        return new OfficeDataNode(this, getLookup());
    }

    @Override
    public Lookup getLookup() {
        return getCookieSet().getLookup();
    }

    @Override
    protected int associateLookup() {
        return 1;
    }

}
