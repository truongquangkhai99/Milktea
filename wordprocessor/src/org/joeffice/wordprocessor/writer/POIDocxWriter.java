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
package org.joeffice.wordprocessor.writer;

import javax.swing.text.Document;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

/**
 * Implements writer of Docx document.
 *
 * @author	Stanislav Lapitsky
 * @author Anthony Goubard - Japplis
 */
public class POIDocxWriter {

    /**
     * document instance to the writing.
     */
    private Document document;

    private XWPFDocument poiDocument;

    /**
     * Constructs new writer instance.
     *
     * @param	doc document for writing.
     */
    public POIDocxWriter(Document doc) {
        document = doc;
    }

    /**
     * Performs writing to a file.
     *
     * @param	fileName Name of file
     * @exception	IOException occure when writing is failed.
     */
    public void write(String fileName) throws IOException {
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            poiDocument = (XWPFDocument) document.getProperty("XWPFDocument");
            poiDocument.write(out);
        }
    }
}