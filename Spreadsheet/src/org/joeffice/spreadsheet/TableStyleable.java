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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.joeffice.desktop.ui.OfficeTopComponent;

import org.joeffice.desktop.ui.Styleable;

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
        SpreadsheetTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(SpreadsheetTopComponent.class);
        if (currentTopComponent != null &&
                currentTopComponent.getSelectedTable().getSelectedRow() > -1 &&
                currentTopComponent.getSelectedTable().getSelectedColumn() > -1) {
            JTable table = currentTopComponent.getSelectedTable();
            Sheet sheet = currentTopComponent.getSpreadsheetComponent().getSelectedSheet().getSheet();

            int rowIndexStart = table.getSelectedRow();
            int rowIndexEnd = table.getSelectionModel().getMaxSelectionIndex();
            int[] selectedColumns1 = table.getSelectedColumns();
            int[] selectedColumns = POIUtils.getSelectedColumns(table, table.getSelectedRows());
            int colIndexStart = selectedColumns[0];
            int colIndexEnd = selectedColumns[selectedColumns.length - 1];

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
        Workbook workbook = cell.getSheet().getWorkbook();
        CellStyle style = cell.getSheet().getWorkbook().createCellStyle();
        style.cloneStyleFrom(oldStyle);
        Font newFont = copyFont(cell);
        if (attribute == FAMILY) {
            newFont.setFontName((String) attributeValue);
            CellUtil.setFont(cell, workbook, newFont);
        } else if (attribute == FOREGROUND) {
            Color color = (Color) attributeValue;
            if (cell instanceof XSSFCell) {
                ((XSSFCellStyle) style).setFillForegroundColor(new XSSFColor(color));
            } else {
                HSSFWorkbook xlsWorkbook = (HSSFWorkbook) workbook;
                HSSFColor xlsColor = xlsWorkbook.getCustomPalette().findColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                if (xlsColor == null) {
                    xlsColor = xlsWorkbook.getCustomPalette().addColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                }
                style.setFillForegroundColor(xlsColor.getIndex());
            }
            style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        } else if (attribute == BACKGROUND) {
            Color color = (Color) attributeValue;
            if (cell instanceof XSSFCell) {
                ((XSSFCellStyle) style).setFillBackgroundColor(new XSSFColor(color));
            } else {
                HSSFWorkbook xlsWorkbook = (HSSFWorkbook) workbook;
                HSSFColor xlsColor = xlsWorkbook.getCustomPalette().findColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                if (xlsColor == null) {
                    xlsColor = xlsWorkbook.getCustomPalette().addColor((byte) color.getRed(), (byte) color.getGreen(), (byte) color.getBlue());
                }
                style.setFillBackgroundColor(xlsColor.getIndex());
            }
        } else if (attribute == WEIGHT) {
            short boldValue = Font.BOLDWEIGHT_BOLD;
            if (newFont.getBoldweight() == Font.BOLDWEIGHT_BOLD) {
                boldValue = Font.BOLDWEIGHT_NORMAL;
            }
            newFont.setBoldweight(boldValue);
            CellUtil.setFont(cell, workbook, newFont);
        } else if (attribute == UNDERLINE) {
            byte underlineValue = Font.U_SINGLE;
            if (newFont.getUnderline() == Font.U_SINGLE) {
                underlineValue = Font.U_NONE;
            }
            newFont.setUnderline(underlineValue);
            CellUtil.setFont(cell, workbook, newFont);
        } else if (attribute == POSTURE) {
            boolean italic = true;
            if (newFont.getItalic()) {
                italic = false;
            }
            newFont.setItalic(italic);
            CellUtil.setFont(cell, workbook, newFont);
        } else if (attribute == SIZE) {
            newFont.setFontHeightInPoints(((Number) attributeValue).shortValue());
            CellUtil.setFont(cell, workbook, newFont);
        } else if (attribute == JUSTIFICATION) {
            CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_JUSTIFY);
        } else if (attribute == ALIGNMENT) {
            if (attributeValue.equals(StyleConstants.ALIGN_LEFT)) {
                CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_LEFT);
            } else if (attributeValue.equals(StyleConstants.ALIGN_RIGHT)) {
                CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_RIGHT);
            } else if (attributeValue.equals(StyleConstants.ALIGN_CENTER)) {
                CellUtil.setAlignment(cell, workbook, CellStyle.ALIGN_CENTER);
            }
        }
    }

    private Font copyFont(Cell cell) {
        CellStyle style = cell.getCellStyle();
        Workbook workbook = cell.getSheet().getWorkbook();
        short fontIndex = style.getFontIndex();
        Font xlsFont = cell.getSheet().getWorkbook().getFontAt(fontIndex);
        Font newFont = workbook.createFont();
        newFont.setFontName(xlsFont.getFontName());
        newFont.setFontHeight((short) xlsFont.getFontHeight());
        newFont.setBoldweight(xlsFont.getBoldweight());
        newFont.setItalic(xlsFont.getItalic());
        newFont.setUnderline(xlsFont.getUnderline());
        newFont.setColor(xlsFont.getColor());
        return newFont;
    }

    @Override
    public AttributedString getCommonFontAttributes() {
        AttributedString commonAttributes = new AttributedString("Selection");
        return commonAttributes;
    }
}
