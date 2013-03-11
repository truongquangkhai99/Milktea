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
package org.joeffice.wordprocessor.nb;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.ViewFactory;
import java.io.*;

import org.joeffice.wordprocessor.writer.DocxWriter;
import org.netbeans.editor.BaseKit;

import org.netbeans.modules.editor.NbEditorDocument;
import org.openide.util.Lookup;

/**
 * This is the implementation of editing functionality.
 *
 * @author Stanislav Lapitsky
 */
public class DocxEditorKit extends BaseKit {

    private Lookup lookup;

    public DocxEditorKit() {
        this(Lookup.getDefault());
    }

    public DocxEditorKit(Lookup lookup) {
        this.lookup = lookup;
    }

    @Override
    public Object clone() {
        return new DocxEditorKit(lookup);
    }

    @Override
    public String getContentType() {
        return "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    }

    @Override
    public void read(InputStream in, Document doc, int pos) throws IOException,
            BadLocationException {
        DocxReader db = new DocxReader(doc);
        db.read(in, pos);
    }

    @Override
    public void read(Reader in, Document doc, int pos) throws IOException,
            BadLocationException {

        BufferedReader br = new BufferedReader(in);

        String s = br.readLine();
        StringBuilder sb = new StringBuilder();
        while (s != null) {
            sb.append(s).append("\n");
            s = br.readLine();
        }
        System.out.println(sb.toString());
        read(new ByteArrayInputStream(sb.toString().getBytes("UTF-8")), doc, pos);
    }

    @Override
    public void write(OutputStream out, Document doc, int pos, int len)
            throws IOException, BadLocationException {
        DocxWriter writer = new DocxWriter(doc);
        writer.write(out, pos, len);
    }

    @Override
    public void write(Writer out, Document doc, int pos, int len)
            throws IOException, BadLocationException {
        throw new UnsupportedOperationException("method not implemented");
    }

    @Override
    public Document createDefaultDocument() {
        //Document doc = NbDocument.getDocument(new LookupProvider());
        //Document doc = new DocxDocument();
        Document doc = new NbEditorDocument(getContentType());
        return doc;
    }

    @Override
    public ViewFactory getViewFactory() {
        return new DocxViewFactory();
    }

    class LookupProvider implements Lookup.Provider {

        @Override
        public Lookup getLookup() {
            return lookup;
        }
    }
}