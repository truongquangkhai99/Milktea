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

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.text.*;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

import org.openide.util.Exceptions;

/**
 * Transfer handler that support rich text.
 * Supported MIME types are plain text, HTML and RTF.
 *
 * @author Anthony Goubard - Japplis
 */
public class RichTextTransferHandler extends TransferHandler {

    /**
     * The paste method used for DnD and clipboard.
     */
    @Override
    public boolean importData(JComponent c, Transferable t) {
        if (canImport(new TransferSupport(c, t))) {
            JTextComponent textField = (JTextComponent) c;
            String rtfText = textFromTransferable(t, TransferableRichText.RTF_FLAVOR);
            if (rtfText != null) {
                addRichtText(rtfText, textField, new RTFEditorKit());
                return true;
            }
            String htmlText = textFromTransferable(t, TransferableRichText.HTML_FLAVOR);
            if (htmlText != null) {
                addRichtText(rtfText, textField, new HTMLEditorKit());
                return true;
            }
            String plainText = textFromTransferable(t, DataFlavor.stringFlavor);
            if (plainText != null) {
                try {
                    textField.getDocument().insertString(textField.getSelectionStart(), plainText, null);
                    return true;
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return false;
    }

    private String textFromTransferable(Transferable t, DataFlavor flavor) {
        if (t.isDataFlavorSupported(flavor)) {
            try {
                Object data = t.getTransferData(flavor);
                if (data instanceof InputStream) {
                    return readFully((InputStream) data, Charset.defaultCharset());
                } else if (data instanceof Reader) {
                    return readFully((Reader) data);
                } else if (data instanceof String) {
                    return (String) data;
                }
            } catch (UnsupportedFlavorException | IOException ex) {
            }
        }
        return null;
    }

    private String readFully(Reader reader) throws IOException {
        char[] buffer = new char[1024];
        StringBuilder dataAsString = new StringBuilder();
        int length = reader.read(buffer);
        while (length != -1) {
            dataAsString.append(buffer, 0, length);
            length = reader.read(buffer);
        }
        return dataAsString.toString();
    }

    private String readFully(InputStream stream, Charset charset) throws IOException {
        byte[] buffer = new byte[stream.available()];
        ByteArrayOutputStream allData = new ByteArrayOutputStream();
        int length = stream.read(buffer);
        while (length != -1) {
            allData.write(buffer, 0, length);
            length = stream.read(buffer);
        }
        return new String(allData.toByteArray(), charset);
    }

    @Override
    public void exportToClipboard(JComponent comp, Clipboard clip, int action) {
        Transferable content = createTransferable(comp);
        clip.setContents(content, null);
        /* Disabled as TransferableRichText.getTransferData is called afterwards by netbeans and the offset have changed
        if (action == MOVE) {
            JTextComponent textField = (JTextComponent) comp;
            try {
                textField.getDocument().remove(textField.getSelectionStart(), textField.getSelectionEnd() - textField.getSelectionStart());
            } catch (BadLocationException ex) {
                Exceptions.printStackTrace(ex);
            }
        }*/
    }

    @Override
    protected Transferable createTransferable(JComponent c) {
        JTextComponent textField = (JTextComponent) c;
        int selectionStart = textField.getSelectionStart();
        int selectionLength = textField.getSelectionEnd() - selectionStart;
        return new TransferableRichText(textField.getDocument(), selectionStart, selectionLength);
    }

    @Override
    public int getSourceActions(JComponent c) {
        return COPY_OR_MOVE;
    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {
        return true;
    }

    private void addRichtText(String richText, JTextComponent textField, EditorKit richEditor) {
        try {
            StringReader reader = new StringReader(richText);
            Document doc = richEditor.createDefaultDocument();
            richEditor.read(reader, doc, 0);

            addRichText(textField, doc.getRootElements()[0]);
        } catch (IOException | BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void addRichText(JTextComponent textField, Element... elements) throws BadLocationException {
        for (int i = 0; i < elements.length; i++) {
            Element element = elements[i];
            if (element.isLeaf()) {
                String text = element.getDocument().getText(element.getStartOffset(), element.getEndOffset() - element.getStartOffset());
                textField.getDocument().insertString(textField.getSelectionStart(), text, element.getAttributes());
            } else {
                Element[] children = new Element[element.getElementCount()];
                for (int j = 0; j < children.length; j++) {
                    children[j] = element.getElement(j);
                }
                addRichText(textField, children);
            }
        }
    }

}
