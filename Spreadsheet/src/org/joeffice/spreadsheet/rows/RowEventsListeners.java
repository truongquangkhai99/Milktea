/*
 * Copyright 2013 Japplis.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.joeffice.spreadsheet.rows;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.openide.util.Utilities;

/**
 * Listeners for events that happen in the row header.
 *
 * @author Anthony Goubard - Japplis
 */
public class RowEventsListeners implements PropertyChangeListener, ListSelectionListener, MouseListener {

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

    private void showPopup(MouseEvent me) {
        if (me.isPopupTrigger()) {
            /*Action insertAction = Utilities.actionsForPath("Actions/Build/org-joeffice-spreadsheet-actions-InsertRowsAction").get(0);
            Action removeAction = Utilities.actionsForPath("Actions/Build/org-joeffice-spreadsheet-actions-RemoveRowsAction").get(0);
            JPopupMenu menu = Utilities.actionsToPopup(new Action[] {insertAction, removeAction}, me.getComponent());*/
            List<? extends Action> buildActions = Utilities.actionsForPath("Office/Spreadsheet/Rows/Popup");
            JPopupMenu menu = Utilities.actionsToPopup(buildActions.toArray(new Action[buildActions.size()]), me.getComponent());
            /*int row = rowTable.rowAtPoint(me.getPoint());
            rowTable.getSelectionModel().addSelectionInterval(row, row);
            JPopupMenu menu = new JPopupMenu();
            menu.add(removeRows);
            menu.add(insertRows);*/
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
