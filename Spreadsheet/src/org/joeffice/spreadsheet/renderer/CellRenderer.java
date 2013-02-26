package org.joeffice.spreadsheet.renderer;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;

/**
 * The POI cell renderer.
 *
 * @author Anthony Goubard - Japplis
 */
public class CellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            Cell cell = (Cell) value;
            decorateLabel(cell);
        }
        return this;
    }

    public void decorateLabel(Cell cell) {
        int type = cell.getCellType();
        if (type == Cell.CELL_TYPE_STRING) {
            setText(cell.getStringCellValue());
            setHorizontalTextPosition(SwingConstants.LEADING);
        } else if (type == Cell.CELL_TYPE_NUMERIC) {
            setText("" + cell.getNumericCellValue());
            setHorizontalTextPosition(SwingConstants.RIGHT);
        } else if (type == Cell.CELL_TYPE_BOOLEAN) {
        } else if (type == Cell.CELL_TYPE_FORMULA) {
        }
        short backgroundIndex = cell.getCellStyle().getFillBackgroundColor();
        Color backgroundColor = shortToColor(backgroundIndex);
        if (backgroundColor != null) {
            setBackground(backgroundColor);
        } else {
            setBackground(Color.WHITE);
        }
        short fontIndex = cell.getCellStyle().getFontIndex();
        if (fontIndex > 0) {
            Font xlsFont = cell.getSheet().getWorkbook().getFontAt(fontIndex);
            java.awt.Font font = java.awt.Font.decode(xlsFont.getFontName());
            font = font.deriveFont((float) xlsFont.getFontHeightInPoints());
            font = font.deriveFont(java.awt.Font.PLAIN);
            if (xlsFont.getItalic()) {
                font = font.deriveFont(java.awt.Font.ITALIC);
            }
            if (xlsFont.getBoldweight() == Font.BOLDWEIGHT_BOLD) {
                font = font.deriveFont(java.awt.Font.BOLD);
            }
            if (xlsFont.getUnderline() > Font.U_NONE) {
                // no underline in fonts
            }
            short fontColorIndex = xlsFont.getColor();
            Color fontColor = shortToColor(fontColorIndex);
            if (fontColor != null) {
                setForeground(fontColor);
            } else {
                setForeground(Color.BLACK);
            }
            setFont(font);
        }
    }

    private Color shortToColor(short xlsColorIndex) {
        if (xlsColorIndex > 0) {
            HSSFColor xlsColor = HSSFColor.getIndexHash().get(new Integer(xlsColorIndex));
            if (xlsColor != null) {
                short[] rgb = xlsColor.getTriplet();
                return new Color(rgb[0], rgb[1], rgb[2]);
            }
        }
        return null;
    }
}
