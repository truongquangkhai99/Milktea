package org.joeffice.presentation;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 * Class containing the visual association for the .docx extension.
 *
 * @author Anthony Goubard - Japplis
 */
public class PptxDataNode extends DataNode {

    public PptxDataNode(PptxDataObject dataObject, Lookup lookup) {
        super(dataObject, Children.LEAF, lookup);
    }
}
