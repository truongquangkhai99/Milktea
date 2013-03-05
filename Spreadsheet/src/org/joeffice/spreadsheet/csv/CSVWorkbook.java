package org.joeffice.spreadsheet.csv;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Anthony Goubard - Japplis
 */
public class CSVWorkbook extends XSSFWorkbook {

    public XSSFSheet createSheet() {
        return new CSVSheet();
    }
}
