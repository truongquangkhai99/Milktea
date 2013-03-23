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
package org.joeffice.wordprocessor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.ElementIterator;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import org.joeffice.desktop.file.OfficeDataObject;
import org.joeffice.desktop.ui.OfficeTopComponent;

import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.netbeans.api.settings.ConvertAsProperties;

import org.openide.awt.ActionID;
import org.openide.awt.UndoRedo;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.RequestProcessor;

/**
 * Top component which displays the docx documents.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.wordprocessor//Wordp//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "WordpTopComponent",
        iconBase = "org/joeffice/wordprocessor/wordp-16.png",
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
public final class WordpTopComponent extends OfficeTopComponent implements DocumentListener {

    private Document document;
    private XWPFDocument poiDocument;
    private EditorStyleable styleable;

    public WordpTopComponent() {
    }

    public WordpTopComponent(OfficeDataObject dataObject) {
        super(dataObject);
    }

    @Override
    protected JComponent createMainComponent() {
        JTextPane editor = new JTextPane();
        editor.setEditorKit(new DocxEditorKit());
        return editor;
    }

    @Override
    public void loadDocument(final File docxFile) {
        styleable = new EditorStyleable();
        RequestProcessor.getDefault().post(new Runnable() {
            @Override
            public void run() {
                ProgressHandle progress = ProgressHandleFactory.createHandle("Opening " + docxFile.getName());
                progress.start();
                try (FileInputStream docxIS = new FileInputStream(docxFile)) {
                    JTextPane wordProcessor = (JTextPane) getMainComponent();
                    wordProcessor.getEditorKit().read(docxIS, wordProcessor.getDocument(), 0);
                    document = wordProcessor.getDocument();
                    document.addDocumentListener(WordpTopComponent.this);
                    document.addUndoableEditListener((UndoRedo.Manager) getUndoRedo());
                    poiDocument = (XWPFDocument) document.getProperty("XWPFDocument");
                    getDataObject().setDocument(poiDocument);
                    //Spellchecker.register(wordProcessor); // Doesn't do anything (yet)
                    /*FindAction find = new FindAction();
                     getActionMap().put(find.getName(), find);
                     ReplaceAction replace = new ReplaceAction();
                     getActionMap().put(replace.getName(), replace);*/
                } catch (IOException | BadLocationException ex) {
                    Exceptions.attachMessage(ex, "Failed to load: " + docxFile.getAbsolutePath());
                    Exceptions.printStackTrace(ex);
                } finally {
                    progress.finish();
                }
            }
        });
    }

    @Override
    protected void componentActivated() {
        JTextPane wordProcessor = (JTextPane) getMainComponent();
        ActionMap editorActionMap = wordProcessor.getActionMap();
        getActionMap().put(DefaultEditorKit.cutAction, editorActionMap.get(DefaultEditorKit.cutAction));
        getActionMap().put(DefaultEditorKit.copyAction, editorActionMap.get(DefaultEditorKit.copyAction));
        getActionMap().put(DefaultEditorKit.pasteAction, editorActionMap.get(DefaultEditorKit.pasteAction));
        getServices().add(styleable);
        super.componentActivated();
    }

    @Override
    protected void componentDeactivated() {
        getServices().remove(styleable);
        super.componentDeactivated();
    }

    @Override
    public void writeProperties(java.util.Properties properties) {
        super.writeProperties(properties);
    }

    @Override
    public void readProperties(java.util.Properties properties) {
        super.readProperties(properties);
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
        int offset = de.getOffset();
        int length = de.getLength();
        try {
            String addedText = de.getDocument().getText(offset, length);
            XWPFRun run = getRunAt(offset, de.getDocument());
            if (run != null) {
                //run.setText(addedText);
                run.getCTR().getTArray(0).setStringValue(addedText); // modified text
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
        setModified(true);
    }

    public XWPFRun getRunAt(int offset, Document doc) {
        List<XWPFRun> runs = (List<XWPFRun>) doc.getProperty("XWPFRun");
        int currentOffet = 0;
        for (XWPFRun run : runs) {
            currentOffet += run.toString().length();
            if (currentOffet >= offset) {
                return run;
            }
        }
        return null;
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        setModified(true);
    }

    @Override
    public void changedUpdate(DocumentEvent de) {
        DefaultStyledDocument doc = (DefaultStyledDocument) de.getDocument();
        System.out.println("de " + de);
        System.out.println("de type " + de.getType());
        System.out.println("de length " + de.getLength());
        ElementIterator iter = new ElementIterator(de.getDocument());
        for (Element elem = iter.first(); elem != null; elem = iter.next()) {
            DocumentEvent.ElementChange change = de.getChange(elem);
            if (change != null) { // null means there was no change in elem
                System.out.println("Element " + elem.getName() + " (depth "
                        + iter.depth() + ") changed its children: "
                        + change.getChildrenRemoved().length
                        + " children removed, "
                        + change.getChildrenAdded().length
                        + " children added.\n");
            }
        }
        setModified(true);
    }
}
