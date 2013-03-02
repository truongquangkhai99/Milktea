package org.joeffice.drawing.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class CategoryNode extends AbstractNode {

    public CategoryNode(Category category) {
        super(Children.create(new ShapeNodeFactory(category.getName()), false));
        setDisplayName(category.getName());
    }
}