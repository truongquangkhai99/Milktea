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

import static javax.swing.text.StyleConstants.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
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
            StringTokenizer stText = new StringTokenizer(text, "\n", true);
            while (stText.hasMoreTokens()) {
                String textPart = stText.nextToken();
                DocumentPosition position = searchPart(document.getBodyElements(), offset);
                if (position != null) {
                    String oldText = position.text.getStringValue();
                    String newText = oldText.substring(0, position.offsetInText) + text
                            + (position.offsetInText == oldText.length() ? "" : oldText.substring(position.offsetInText));
                    position.text.setStringValue(newText);
                    if (textPart.endsWith("\n")) {
                        XmlCursor cursor = position.run.getParagraph().getCTP().newCursor();
                        cursor.toNextSibling();
                        XWPFParagraph newParagraph = document.insertNewParagraph(cursor);
                        newParagraph.setAlignment(position.run.getParagraph().getAlignment());
                        newParagraph.getCTP().insertNewR(0).insertNewT(0);
                        offset += textPart.length();
                    }
                }
            }
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public void insertImage(ImageIcon image, int offset) {
        try {
            currentOffset = 0;
            DocumentPosition position = searchPart(document.getBodyElements(), offset);
            if (position != null) {
                int width = image.getIconWidth();
                int height = image.getIconHeight();
                ByteArrayOutputStream output=new ByteArrayOutputStream();
                BufferedImage bi=new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                bi.getGraphics().drawImage(image.getImage(),0,0,null);
                ImageIO.write(bi, "png", output);
                byte[] imageData = output.toByteArray();
                InputStream imageDataStream = new ByteArrayInputStream(imageData);
                position.run.addPicture(imageDataStream, XWPFDocument.PICTURE_TYPE_PNG, image.getDescription(), width, height);
            }
        } catch (BadLocationException | IOException | InvalidFormatException ex) {
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
            if (newValue.isEmpty()) {
                position.run.getCTR().removeT(position.positionInRun);
                if (position.run.getCTR().getTList().isEmpty()) {
                    position.run.getParagraph().removeRun(position.positionInParagraph);
                }
            }
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
        if (position.run.getCTR().getTList().isEmpty()) {
            position.run.getParagraph().removeRun(position.positionInParagraph);
        }
    }

    @Override
    public void insertUpdate(DocumentEvent de) {
        int offset = de.getOffset();
        int length = de.getLength();
        try {
            Element selectedCharElement = ((StyledDocument) de.getDocument()).getCharacterElement(offset);
            ImageIcon image = (ImageIcon) StyleConstants.getIcon(selectedCharElement.getAttributes());
            if (image != null) {
                insertImage(image, offset);
            }
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
        ElementIterator iter = new ElementIterator(de.getDocument());
        for (Element elem = iter.first(); elem != null; elem = iter.next()) {
            DocumentEvent.ElementChange change = de.getChange(elem);
            if (change != null) {
                Element[] removedElems = change.getChildrenRemoved();
                for (int i = removedElems.length - 1; i>=0; i--) {
                    remove(removedElems[i].getStartOffset(), removedElems[i].getEndOffset());
                }
                for (Element addedElem : change.getChildrenAdded()) {
                    try {
                        currentOffset = 0;
                        DocumentPosition pos = searchPart(document.getBodyElements(), addedElem.getStartOffset());
                        if (pos != null) {
                            String text = doc.getText(addedElem.getStartOffset(), addedElem.getEndOffset() - addedElem.getStartOffset());
                            XWPFRun run = pos.run.getParagraph().createRun();
                            run.setText(text);
                            applyAttributes(run, addedElem.getAttributes());
                        }
                    } catch (BadLocationException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
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
                //currentOffset++;
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
        if (offset == currentOffset) {
            return createRun(paragraph);
        }
        return null;
    }

    DocumentPosition createRun(XWPFParagraph paragraph) {
        XWPFRun run = paragraph.createRun();
        DocumentPosition position = new DocumentPosition();
        position.run = run;
        position.text = run.getCTR().addNewT();
        return position;
    }

    DocumentPosition searchRun(XWPFRun run, int offset) throws BadLocationException {
        for (XWPFPicture picture : run.getEmbeddedPictures()) {
            // currentOffset++;
        }
        List<CTText> texts = run.getCTR().getTList();
        int textIndex = 0;
        for (CTText text : texts) {
            String textValue = text.getStringValue();
            int textLength = textValue.length();
            if (currentOffset + textLength > offset || (currentOffset + textLength == offset && !textValue.endsWith("\n"))) {
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

    public void applyAttributes(XWPFRun run, AttributeSet attributes) {
        Enumeration attributeNames = attributes.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            Object attributeName = attributeNames.nextElement();
            Object attributeValue = attributes.getAttribute(attributeName);
            if (attributeName == Bold) {
                run.setBold((Boolean) attributeValue);
            } else if (attributeName == Italic) {
                run.setItalic((Boolean) attributeValue);
            } else if (attributeName == Underline) {
                run.setUnderline((Boolean) attributeValue ? UnderlinePatterns.SINGLE : UnderlinePatterns.NONE);
            } else if (attributeName == FontFamily || attributeName == Family) {
                run.setFontFamily((String) attributeValue);
            } else if (attributeName == FontSize) {
                run.setFontSize(((Number) attributeValue).intValue());
            } else if (attributeName == Foreground) {
                Color color = (Color) attributeValue;
                String rgb = Integer.toHexString((color.getRGB() & 0xffffff) | 0x1000000).substring(1);
                run.setColor(rgb);
            } else if (attributeName == Background) {
                Color color = (Color) attributeValue;
                String rgb = Integer.toHexString((color.getRGB() & 0xffffff) | 0x1000000).substring(1);
                //run.getCTR().getRPr().setHighlight();
            } else if (attributeName == Subscript) {
                run.setSubscript((Boolean) attributeValue ? VerticalAlign.SUBSCRIPT : VerticalAlign.BASELINE);
            } else if (attributeName == Superscript) {
                run.setSubscript((Boolean) attributeValue ? VerticalAlign.SUPERSCRIPT : VerticalAlign.BASELINE);
            } else if (attributeName == StrikeThrough) {
                run.setStrike((Boolean) attributeValue);
            } else if (attributeName == Alignment) {
                ParagraphAlignment alignment = documentToPOI((Integer) attributeValue);
                run.getParagraph().setAlignment(alignment);
            }
        }
    }

    private ParagraphAlignment documentToPOI(int alignment) {
        switch (alignment) {
            case ALIGN_LEFT:
                return ParagraphAlignment.LEFT;
            case ALIGN_RIGHT:
                return ParagraphAlignment.RIGHT;
            case ALIGN_CENTER:
                return ParagraphAlignment.CENTER;
            case ALIGN_JUSTIFIED:
                return ParagraphAlignment.DISTRIBUTE;
        }
        return ParagraphAlignment.LEFT;
    }

    class DocumentPosition {

        private XWPFRun run;
        private CTText text;
        private int offsetInText;
        private int positionInRun; // index of the CTText
        private int positionInParagraph; // index of the run
    }
}
