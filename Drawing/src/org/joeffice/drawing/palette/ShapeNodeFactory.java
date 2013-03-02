package org.joeffice.drawing.palette;

import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class ShapeNodeFactory extends ChildFactory<String> {

    public ShapeNodeFactory(String name) {
        
    }

    @Override
    protected boolean createKeys(List<String> toPopulate) {
        return false;
    }

   @Override
   protected Node[] createNodesForKey(String key) {
       return null;
   }
}
