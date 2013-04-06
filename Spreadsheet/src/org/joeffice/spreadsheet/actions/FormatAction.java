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
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.JTable;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Workbook;
import org.joeffice.desktop.ui.OfficeTopComponent;
import org.joeffice.spreadsheet.POIUtils;
import org.joeffice.spreadsheet.SpreadsheetTopComponent;
import org.joeffice.spreadsheet.tablemodel.SheetTableModel;

/**
 * Action to apply a specific format on the cell.
 *
 * @author Anthony Goubard - Japplis
 */
public class FormatAction extends AbstractAction {

    private String pattern;

    public FormatAction(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        SpreadsheetTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(SpreadsheetTopComponent.class);
        if (currentTopComponent != null) {
            JTable currentTable = currentTopComponent.getSelectedTable();
            SheetTableModel tableModel = (SheetTableModel) currentTable.getModel();
            List<Cell> selectedCells = POIUtils.getSelectedCells(currentTable);
            if (selectedCells.isEmpty()) {
                return;
            }
            Workbook workbook = selectedCells.get(0).getSheet().getWorkbook();
            CellStyle formatStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            short formatIndex = format.getFormat(pattern);
            formatStyle.setDataFormat(formatIndex);
            for (Cell cell : selectedCells) {
                cell.setCellStyle(formatStyle);
                tableModel.fireTableCellUpdated(cell.getRowIndex(), cell.getColumnIndex());
            }
        }
    }
}
