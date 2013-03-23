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
package org.joeffice.spreadsheet;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 * The JTable used to display data.
 * This class is only to fix bugs or improve existing functionalities.
 *
 * @author Anthony Goubard - Japplis
 */
public class SheetTable extends JTable {

    private List<Point> selectedCells = new ArrayList<>();
    private Point previousCell;

    public SheetTable(TableModel tableModel) {
        super(tableModel);
    }

    @Override
    public void setRowHeight(int row, int rowHeight) {
        int oldRowHeight = getRowHeight(row);
        super.setRowHeight(row, rowHeight);
        // Fire the row changed
        firePropertyChange("singleRowHeight", oldRowHeight, row);
    }

    @Override
    public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
        Point cell = new Point(rowIndex, columnIndex);
        if (!cell.equals(previousCell) && !selectedCells.contains(cell)) {
            selectedCells.add(cell);
        }
        super.changeSelection(rowIndex, columnIndex, toggle, extend);
    }

    @Override
    public void clearSelection() {
        if (selectedCells != null) {
            selectedCells.clear();
        }
        super.clearSelection();
    }

    @Override
    public void addRowSelectionInterval(int index0, int index1) {
        int columnCount = getColumnCount();
        for (int i = index0; i <= index1; i++) {
            for (int j = 0; j < columnCount; j++) {
                Point cell = new Point(i, j);
                if (!selectedCells.contains(cell)) {
                    selectedCells.add(cell);
                }
            }
        }
        super.addRowSelectionInterval(index0, index1);
    }

    @Override
    public void removeRowSelectionInterval(int index0, int index1) {
        for (Point cell : selectedCells) {
            if (cell.x >= index0 && cell.x <= index1) {
                selectedCells.remove(cell);
            }
        }
        super.removeRowSelectionInterval(index0, index1);
    }

    @Override
    public void selectAll() {
        addRowSelectionInterval(0, getRowCount());
        super.selectAll();
    }

    @Override
    public boolean isCellSelected(int row, int column) {
        return selectedCells.contains(new Point(row, column));
    }
}
