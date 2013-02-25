package org.joeffice.wordprocessor;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 * Class containing the visual association for the .docx extension.
 *
 * @author Anthony Goubard - Japplis
 */
public class DocxDataNode extends DataNode {

    public DocxDataNode(DocxDataObject dataObject, Lookup lookup) {
        super(dataObject, Children.LEAF, lookup);
    }
}
