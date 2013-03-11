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
package org.joeffice.wordprocessor.view;

import javax.swing.text.*;

/**
 * A factory to create a view of some portion of document subject.
 * This is intended to enable customization of how views get
 * mapped over a document model.
 *
 * @author	Stanislav Lapitsky
 */
public class DocxViewFactory implements ViewFactory {

    /**
     * Constructs new instance.
     */
    public DocxViewFactory() {
    }

    /**
     * Creates a view from the given structural element of a
     * document.
     *
     * @param elem  the piece of the document to build a view of
     * @return the view
     * @see View
     */
    @Override
    public View create(Element elem) {
        String kind = elem.getName();
        if (kind != null) {
            if (kind.equals(AbstractDocument.ContentElementName)) {
                return new LabelView(elem);
            } else if (kind.equals(AbstractDocument.ParagraphElementName)) {
                return new ParagraphView(elem);
            } else if (kind.equals(AbstractDocument.SectionElementName)) {
                return new SectionView(elem, View.Y_AXIS);
            } else if (kind.equals(StyleConstants.ComponentElementName)) {
                return new ComponentView(elem);
            } else if (kind.equals(StyleConstants.IconElementName)) {
                return new IconView(elem);
            } else if (kind.equals("table")) {
                return new TableView(elem);
            } else if (kind.equals("row")) {
                return new RowView(elem);
            } else if (kind.equals("cell")) {
                return new CellView(elem);
            }
        }
        // default to text display
        return new LabelView(elem);
    }
}