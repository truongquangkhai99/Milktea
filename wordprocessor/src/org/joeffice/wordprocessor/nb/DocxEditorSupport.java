package org.joeffice.wordprocessor.nb;

import java.io.IOException;
import java.io.InputStream;
import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.StyledDocument;

import org.joeffice.desktop.file.OfficeDataObject;

import org.openide.cookies.*;
import org.openide.filesystems.FileLock;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.text.DataEditorSupport;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.windows.CloneableTopComponent;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class DocxEditorSupport extends DataEditorSupport implements EditorCookie, OpenCookie, EditCookie, ViewCookie, CloseCookie, PrintCookie {

    private Lookup localLookup;

    public DocxEditorSupport(OfficeDataObject obj, Lookup lkp) {
        super(obj, lkp, new DocxEditorSupportEnv(obj));
        localLookup = lkp;
        setMIMEType(obj.getPrimaryFile().getMIMEType());
    }

    @Override
    protected void loadFromStreamToKit(StyledDocument document, InputStream inputStream, EditorKit editorKit) {
        try {
            editorKit.read(inputStream, document, 0);
        } catch (IOException | BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException ex) {
            }
        }
    }

    @Override
    protected Pane createPane() {
        return new Pane() {

            @Override
            public JEditorPane getEditorPane() {
                JEditorPane editor = new JTextPane();
                editor.setEditorKit(new DocxEditorKit(localLookup));
                editor.registerEditorKitForContentType(getDataObject().getPrimaryFile().getMIMEType(), DocxEditorKit.class.getName());
                return editor;
            }

            @Override
            public CloneableTopComponent getComponent() {
                return new WordpTopComponent((DocxDataObject) getDataObject());
            }

            @Override
            public void updateName() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void ensureVisible() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        };
    }

    protected StyledDocument createStyledDocument(EditorKit kit) {
        return (StyledDocument) kit.createDefaultDocument();
    }

    public static class DocxEditorSupportEnv extends DataEditorSupport.Env {

        public DocxEditorSupportEnv(DataObject obj) {
            super(obj);
        }

        @Override
        protected FileObject getFile() {
            return getDataObject().getPrimaryFile();
        }

        @Override
        protected FileLock takeLock() throws IOException {
            return null;
        }
    }
}
