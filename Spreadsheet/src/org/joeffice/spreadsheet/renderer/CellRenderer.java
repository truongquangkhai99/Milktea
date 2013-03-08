package org.joeffice.spreadsheet.renderer;

import java.awt.Color;
import java.awt.Component;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.joeffice.spreadsheet.POIUtils;

/**
 * The POI cell renderer.
 *
 * @author Anthony Goubard - Japplis
 */
public class CellRenderer extends DefaultTableCellRenderer {

    // Formatters
    private final static DataFormatter DATA_FORMATTER = new DataFormatter();

    private final static TableCellRenderer DEFAULT_RENDERER = new CellRenderer();
    private FormulaEvaluator formulaEvaluator;

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
        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA && formulaEvaluator == null) {
            formulaEvaluator = cell.getRow().getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator();
        }
        String text = DATA_FORMATTER.formatCellValue(cell, formulaEvaluator);
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
        short verticalAlignment = style.getAlignment();
        if (verticalAlignment == CellStyle.VERTICAL_TOP) {
            setVerticalAlignment(SwingConstants.TOP);
        } else if (verticalAlignment == CellStyle.VERTICAL_CENTER) {
            setVerticalAlignment(SwingConstants.CENTER);
        } else if (verticalAlignment == CellStyle.VERTICAL_BOTTOM) {
            setVerticalAlignment(SwingConstants.BOTTOM);
        }
    }

    public static void decorateComponent(Cell cell, JComponent renderingComponent, JComponent defaultRenderer) {
        CellStyle style = cell.getCellStyle();

        // Background
        short backgroundIndex = style.getFillBackgroundColor();
        Color backgroundColor = POIUtils.shortToColor(backgroundIndex);
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
            Color fontColor = POIUtils.shortToColor(fontColorIndex);
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

        // Borders
        // At the moment done in renderer but should be done with a JLayer to paint over the grid
        renderingComponent.setBorder(new CellBorder(cell));

        if (cell.getCellComment() != null) {
            renderingComponent.setToolTipText(cell.getCellComment().getString().getString());
        }
    }
}
