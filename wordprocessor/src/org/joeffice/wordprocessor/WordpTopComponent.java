package org.joeffice.wordprocessor;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.joeffice.desktop.ui.OfficeUIUtils;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.CloneableTopComponent;

/**
 * Top component which displays the docx documents.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.wordprocessor//Wordp//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "WordpTopComponent",
        iconBase="org/joeffice/wordprocessor/wordp-16.png",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.joeffice.wordprocessor.WordpTopComponent")
/*@ActionReference(path = "Menu/Window")*/
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_WordpAction",
        preferredID = "WordpTopComponent")
@Messages({
    "CTL_WordpAction=Wordp",
    "CTL_WordpTopComponent=Word processor Window",
    "HINT_WordpTopComponent=This is a Word processor window"
})
public final class WordpTopComponent extends CloneableTopComponent implements LookupListener, DocumentListener {

    private JEditorPane wordProcessor;

    private Document document;

    private DocxDataObject docxDataObject;

    public WordpTopComponent() {
    }

    public WordpTopComponent(DocxDataObject dataObject) {
        this.docxDataObject = dataObject;
        init(dataObject);
    }

    private void init(DocxDataObject dataObject) {
        initComponents();
        FileObject docxFileObject = dataObject.getPrimaryFile();
        String fileDisplayName = FileUtil.getFileDisplayName(docxFileObject);
        setToolTipText(fileDisplayName);
        setName(docxFileObject.getName());
        loadDocument(dataObject);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        JToolBar wordProcessingToolbar = createToolbar();
        wordProcessor = createEditorPane();
        JScrollPane editorScrollPane = new JScrollPane(wordProcessor);

        add(wordProcessingToolbar, BorderLayout.NORTH);
        add(editorScrollPane);
    }

    private JToolBar createToolbar() {
        JToolBar editorToolbar = new JToolBar();
        return editorToolbar;
    }

    private JEditorPane createEditorPane() {
        JEditorPane editor = new JTextPane();
        editor.setEditorKit(new DocxEditorKit());
        return editor;
    }

    private void loadDocument(DocxDataObject docxDataObject) {
        File docxFile = FileUtil.toFile(docxDataObject.getPrimaryFile());
        try (FileInputStream docxIS = new FileInputStream(docxFile)) {
            wordProcessor.getEditorKit().read(docxIS, wordProcessor.getDocument(), 0);
            document = wordProcessor.getDocument();
            document.addDocumentListener(this);
        } catch (IOException|BadLocationException ex) {
            Exceptions.attachMessage(ex, "Failed to load: " + docxFile.getAbsolutePath());
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public boolean canClose() {
        int answer = OfficeUIUtils.checkSaveBeforeClosing(docxDataObject, this);
        boolean canClose = answer == JOptionPane.YES_OPTION || answer == JOptionPane.NO_OPTION;
        if (canClose && docxDataObject != null) {
            docxDataObject.setContent(null);
        }
        return canClose;
    }

    public void setModified(boolean modified) {
        if (modified) {
            docxDataObject.setContent(document);
        } else {
            docxDataObject.setContent(null);
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

    @Override
    public void resultChanged(LookupEvent le) {

    }

    @Override
    public void insertUpdate(DocumentEvent de) {
        changedUpdate(de);
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        changedUpdate(de);
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        setModified(true);
    }
}
