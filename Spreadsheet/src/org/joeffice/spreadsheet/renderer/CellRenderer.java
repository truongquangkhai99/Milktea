package org.joeffice.spreadsheet.renderer;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import org.apache.poi.ss.usermodel.Cell;

/**
 * The POI cell renderer.
 * 
 * @author Anthony Goubard - Japplis
 */
public class CellRenderer extends DefaultTableCellRenderer {

    public final static JLabel EMPTY_CELL = new JLabel();

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (value != null) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            Cell cell = (Cell) value;
            decorateLabel(cell);
            return this;
        } else {
            return EMPTY_CELL;
        }
    }

    public void decorateLabel(Cell cell) {
        setText(cell.getStringCellValue());
        short foregroundColor = cell.getCellStyle().getFillForegroundColor();
    }
}
