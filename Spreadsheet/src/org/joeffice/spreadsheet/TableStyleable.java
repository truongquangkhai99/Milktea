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
package org.joeffice.spreadsheet;

import javax.swing.JTable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;

import static java.awt.font.TextAttribute.*;
import static org.joeffice.desktop.actions.ParagraphAttributes.*;

import java.awt.Color;
import java.awt.font.TextAttribute;
import java.text.AttributedCharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.text.AttributedString;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.StyleConstants;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;

import org.joeffice.desktop.ui.Styleable;
import org.openide.windows.TopComponent;

/**
 * Class that applies the style to the selected cells.
 *
 * This involves converting the {@link AttributedString} to a {@link CellStyle} and vice versa.
 *
 * @author Anthony Goubard - Japplis
 */
public class TableStyleable implements Styleable {

    @Override
    public void setFontAttributes(AttributedString attributes) {
        TopComponent currentTopComponent = TopComponent.getRegistry().getActivated();
        if (currentTopComponent instanceof SpreadsheetTopComponent) {
            JTable table = ((SpreadsheetTopComponent) currentTopComponent).getSelectedTable();
            Sheet sheet = ((SpreadsheetTopComponent) currentTopComponent).getSpreadsheetComponent().getSelectedSheet().getSheet();

            int rowIndexStart = table.getSelectedRow();
            int rowIndexEnd = table.getSelectionModel().getMaxSelectionIndex();
            int colIndexStart = table.getSelectedColumn();
            int colIndexEnd = table.getColumnModel().getSelectionModel().getMaxSelectionIndex();

            // Go through all the selected cells and all the attributes
            for (int i = rowIndexStart; i <= rowIndexEnd; i++) {
                for (int j = colIndexStart; j <= colIndexEnd; j++) {
                    if (table.isCellSelected(i, j)) {
                        AttributedCharacterIterator attributesIterator = attributes.getIterator();
                        for (Attribute attribute : attributesIterator.getAllAttributeKeys()) {
                            Object value = attributesIterator.getAttribute(attribute);
                            Cell cell = POIUtils.getCell(false, sheet, i, j);
                            if (cell != null) {
                                addAttribute(attribute, value, cell);
                            }
                            ((AbstractTableModel) table.getModel()).fireTableCellUpdated(i, j);
                        }
                    }
                }
            }
        }
    }

    /**
     * Add the attribute as defined in {@link AttributedString} to the {@link MutableAttributeSet} for the JTextPane.
     *
     * @see java.awt.font.TextAttribute
     */
    protected void addAttribute(AttributedCharacterIterator.Attribute attribute, Object attributeValue, Cell cell) {
        CellStyle oldStyle = cell.getCellStyle();
        CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
        style.cloneStyleFrom(oldStyle);
        short fontIndex = style.getFontIndex();
        org.apache.poi.ss.usermodel.Font xlsFont = cell.getSheet().getWorkbook().getFontAt(fontIndex);
        java.awt.Font font = java.awt.Font.decode(xlsFont.getFontName());
        if (attribute == FAMILY) {
            xlsFont.setFontName((String) attributeValue);
            style.setFont(xlsFont);
        } else if (attribute == FOREGROUND) {
            Color color = (Color) attributeValue;
            if (cell instanceof XSSFCell) {
                ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(color));
            } else {
                HSSFWorkbook workbook = (HSSFWorkbook) cell.getSheet().getWorkbook();
                HSSFColor xlsColor = workbook.getCustomPalette().findColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                if (xlsColor == null) {
                    xlsColor = workbook.getCustomPalette().addColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                }
                style.setFillForegroundColor(xlsColor.getIndex());
            }
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        } else if (attribute == BACKGROUND) {
            Color color = (Color) attributeValue;
            if (cell instanceof XSSFCell) {
                ((XSSFCellStyle) style).setFillBackgroundColor(new XSSFColor(color));
            } else {
                HSSFWorkbook workbook = (HSSFWorkbook) cell.getSheet().getWorkbook();
                HSSFColor xlsColor = workbook.getCustomPalette().findColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                if (xlsColor == null) {
                    xlsColor = workbook.getCustomPalette().addColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                }
                style.setFillBackgroundColor(xlsColor.getIndex());
            }
        } else if (attribute == WEIGHT) {
            short boldValue = Font.BOLDWEIGHT_BOLD;
            if (xlsFont.getBoldweight() == Font.BOLDWEIGHT_BOLD) {
                boldValue = Font.BOLDWEIGHT_NORMAL;
            }
            xlsFont.setBoldweight(boldValue);
            style.setFont(xlsFont);
        } else if (attribute == UNDERLINE) {
            byte underlineValue = Font.U_SINGLE;
            if (xlsFont.getUnderline() == Font.U_SINGLE) {
                underlineValue = Font.U_NONE;
            }
            xlsFont.setUnderline(underlineValue);
        } else if (attribute == POSTURE) {
            boolean italic = true;
            if (xlsFont.getItalic()) {
                italic = false;
            }
            xlsFont.setItalic(italic);
            style.setFont(xlsFont);
        } else if (attribute == SIZE) {
            xlsFont.setFontHeightInPoints(((Number) attributeValue).shortValue());
            style.setFont(xlsFont);
        } else if (attribute == JUSTIFICATION) {
            style.setAlignment(CellStyle.ALIGN_JUSTIFY);
        } else if (attribute == ALIGNMENT) {
            if (attributeValue.equals(StyleConstants.ALIGN_LEFT)) {
                style.setAlignment(CellStyle.ALIGN_LEFT);
            } else if (attributeValue.equals(StyleConstants.ALIGN_RIGHT)) {
                style.setAlignment(CellStyle.ALIGN_RIGHT);
            } else if (attributeValue.equals(StyleConstants.ALIGN_CENTER)) {
                style.setAlignment(CellStyle.ALIGN_CENTER);
            }
        }
        cell.setCellStyle(style);
    }

    @Override
    public AttributedString getCommonFontAttributes() {
        AttributedString commonAttributes = new AttributedString("Selection");
        return commonAttributes;
    }
}
