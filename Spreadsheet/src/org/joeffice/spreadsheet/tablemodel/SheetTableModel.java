package org.joeffice.spreadsheet.tablemodel;

import javax.swing.table.AbstractTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

/**
 * The POI sheet table model.
 * 
 * @author Anthony Goubard - Japplis
 */
public class SheetTableModel extends AbstractTableModel {

    private Sheet sheet;

    public SheetTableModel(Sheet sheet) {
        this.sheet = sheet;
    }

    @Override
    public int getRowCount() {
        int lastRowNum = sheet.getLastRowNum();
        if (lastRowNum < 100) {
            return lastRowNum + 100;
        } else {
            return lastRowNum + 30;
        }
    }

    @Override
    public int getColumnCount() {
        int lastRowNum = sheet.getLastRowNum();
        int lastColumn = 0;
        for (int i = 0; i < lastRowNum; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                int lastCell = row.getLastCellNum() - 1;
                if (lastColumn < lastCell) {
                    lastColumn = lastCell;
                }
            }
        }
        if (lastColumn < 20) {
            return lastColumn + 26;
        } else {
            return lastColumn + 10;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            Cell cell = row.getCell(columnIndex);
            return cell;
        } else {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object newValue, int rowIndex, int columnIndex) {
        System.out.println("value " + newValue.getClass().getSimpleName());
        Row row = sheet.getRow(rowIndex);
        Cell cell = null;
        if (row != null) {
            cell = row.getCell(columnIndex);
            if (cell == null) {
                cell = row.createCell(columnIndex);
            }
        } else {
            row = sheet.createRow(rowIndex);
            cell = row.createCell(columnIndex);
        }
        cell.setCellValue((String) newValue);
    }
}
