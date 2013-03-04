package org.joeffice.spreadsheet.rows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class RowEventsListeners implements PropertyChangeListener, ActionListener, ListSelectionListener {

    private RowTable rowTable;

    public RowEventsListeners(RowTable rowTable) {
        this.rowTable = rowTable;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String property = evt.getPropertyName();
        if (property.equals("rowHeight")) {
            int newRowHeight = (Integer) evt.getNewValue();
            rowTable.setRowHeight(newRowHeight);
        }
        if (property.equals("singleRowHeight")) {
            int rowChanged = (Integer) evt.getNewValue();
            int newHeight = rowTable.getDataTable().getRowHeight(rowChanged);
            if (newHeight != rowTable.getRowHeight(rowChanged)) {
                rowTable.setRowHeight(rowChanged, newHeight);
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent lse) {
        ListSelectionModel rowSelectionModel = rowTable.getSelectionModel();
        ListSelectionModel dataSelectionModel = rowTable.getDataTable().getSelectionModel();
        for(int i = lse.getFirstIndex(); i<=lse.getLastIndex(); i++) {
            if (rowSelectionModel.isSelectedIndex(i)) {
                dataSelectionModel.addSelectionInterval(i, i);
            } else {
                dataSelectionModel.removeSelectionInterval(i, i);
            }
        }
    }

    // Not used to remove
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() instanceof JToggleButton) {
            // Get selected row
            JToggleButton button = (JToggleButton) ae.getSource();
            int row = rowTable.rowAtPoint(button.getLocation());
            ListSelectionModel dataSelection = rowTable.getDataTable().getSelectionModel();
            if (button.isSelected()) {
                dataSelection.addSelectionInterval(row, row);
            } else {
                dataSelection.removeIndexInterval(row, row);
            }
        }
    }
}
