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

import java.util.List;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import org.apache.poi.xwpf.usermodel.*;
import org.openide.util.Exceptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;

/**
 * Updates the XWPFDocument according to the changes of the DocxDocument.
 *
 * @author Anthony Goubard - Japplis
 */
public class DocumentUpdater implements DocumentListener {

    private XWPFDocument document;
    private int currentOffset;

    public DocumentUpdater(XWPFDocument document) {
        this.document = document;
    }

    public void insertString(String text, int offset) {
        try {
            currentOffset = 0;
            DocumentPosition position = searchPart(document.getBodyElements(), offset);
            if (position != null) {
                String oldText = position.text.getStringValue();
                String newText = oldText.substring(0, position.offsetInText) + text
                        + (position.offsetInText == oldText.length() ? "" : oldText.substring(position.offsetInText));
                position.text.setStringValue(newText);
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void remove(int offset, int length) {
        try {
            currentOffset = 0;
            DocumentPosition position = searchPart(document.getBodyElements(), offset);
            currentOffset = 0;
            DocumentPosition endPosition = searchPart(document.getBodyElements(), offset + length);
            if (position != null) {
                boolean deleted = deleteFromSameText(position, endPosition);
                if (!deleted) {
                    deleteFromSameText(position, null);
                    deleteInBetween(position, endPosition);
                    String endText = endPosition.text.getStringValue();
                    String newEndText = endText.substring(endPosition.offsetInText);
                    endPosition.text.setStringValue(newEndText);
                }
            }

        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private boolean deleteFromSameText(DocumentPosition position, DocumentPosition endPosition) {
        boolean sameText = endPosition == null || position.text == endPosition.text;
        if (sameText) {
            String oldValue = position.text.getStringValue();
            String newValue = oldValue.substring(0, position.offsetInText)
                    + (endPosition == null ? "" : oldValue.substring(endPosition.offsetInText));
            position.text.setStringValue(newValue);
        }
        return sameText;
    }

    private void deleteInBetween(DocumentPosition startPosition, DocumentPosition endPosition) {
        XWPFDocument doc = startPosition.run.getParagraph().getDocument();
        boolean deletePart = false;
        for (int elemIndex = 0; elemIndex  < doc.getBodyElements().size(); elemIndex++) {
            IBodyElement elem = doc.getBodyElements().get(elemIndex);
            if (!deletePart && startPosition.run.getParagraph() != elem) {
                // Skip
            } else if (!deletePart) {
                deletePart = true;
                deleteRuns(startPosition, true);
            } else if (elem != endPosition.run.getParagraph()) {
                doc.removeBodyElement(elemIndex);
                elemIndex--;
            } else {
                deleteRuns(endPosition, false);
                return;
            }
        }
    }

    private void deleteRuns(DocumentPosition position, boolean fromPosition) {
        boolean deleteRun = false;
        List<CTText> texts = position.run.getCTR().getTList();
        for (int textIndex = 0; textIndex < texts.size(); textIndex++) {
            CTText text = texts.get(textIndex);
            if (!fromPosition && text != position.text) {
                position.run.getCTR().removeT(0);
                textIndex--;
            } else if (!fromPosition) {
                return;
            }
            if (deleteRun) {
                position.run.getCTR().removeT(position.positionInRun + 1);
                textIndex--;
            } else if (fromPosition && text == position.text) {
                deleteRun = true;
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
        int offset = de.getOffset();
        int length = de.getLength();
        try {
            String addedText = de.getDocument().getText(offset, length);
            insertString(addedText, offset);
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void removeUpdate(DocumentEvent de) {
        int offset = de.getOffset();
        int length = de.getLength();
        remove(offset, length);
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
    }

    DocumentPosition searchPart(List<IBodyElement> content, int offset) throws BadLocationException {
        for (IBodyElement elem : content) {
            if (elem instanceof XWPFParagraph) {
                DocumentPosition position = searchParagraph((XWPFParagraph) elem, offset);
                if (position != null) {
                    return position;
                }
                currentOffset++;
            } else if (elem instanceof XWPFTable) {
                searchTable((XWPFTable) elem, offset);
            }
        }
        return null;
    }

    DocumentPosition searchParagraph(XWPFParagraph paragraph, int offset) throws BadLocationException {
        int runIndex = 0;
        for (XWPFRun run : paragraph.getRuns()) {
            DocumentPosition position = searchRun(run, offset);
            if (position != null) {
                position.positionInParagraph = runIndex;
                return position;
            }
            runIndex++;
        }
        return null;
    }

    DocumentPosition searchRun(XWPFRun run, int offset) throws BadLocationException {
        for (XWPFPicture picture : run.getEmbeddedPictures()) {
            // currentOffset++;
        }
        List<CTText> texts = run.getCTR().getTList();
        int textIndex = 0;
        for (CTText text : texts) {
            int textLength = text.getStringValue().length();
            if (currentOffset + textLength >= offset) {
                DocumentPosition position = new DocumentPosition();
                position.run = run;
                position.text = text;
                position.offsetInText = offset - currentOffset;
                position.positionInRun = textIndex;
                return position;
            } else {
                currentOffset += textLength;
            }
            textIndex++;
        }
        return null;
    }

    DocumentPosition searchTable(XWPFTable table, int offset) throws BadLocationException {
        for (XWPFTableRow row : table.getRows()) {
            for (XWPFTableCell cell : row.getTableCells()) {
                DocumentPosition position = searchPart(cell.getBodyElements(), offset);
                if (position != null) {
                    return position;
                }
            }
        }
        return null;
    }

    class DocumentPosition {

        private XWPFRun run;
        private CTText text;
        private int offsetInText;
        private int positionInRun; // index of the CTText
        private int positionInParagraph; // index of the run
    }
}
