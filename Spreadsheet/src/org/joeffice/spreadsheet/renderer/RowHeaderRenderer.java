package org.joeffice.spreadsheet.renderer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class RowHeaderRenderer extends JToggleButton implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText("" + row);
        return this;
    }
}
