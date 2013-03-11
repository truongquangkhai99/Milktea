/*
 * Copyright 2013 Japplis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joeffice.drawing.palette;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 * From http://platform.netbeans.org/images/tutorials/vislib/CategoryChildren.java
 * To understand this class, see http://platform.netbeans.org/tutorials/nbm-nodesapi3.html
 *
 * @author Geertjan Wielenga
 */
public class CategoryChildren extends Children.Keys {

    private String[] Categories = new String[]{"Shapes"};

    public CategoryChildren() {
    }

    @Override
    protected Node[] createNodes(Object key) {
        Category obj = (Category) key;
        return new Node[]{new CategoryNode(obj)};
    }

    @Override
    protected void addNotify() {
        super.addNotify();
        Category[] objs = new Category[Categories.length];
        for (int i = 0; i < objs.length; i++) {
            Category cat = new Category();
            cat.setName(Categories[i]);
            objs[i] = cat;
        }
        setKeys(objs);
    }
}
