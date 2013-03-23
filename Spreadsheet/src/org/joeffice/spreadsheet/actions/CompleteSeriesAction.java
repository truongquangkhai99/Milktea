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
package org.joeffice.spreadsheet.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.joeffice.desktop.ui.OfficeTopComponent;
import org.joeffice.spreadsheet.POIUtils;
import org.joeffice.spreadsheet.SpreadsheetTopComponent;
import org.joeffice.spreadsheet.tablemodel.SheetTableModel;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Completes the empty cell based on the content of the previous cells.
 *
 * Basic implementation that just fill the values with "1".
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office/Spreadsheet",
        id = "org.joeffice.spreadsheet.actions.CompleteSeriesAction")
@ActionRegistration(
        displayName = "#CTL_CompleteSeriesAction")
@ActionReferences(value = {
    @ActionReference(path = "Office/Spreadsheet/Toolbar", position = 700)})
@Messages("CTL_CompleteSeriesAction=Complete serie")
public final class CompleteSeriesAction  extends AbstractAction {

    @Override
    public void actionPerformed(ActionEvent e) {
        SpreadsheetTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(SpreadsheetTopComponent.class);
        if (currentTopComponent != null) {
            JTable currentTable = currentTopComponent.getSelectedTable();
            Sheet currentSheet = currentTopComponent.getCurentSheet();
            int[] selectedRows = currentTable.getSelectedRows();
            int[] selectedColumns = currentTable.getSelectedColumns();
            if (selectedRows.length == 0 || selectedColumns.length == 0) {
                return;
            }
            completeCells(currentSheet, selectedRows, selectedColumns);
            ((SheetTableModel) currentTable.getModel()).fireTableDataChanged();
        }
    }

    protected void completeCells(Sheet sheet, int[] selectedRows, int[] selectedColumns) {
        boolean completeRows = true;
        int firstRow = selectedRows[0];
        int firstColumn = selectedColumns[0];
        int lastRow = selectedRows[selectedRows.length - 1];
        int lastColumn = selectedColumns[selectedColumns.length - 1];
        for (int i = firstRow; i <= lastRow; i++) {
            for (int j = firstColumn; j <= lastColumn; j++) {
                Cell cell = POIUtils.getCell(true, sheet, i, j);
                String text = POIUtils.getFormattedText(cell);
                if (text.trim().equals("")) {
                    if (completeRows && j == firstColumn) {
                        completeRows = false;
                    }
                    List<String> previousValues;
                    if (completeRows) {
                        previousValues = getPreviousValues(sheet, firstColumn, j, completeRows, i);
                    } else {
                        previousValues = getPreviousValues(sheet, firstRow, i, completeRows, j);
                    }
                    String nextValue = getNextValue(previousValues);
                    cell.setCellValue(nextValue);
                }
            }
        }
    }

    protected List<String> getPreviousValues(Sheet sheet, int from, int to, boolean completeRows, int columnOrRow) {
        List<String> previousValues = new ArrayList<>();
        for (int i = from; i < to; i++) {
            Cell cell;
            if (completeRows) {
                cell = POIUtils.getCell(false, sheet, columnOrRow, i);
            } else {
                cell = POIUtils.getCell(false, sheet, i, columnOrRow);
            }
            String text = POIUtils.getFormattedText(cell);
            previousValues.add(text);
        }
        return previousValues;
    }

    protected String getNextValue(List<String> previousValues) {
        if (previousValues.isEmpty()) {
            return "1";
        }
        return "1";
    }
}
