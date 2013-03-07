package org.joeffice.spreadsheet.csv;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Workbook that handle csv files.
 *
 * @author Anthony Goubard - Japplis
 */
public class CSVWorkbook extends XSSFWorkbook {

    private SmartCsvReader reader;

    public CSVWorkbook(SmartCsvReader reader) {
        this.reader = reader;
    }

    // The write method is final in XSSFWorkbook so I've change the name
    public void write2(OutputStream output) throws IOException {
        reader.write(output, this);
    }
}
