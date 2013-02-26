package org.joeffice.spreadsheet.renderer;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.TableCellRenderer;

/**
 * The renderer for the row headers.
 *
 * @author Anthony Goubard - Japplis
 */
public class RowHeadersRenderer extends JToggleButton implements TableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText("bla " + value);
        return this;
    }
}
