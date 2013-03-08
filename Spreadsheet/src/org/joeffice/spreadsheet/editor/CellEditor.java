package org.joeffice.spreadsheet.editor;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import org.apache.poi.ss.usermodel.Cell;
import org.joeffice.spreadsheet.POIUtils;
import org.joeffice.spreadsheet.renderer.CellRenderer;

/**
 * Editor for POI Cell objects.
 *
 * @author Anthony Goubard - Japplis
 */
public class CellEditor extends DefaultCellEditor implements TableCellEditor {

    public final static CellEditor DEFAULT_EDITOR = new CellEditor();

    public CellEditor() {
        super(new JTextField());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        super.getTableCellEditorComponent(table, value, isSelected, row, column);
        if (value != null) {
            JComponent defaultComponent = (JComponent) DEFAULT_EDITOR.getTableCellEditorComponent(table, null, isSelected, row, column);
            Cell cell = (Cell) value;
            ((JTextField) getComponent()).setText(POIUtils.getFormattedText(cell));
            CellRenderer.decorateComponent(cell, (JComponent) getComponent(), defaultComponent);
        }
        return getComponent();
    }
}
