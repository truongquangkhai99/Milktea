package org.joeffice.spreadsheet.renderer;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;

/**
 * The POI cell renderer.
 *
 * @author Anthony Goubard - Japplis
 */
public class CellRenderer extends DefaultTableCellRenderer {

    // Formatters
    private final static NumberFormat NUMBER_FORMATTER = DecimalFormat.getInstance();
    private final static DateFormat DATE_FORMATTER = DateFormat.getDateInstance();
    private final static DateFormat TIME_FORMATTER = DateFormat.getTimeInstance();
    private final static NumberFormat CURRENCY_FORMATTER = DecimalFormat.getCurrencyInstance();
    private final static DataFormatter DATA_FORMATTER = new DataFormatter();

    private final static TableCellRenderer DEFAULT_RENDERER = new CellRenderer();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value != null) {
            JLabel defaultComponent = (JLabel) DEFAULT_RENDERER.getTableCellRendererComponent(table, null, isSelected, hasFocus, row, column);
            Cell cell = (Cell) value;
            decorateLabel(cell, defaultComponent);
        }
        return this;
    }

    public void decorateLabel(Cell cell, JLabel defaultRenderer) {

        // Text
        // String text = getFormattedText(cell);
        // XXX small bug with decimal not using the correct comma's
        String text = DATA_FORMATTER.formatCellValue(cell);
        setText(text);

        decorateComponent(cell, this, defaultRenderer);

        // Alignment
        CellStyle style = cell.getCellStyle();
        short alignment = style.getAlignment();
        if (alignment == CellStyle.ALIGN_CENTER) {
            setHorizontalAlignment(SwingConstants.CENTER);
        } else if (alignment == CellStyle.ALIGN_RIGHT) {
            setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
            setHorizontalAlignment(defaultRenderer.getHorizontalAlignment());
        }
    }

    private static Color shortToColor(short xlsColorIndex) {
        if (xlsColorIndex > 0) {
            HSSFColor xlsColor = HSSFColor.getIndexHash().get(new Integer(xlsColorIndex));
            if (xlsColor != null) {
                short[] rgb = xlsColor.getTriplet();
                return new Color(rgb[0], rgb[1], rgb[2]);
            }
        }
        return null;
    }

    public static void decorateComponent(Cell cell, JComponent renderingComponent, JComponent defaultRenderer) {
        CellStyle style = cell.getCellStyle();

        // Background
        short backgroundIndex = style.getFillBackgroundColor();
        Color backgroundColor = shortToColor(backgroundIndex);
        if (backgroundColor != null) {
            renderingComponent.setBackground(backgroundColor);
        } else {
            renderingComponent.setBackground(defaultRenderer.getBackground());
        }

        // Font and forground
        short fontIndex = style.getFontIndex();
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
                renderingComponent.setForeground(fontColor);
            } else {
                renderingComponent.setForeground(defaultRenderer.getForeground());
            }
            renderingComponent.setFont(font);
        } else {
            renderingComponent.setForeground(defaultRenderer.getForeground());
            renderingComponent.setFont(defaultRenderer.getFont());
        }
    }

    private String getFormattedText(Cell cell) {
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
