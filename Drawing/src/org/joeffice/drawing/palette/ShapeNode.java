package org.joeffice.drawing.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class ShapeNode extends AbstractNode {

    private Shape shape;

    public ShapeNode(Shape shape) {
        super(Children.LEAF);
        this.shape = shape;
    }
}
