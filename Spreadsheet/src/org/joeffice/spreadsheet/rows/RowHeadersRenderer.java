package org.joeffice.spreadsheet.rows;

import java.awt.Component;
import java.awt.Insets;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.TableCellRenderer;

/**
 * The renderer for the row headers.
 *
 * @author Anthony Goubard - Japplis
 */
public class RowHeadersRenderer extends JToggleButton implements TableCellRenderer {

    public RowHeadersRenderer() {
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBorderPainted(false);
        setBorder(null);
        setMargin(new Insets(0, 0, 0, 0));
        setContentAreaFilled(false);
        setText("" + value);
        return this;
    }
}
