package org.joeffice.presentation;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;

import org.joeffice.desktop.OfficeUIUtils;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.CloneableTopComponent;

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
public final class SlidesTopComponent extends CloneableTopComponent {

    private PptxDataObject pptxDataObject;
    private XMLSlideShow presentation;
    private JPanel slidesHolder;

    public SlidesTopComponent() {
        init();
    }

    public SlidesTopComponent(PptxDataObject pptxDataObject) {
        this.pptxDataObject = pptxDataObject;
        init();
    }

    private void init() {
        initComponents();
        FileObject docxFileObject = pptxDataObject.getPrimaryFile();
        String fileDisplayName = FileUtil.getFileDisplayName(docxFileObject);
        setToolTipText(fileDisplayName);
        setName(docxFileObject.getName());

        loadDocument();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        JToolBar presentationToolbar = createToolbar();
        JComponent slidesHolder = createSlidesHolder();

        add(presentationToolbar, BorderLayout.NORTH);
        add(slidesHolder);
    }

    protected JToolBar createToolbar() {
        JToolBar spreadsheetToolbar = new JToolBar();
        return spreadsheetToolbar;
    }

    protected JComponent createSlidesHolder() {
        slidesHolder = new JPanel();
        slidesHolder.setLayout(new BoxLayout(slidesHolder, BoxLayout.Y_AXIS));
        return new JScrollPane(slidesHolder);
    }

    private void loadDocument() {
        File pptxFile = FileUtil.toFile(pptxDataObject.getPrimaryFile());
        try (FileInputStream fis = new FileInputStream(pptxFile)) {
            presentation = new XMLSlideShow(fis);

            // Add the slides
            XSLFSlide[] slides = presentation.getSlides();
            for (XSLFSlide slide : slides) {
                SlideComponent slideComp = new SlideComponent(slide, this);
                slidesHolder.add(slideComp);
                slidesHolder.add(new JSeparator(JSeparator.HORIZONTAL));
            }
            pptxDataObject.setContent(null);
        } catch (IOException ex) {
            Exceptions.attachMessage(ex, "Failed to load: " + pptxFile.getAbsolutePath());
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public boolean canClose() {
        int answer = OfficeUIUtils.checkSaveBeforeClosing(pptxDataObject, this);
        boolean canClose = answer == JOptionPane.YES_OPTION || answer == JOptionPane.NO_OPTION;
        if (canClose && pptxDataObject != null) {
            pptxDataObject.setContent(null);
        }
        return canClose;
    }

    @Override
    public void componentClosed() {
    }

    public void setModified(boolean modified) {
        if (modified) {
            pptxDataObject.setContent(presentation);
        } else {
            pptxDataObject.setContent(null);
        }
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
