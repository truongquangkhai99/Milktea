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
package org.joeffice.drawing;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Properties;
import javax.swing.JComponent;

import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;

import org.joeffice.desktop.file.OfficeDataObject;
import org.joeffice.desktop.ui.OfficeTopComponent;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.w3c.dom.Document;

/**
 * Top component which displays a drawing.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.drawing//Drawing//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "DrawingTopComponent",
        iconBase="org/joeffice/drawing/drawing-16.png",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.joeffice.drawing.DrawingTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_DrawingAction",
        preferredID = "DrawingTopComponent")
@Messages({
    "CTL_DrawingAction=Drawing",
    "CTL_DrawingTopComponent=Drawing Window",
    "HINT_DrawingTopComponent=This is a Drawing window"
})
public final class DrawingTopComponent extends OfficeTopComponent {

    //private Scene scene; // Visual library

    public DrawingTopComponent() {
    }

    public DrawingTopComponent(OfficeDataObject dataObject) {
        super(dataObject);
    }

    @Override
    protected JComponent createMainComponent() {
        //scene = new Scene();
        //JScrollPane scenePane = new JScrollPane(scene.createView());
        JSVGCanvas svgCanvas = new JSVGCanvas();
        svgCanvas.setEnableImageZoomInteractor(true);
        svgCanvas.setEnablePanInteractor(true);
        svgCanvas.setEnableZoomInteractor(true);
        return svgCanvas;
    }

    @Override
    protected void loadDocument() {
        JSVGCanvas svgCanvas = (JSVGCanvas) getMainComponent();
        File svgFile = FileUtil.toFile(getDataObject().getPrimaryFile());
        try {
            String uri = Utilities.toURI(svgFile).toURL().toString();
            svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {

                @Override
                public void documentLoadingCompleted(SVGDocumentLoaderEvent svgdle) {
                    // The loaded document is a org.apache.batik.dom.svg.SVGOMDocument
                    setModified(false);
                }
            });
            svgCanvas.setURI(uri);
        } catch (MalformedURLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void setModified(boolean modified) {
        if (modified) {
            Document svgDocument = ((JSVGCanvas) getMainComponent()).getSVGDocument();
            getDataObject().setContent(svgDocument);
        } else {
            getDataObject().setContent(null);
        }
    }

    @Override
    public void writeProperties(Properties properties) {
        super.writeProperties(properties);
    }

    @Override
    public void readProperties(Properties properties) {
        super.readProperties(properties);
    }
}
