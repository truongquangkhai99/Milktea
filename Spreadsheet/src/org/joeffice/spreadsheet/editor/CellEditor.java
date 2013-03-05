package org.joeffice.spreadsheet.editor;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class CellEditor extends AbstractCellEditor implements TableCellEditor {

    private JComponent currentComponent;

    @Override
    public Object getCellEditorValue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value instanceof String) {
            currentComponent = new JTextField((String) value);
        }
        return currentComponent;
    }
}
