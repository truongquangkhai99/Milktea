package org.joeffice.desktop.file;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 * Class containing the visual association for the office document.
 *
 * @author Anthony Goubard - Japplis
 */
public class OfficeDataNode extends DataNode {

    public OfficeDataNode(OfficeDataObject dataObject, Lookup lookup) {
        super(dataObject, Children.LEAF, lookup);
    }
}
