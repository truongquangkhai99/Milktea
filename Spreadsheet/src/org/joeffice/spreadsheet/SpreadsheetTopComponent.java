/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.spreadsheet;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.joeffice.desktop.OfficeUIUtils;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.CloneableTopComponent;

/**
 * Top component which displays the toolbar and the sheets tab panel.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.spreadsheet//Spreadsheet//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "SpreadsheetTopComponent",
        iconBase = "org/joeffice/spreadsheet/spreadsheet-16.png",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.joeffice.spreadsheet.SpreadsheetTopComponent")
/*@ActionReference(path = "Menu/Window")*/
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_SpreadsheetAction",
        preferredID = "SpreadsheetTopComponent")
@Messages({
    "CTL_SpreadsheetAction=Spreadsheet",
    "CTL_SpreadsheetTopComponent=Spreadsheet Window",
    "HINT_SpreadsheetTopComponent=This is a Spreadsheet window"
})
public final class SpreadsheetTopComponent extends CloneableTopComponent {

    private XlsxDataObject xlsxDataObject;

    private SpreadsheetComponent spreadsheetComponent;

    private Workbook workbook;

    public SpreadsheetTopComponent() {
    }

    public SpreadsheetTopComponent(XlsxDataObject dataObject) {
        this.xlsxDataObject = dataObject;
        init();
    }

    private void init() {
        initComponents();
        FileObject docxFileObject = xlsxDataObject.getPrimaryFile();
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
        JToolBar spreadsheetToolbar = createToolbar();
        spreadsheetComponent = createSpreadsheet();

        add(spreadsheetToolbar, BorderLayout.NORTH);
        add(spreadsheetComponent);
    }

    protected JToolBar createToolbar() {
        JToolBar spreadsheetToolbar = new JToolBar();
        return spreadsheetToolbar;
    }

    protected SpreadsheetComponent createSpreadsheet() {
        SpreadsheetComponent spreadsheet = new SpreadsheetComponent(this);
        return spreadsheet;
    }

    private void loadDocument() {
        File xslxFile = FileUtil.toFile(xlsxDataObject.getPrimaryFile());
        try {
            workbook = WorkbookFactory.create(xslxFile);

            spreadsheetComponent.load(workbook);
        } catch (IOException|InvalidFormatException ex) {
            Exceptions.attachMessage(ex, "Failed to load: " + xslxFile.getAbsolutePath());
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public boolean canClose() {
        int answer = OfficeUIUtils.checkSaveBeforeClosing(xlsxDataObject, this);
        boolean canClose = answer == JOptionPane.YES_OPTION || answer == JOptionPane.NO_OPTION;
        if (canClose && xlsxDataObject != null) {
            xlsxDataObject.setContent(null);
        }
        return canClose;
    }

    @Override
    public void componentClosed() {
    }

    public void setModified(boolean modified) {
        if (modified) {
            xlsxDataObject.setContent(workbook);
        } else {
            xlsxDataObject.setContent(null);
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
