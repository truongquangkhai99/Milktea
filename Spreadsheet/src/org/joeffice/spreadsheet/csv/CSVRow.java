package org.joeffice.spreadsheet.csv;

import org.apache.poi.xssf.usermodel.XSSFRow;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class CSVRow extends XSSFRow {

    public CSVRow(CSVSheet sheet) {
        super(null, sheet);
    }
}
