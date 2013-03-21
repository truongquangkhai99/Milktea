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
package org.joeffice.wordprocessor.reader;

import javax.swing.text.*;
import javax.swing.text.Document;
import java.awt.Color;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.joeffice.wordprocessor.DocxDocument;

import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.LineSpacingRule;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/**
 * Implements reader of document.
 *
 * @author	Stanislav Lapitsky
 * @author Anthony Goubard - Japplis
 */
public class POIDocxReader {

    public static final int INDENTS_MULTIPLIER = 20;
    /**
     * document instance to the building (for the editor kit).
     */
    private DocxDocument document;
    /**
     * Document as read from POI library
     */
    private XWPFDocument poiDocument;
    /**
     * Current offset in the document for insert action.
     */
    private int currentOffset = 0;
    private SimpleAttributeSet parAttrs;
    private SimpleAttributeSet charAttrs;

    /**
     * Builds new instance of reader.
     *
     * @param	doc document for reading to.
     */
    public POIDocxReader(Document doc) {
        document = (DocxDocument) doc;
    }

    /**
     * Reads content of specified file to the document.
     *
     * @param fileName path to the file.
     * @param offset offset to read the content.
     */
    public void read(String fileName, int offset) throws IOException, BadLocationException {
        try (FileInputStream input = new FileInputStream(fileName)) {
            read(input, offset);
        }
    }

    /**
     * Reads content of specified stream to the document.
     *
     * @param in stream.
     */
    public void read(InputStream in, int offset) throws IOException, BadLocationException {
        poiDocument = new XWPFDocument(in);

        iteratePart(poiDocument.getBodyElements());

        this.currentOffset = offset;
        document.getDocumentProperties().put("XWPFDocument", poiDocument);
    }

    public void iteratePart(List<IBodyElement> content) throws BadLocationException {
        for (IBodyElement elem : content) {
            /*if (charAttrs != null && parAttrs != null) {
                document.insertString(currentOffset, "\n", charAttrs);
                document.setParagraphAttributes(currentOffset, 1, parAttrs, true);
                currentOffset++;
            }*/
            if (elem instanceof XWPFParagraph) {
                processParagraph((XWPFParagraph) elem);
                if (elem != content.get(content.size() - 1)) {
                    document.insertString(currentOffset, "\n", charAttrs);
                    document.setParagraphAttributes(currentOffset, 1, parAttrs, true);
                    currentOffset++;
                } else {
                    document.setParagraphAttributes(currentOffset, 1, parAttrs, true);
                }
            } else if (elem instanceof XWPFTable) {
                processTable((XWPFTable) elem);
            } else {
                System.out.println(elem);
            }
        }
    }

    protected void processParagraph(XWPFParagraph paragraph) throws BadLocationException {
        parAttrs = new SimpleAttributeSet();
        ParagraphAlignment alignment = paragraph.getAlignment();
        if (alignment == ParagraphAlignment.CENTER) {
            StyleConstants.setAlignment(parAttrs, StyleConstants.ALIGN_CENTER);
        } else if (alignment == ParagraphAlignment.LEFT) {
            StyleConstants.setAlignment(parAttrs, StyleConstants.ALIGN_LEFT);
        } else if (alignment == ParagraphAlignment.RIGHT) {
            StyleConstants.setAlignment(parAttrs, StyleConstants.ALIGN_RIGHT);
        } else if (alignment == ParagraphAlignment.BOTH || alignment == ParagraphAlignment.DISTRIBUTE) {
            StyleConstants.setAlignment(parAttrs, StyleConstants.ALIGN_JUSTIFIED);
        }
        List<TabStop> tabs = new ArrayList<>();
        int leftIndentation = paragraph.getIndentationLeft();
        if (leftIndentation > 0) {
            float indentation = leftIndentation / INDENTS_MULTIPLIER;
            StyleConstants.setLeftIndent(parAttrs, indentation);
            /*TabStop stop = new TabStop(pos, TabStop.ALIGN_LEFT, TabStop.LEAD_NONE);
             tabs.add(stop);*/
        }
        int rightIndentation = paragraph.getIndentationRight();
        if (rightIndentation > 0) {
            float indentation = rightIndentation / INDENTS_MULTIPLIER;
            StyleConstants.setLeftIndent(parAttrs, indentation);
            /*TabStop stop = new TabStop(pos, TabStop.ALIGN_RIGHT, TabStop.LEAD_NONE);
             tabs.add(stop);*/
        }
        /*TabSet tabSet = new TabSet(tabs.toArray(new TabStop[tabs.size()]));
         StyleConstants.setTabSet(parAttrs, tabSet);*/
        int firstLineIndentation = paragraph.getIndentationFirstLine();
        if (firstLineIndentation > 0) {
            float indentation = firstLineIndentation / INDENTS_MULTIPLIER;
            StyleConstants.setFirstLineIndent(parAttrs, indentation);
            /*TabStop stop = new TabStop(pos, TabStop.ALIGN_RIGHT, TabStop.LEAD_NONE);
             tabs.add(stop);*/
        }

        int spacingBefore = paragraph.getSpacingBefore();
        if (spacingBefore > 0) {
            int before = spacingBefore / INDENTS_MULTIPLIER;
            StyleConstants.setSpaceAbove(parAttrs, before);
        }
        int spacingAfter = paragraph.getSpacingAfter();
        if (spacingAfter > 0) {
            int after = spacingAfter / INDENTS_MULTIPLIER;
            StyleConstants.setSpaceAbove(parAttrs, after);
        }
        LineSpacingRule spacingLine = paragraph.getSpacingLineRule();
        if (spacingLine == LineSpacingRule.AT_LEAST || spacingLine == LineSpacingRule.AUTO) {
            float spacing = spacingLine.getValue() / 240;
            StyleConstants.setLineSpacing(parAttrs, spacing);
        }
        document.setParagraphAttributes(currentOffset, 1, parAttrs, true);
        for (XWPFRun run : paragraph.getRuns()) {
            processRun(run);
        }
    }

    protected void processRun(XWPFRun run) throws BadLocationException {
        charAttrs = new SimpleAttributeSet();

        if (run.getFontSize() > 0) {
            int size = run.getFontSize();
            StyleConstants.setFontSize(charAttrs, size);
        }
        StyleConstants.setBold(charAttrs, run.isBold());
        StyleConstants.setItalic(charAttrs, run.isItalic());
        StyleConstants.setStrikeThrough(charAttrs, run.isStrike());
        boolean underlined = run.getUnderline() != UnderlinePatterns.NONE;
        StyleConstants.setUnderline(charAttrs, underlined);
        VerticalAlign verticalAlignment = run.getSubscript();
        if (verticalAlignment == VerticalAlign.SUBSCRIPT) {
            StyleConstants.setSubscript(parAttrs, true);
        } else if (verticalAlignment == VerticalAlign.SUPERSCRIPT) {
            StyleConstants.setSuperscript(parAttrs, true);
        } else {
            StyleConstants.setSubscript(parAttrs, false);
            StyleConstants.setSuperscript(parAttrs, false);
        }
        if (run.getFontFamily() != null) {
            StyleConstants.setFontFamily(charAttrs, run.getFontFamily());
        }
        if (run.getColor() != null) {
            String name = run.getColor();
            if (!name.toLowerCase().equals("auto")) {
                Color color = Color.decode("#" + name);
                StyleConstants.setForeground(charAttrs, color);
            }
        }
        // Not supported in POI
            /*if (run.getHighlight() != null) {
         String name = rPr.getHighlight().getVal();
         Color color = decodeHighlightName(name);
         StyleConstants.setBackground(charAttrs, color);
         }*/

        //iteratePictures(run.getEmbeddedPictures());
        String text = run.toString();
        document.insertString(currentOffset, text, charAttrs);
        currentOffset += text.length();
    }

    protected Color decodeHighlightName(String name) {
        switch (name.toLowerCase()) {
            case "yellow":
                return Color.YELLOW;
            case "blue":
                return Color.BLUE;
            case "cyan":
                return Color.CYAN;
            case "gray":
                return Color.GRAY;
            case "green":
                return Color.GREEN;
            case "magenta":
                return Color.MAGENTA;
            case "orange":
                return Color.ORANGE;
            case "pink":
                return Color.PINK;
            case "red":
                return Color.RED;
            case "white":
                return Color.WHITE;
        }
        return null;
    }

    protected void processTable(XWPFTable table) throws BadLocationException {

    }

    /*protected void processTable(XWPFTable table) throws BadLocationException {
        TblPr tblPr = table.getTblPr();
        TblGrid tblGrid = table.getTblGrid();
        SimpleAttributeSet tableAttrs = new SimpleAttributeSet();
        int cellCount = 0;
        int rowCount = 0;
        for (Object tblObj : table.getContent()) {
            if (tblObj instanceof Tr) {
                rowCount++;
                Tr row = (Tr) tblObj;
                cellCount = Math.max(cellCount, processRow(row));
            }
        }

        int[] rowHeights = new int[rowCount];
        for (int i = 0; i < rowHeights.length; i++) {
            rowHeights[i] = 1;
        }
        int[] colWidths = new int[cellCount];
        int i = 0;
        for (TblGridCol col : tblGrid.getGridCol()) {
            colWidths[i] = col.getW().intValue() / INDENTS_MULTIPLIER;
            i++;
        }

        document.insertTable(currentOffset, rowCount, cellCount, tableAttrs, colWidths, rowHeights);
        for (Object tblObj : table.getContent()) {
            if (tblObj instanceof Tr) {
                Tr row = (Tr) tblObj;
                for (Object rowObj : row.getContent()) {
                    if (rowObj instanceof Tc) {
                        Tc cell = (Tc) rowObj;
                        iteratePart(cell.getContent());
                        currentOffset++;
                    } else if (rowObj instanceof JAXBElement) {
                        JAXBElement el = (JAXBElement) rowObj;
                        if (el.getDeclaredType().equals(Tc.class)) {
                            Tc cell = (Tc) el.getValue();
                            iteratePart(cell.getContent());
                            currentOffset++;
                        }
                    }
                }
            }
        }
    }

    protected int processRow(Tr row) throws BadLocationException {
        int res = 0;
        for (Object rowObj : row.getContent()) {
            res++;
            if (rowObj instanceof Tc) {
                Tc cell = (Tc) rowObj;
                TcPr tcPr = cell.getTcPr();
//                iteratePart(cell.getContent());
            }
        }
        return res;
    }

    protected void processDrawing(Drawing drawing) throws BadLocationException {
        for (Object obj : drawing.getAnchorOrInline()) {
            if (obj instanceof Inline) {
                Inline inline = (Inline) obj;
                String id = inline.getGraphic().getGraphicData().getPic().getBlipFill().getBlip().getEmbed();
                insertImageFromId(id);
            } else if (obj instanceof Anchor) {
                Anchor anchor = (Anchor) obj;
                String id = anchor.getGraphic().getGraphicData().getPic().getBlipFill().getBlip().getEmbed();
                insertImageFromId(id);
            }
        }
    }

    private void insertImageFromId(String id) {
        Relationship rel = wordMLPackage.getMainDocumentPart().getRelationshipsPart().getRelationshipByID(id);

        Part p = wordMLPackage.getMainDocumentPart().getRelationshipsPart().getPart(rel);
        ByteBuffer bb = ((BinaryPartAbstractImage) p).getBuffer();
        byte[] bytes = new byte[bb.remaining()];
        bb.get(bytes);
        ImageIcon ii = new ImageIcon(bytes);
        document.insertPicture(ii, currentOffset);
        currentOffset++;
    }*/

    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            String filePath = args[0];
            POIDocxReader reader = new POIDocxReader(new DocxDocument());
            reader.read(filePath, 0);

            System.out.println(reader.document.getText(0, reader.document.getLength()));
        }
    }
}