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

import java.awt.Color;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFColor;

/**
 * Utility methods for Cell and Row manipulation.
 *
 * @author Anthony Goubard - Japplis
 */
public class POIUtils {

    private final static NumberFormat NUMBER_FORMATTER = DecimalFormat.getInstance();
    private final static DateFormat DATE_FORMATTER = DateFormat.getDateInstance();
    private final static DateFormat TIME_FORMATTER = DateFormat.getTimeInstance();
    private final static NumberFormat CURRENCY_FORMATTER = DecimalFormat.getCurrencyInstance();

    /**
     * Converts a POI color to an AWT color.
     */
    public static Color shortToColor(short xlsColorIndex) {
        if (xlsColorIndex > 0) {
            HSSFColor xlsColor = HSSFColor.getIndexHash().get(new Integer(xlsColorIndex));
            if (xlsColor != null) {
                short[] rgb = xlsColor.getTriplet();
                return new Color(rgb[0], rgb[1], rgb[2]);
                //return Color.decode(xlsColor.getHexString());
            }
        }
        return null;
    }

    /**
     * Converts a POI color to an AWT color.
     */
    public static Color poiToAwtColor(org.apache.poi.ss.usermodel.Color poiColor) {
        if (poiColor instanceof XSSFColor) {
            byte[] rgb = ((XSSFColor) poiColor).getARgb();
            if (rgb != null)  {
                return new Color(rgb[0], rgb[1], rgb[2]);
            }
        } else if (poiColor instanceof HSSFColor && !(poiColor instanceof HSSFColor.AUTOMATIC)) {
            short[] rgb = ((HSSFColor) poiColor).getTriplet();
            return new Color(rgb[0], rgb[1], rgb[2]);
        }
        return null;
    }

    /**
     * Converts a POI color to an AWT color.
     */
    public static short colorToShort(Color awtColor, Cell cell) {
        return -1;
    }

    public static Cell getCell(boolean createIfAbsent, Sheet sheet, int rowIndex, int columnIndex) {
        Cell cell = null;
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            cell = row.getCell(columnIndex);
            if (cell == null && createIfAbsent) {
                cell = row.createCell(columnIndex);
            }
        } else if (createIfAbsent) {
            row = sheet.createRow(rowIndex);
            cell = row.createCell(columnIndex);
        }
        return cell;
    }

    /**
     * Copy a cell to another column in the same row
     *
     * @param input
     * @param column
     */
    public static void copyCellToColumn(Row row, Cell input, int column) {
        if (input == null) {
            Cell destCell = row.getCell(column);
            if (destCell != null) {
                row.removeCell(destCell);
            }
        } else {
            Cell destCell = row.getCell(column);
            if (destCell == null) {
                destCell = row.createCell(column, input.getCellType());
            }
            copyCell(input, destCell);
        }
    }

    // From http://stackoverflow.com/questions/5785724/how-to-insert-a-row-between-two-rows-in-an-existing-excel-with-hssf-apache-poi
    public static void copyCell(Cell oldCell, Cell newCell) {
        newCell.setCellStyle(oldCell.getCellStyle());

        if (newCell.getCellComment() != null) {
            newCell.setCellComment(oldCell.getCellComment());
        }

        if (oldCell.getHyperlink() != null) {
            newCell.setHyperlink(oldCell.getHyperlink());
        }

        newCell.setCellType(oldCell.getCellType());

        // Set the cell data value
        switch (oldCell.getCellType()) {
            case Cell.CELL_TYPE_BLANK:
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                newCell.setCellValue(oldCell.getRichStringCellValue());
                break;
        }
    }

    public static String getFormattedText(Cell cell) {
        if (cell == null) {
            return "";
        }
        int type = cell.getCellType();
        if (type == Cell.CELL_TYPE_STRING) {
            return cell.getStringCellValue();
        } else if (type == Cell.CELL_TYPE_NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return DATE_FORMATTER.format(cell.getDateCellValue());
            } else {
                return NUMBER_FORMATTER.format(cell.getNumericCellValue());
            }
        } else if (type == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return "";
        }
    }
}
