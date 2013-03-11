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
package org.joeffice.presentation;

import java.io.*;
import java.util.Properties;
import javax.swing.*;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.joeffice.desktop.file.OfficeDataObject;
import org.joeffice.desktop.ui.OfficeTopComponent;


import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays the toolbar and the presentation slides in edit mode.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.presentation//SlidesTopComponent//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "SlidesTopComponent",
        iconBase = "org/joeffice/presentation/presentation-16.png",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.joeffice.presentation.SlidesTopComponent")
@ActionReference(path = "Menu/Window")
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_SlidesAction",
        preferredID = "SlidesTopComponent")
@Messages({
    "CTL_SlidesAction=Slides",
    "CTL_SlidesTopComponent=Slides Window",
    "HINT_SlidesTopComponent=This is a Slides window"
})
public final class SlidesTopComponent extends OfficeTopComponent {

    private transient XMLSlideShow presentation;

    public SlidesTopComponent() {
    }

    public SlidesTopComponent(OfficeDataObject pptxDataObject) {
        super(pptxDataObject);
    }

    @Override
    protected JComponent createMainComponent() {
        JPanel slidesHolder = new JPanel();
        slidesHolder.setLayout(new BoxLayout(slidesHolder, BoxLayout.Y_AXIS));
        return slidesHolder;
    }

    @Override
    public void loadDocument() {
        File pptxFile = FileUtil.toFile(getDataObject().getPrimaryFile());
        try (FileInputStream fis = new FileInputStream(pptxFile)) {
            presentation = new XMLSlideShow(fis);

            // Add the slides
            XSLFSlide[] slides = presentation.getSlides();
            if (slides.length > 0) {
                getMainComponent().add(new SlideSeparator());
            }
            for (XSLFSlide slide : slides) {
                SlideComponent slideComp = new SlideComponent(slide, this);
                getMainComponent().add(slideComp);
                // getMainComponent().add(new JSeparator(JSeparator.HORIZONTAL));
                getMainComponent().add(new SlideSeparator());
            }
            ((PptxDataObject) getDataObject()).setContent(null);
        } catch (IOException ex) {
            Exceptions.attachMessage(ex, "Failed to load: " + pptxFile.getAbsolutePath());
            Exceptions.printStackTrace(ex);
        }
    }

    public XMLSlideShow getPresentation() {
        return presentation;
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
