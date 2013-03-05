package org.joeffice.spreadsheet.rows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.joeffice.spreadsheet.actions.InsertRowsAction;
import org.joeffice.spreadsheet.actions.RemoveRowsAction;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class RowEventsListeners implements PropertyChangeListener, ActionListener, ListSelectionListener, MouseListener {

    private RowTable rowTable;
    Action insertRows;
    Action removeRows;

    public RowEventsListeners(RowTable rowTable) {
        this.rowTable = rowTable;
        insertRows = new InsertRowsAction(rowTable.getDataTable());
        removeRows = new RemoveRowsAction(rowTable.getDataTable());
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

    private void showPopup(MouseEvent me) {
        if (me.isPopupTrigger()) {
            int row = rowTable.rowAtPoint(me.getPoint());
            rowTable.getSelectionModel().addSelectionInterval(row, row);
            JPopupMenu menu = new JPopupMenu();
            menu.add(removeRows);
            menu.add(insertRows);
            menu.show(me.getComponent(), me.getX(), me.getY());
        }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        showPopup(me);
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        showPopup(me);
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
}
