package org.joeffice.presentation;

import java.io.*;
import javax.swing.*;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.joeffice.desktop.file.OfficeDataObject;
import org.joeffice.desktop.ui.OfficeTopComponent;


import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays the toolbar and the presentation slides in edit mode.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.presentation//Slides//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "SlidesTopComponent",
        iconBase = "org/joeffice/presentation/presentation-16.png",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "explorer", openAtStartup = false)
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
        System.out.println("created");
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
