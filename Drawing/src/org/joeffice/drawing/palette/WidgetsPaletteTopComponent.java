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

import org.netbeans.api.settings.ConvertAsProperties;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.drawing//WidgetsPalette//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "WidgetsPaletteTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE",
        persistenceType = TopComponent.PERSISTENCE_NEVER)
@TopComponent.Registration(mode = "commonpalette", openAtStartup = false)
@ActionID(category = "Window", id = "org.joeffice.drawing.WidgetsPaletteTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_WidgetsPaletteAction",
        preferredID = "WidgetsPaletteTopComponent")
@Messages({
    "CTL_WidgetsPaletteAction=WidgetsPalette",
    "CTL_WidgetsPaletteTopComponent=WidgetsPalette Window",
    "HINT_WidgetsPaletteTopComponent=This is a WidgetsPalette window"
})
public final class WidgetsPaletteTopComponent extends TopComponent {

    public WidgetsPaletteTopComponent() {
        initComponents();
        setName(Bundle.CTL_WidgetsPaletteTopComponent());
        setToolTipText(Bundle.HINT_WidgetsPaletteTopComponent());
        putClientProperty(TopComponent.PROP_MAXIMIZATION_DISABLED, Boolean.TRUE);

        Node root = new AbstractNode(Children.create(new ShapeNodeFactory("test"), false));
        /*PaletteActions paletteActions = new MyPaletteActions();
        PaletteController paletteController = PaletteFactory.createPalette(root, paletteActions);
        associateLookup(Lookups.fixed(paletteController));*/
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
