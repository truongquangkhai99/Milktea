package org.joeffice.spreadsheet;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.joeffice.spreadsheet.csv.SmartCsvReader;

/**
 * Workbook factory that also support CSV files.
 *
 * @author Anthony Goubard - Japplis
 */
public class JoefficeWorkbookFactory extends WorkbookFactory {

    public static Workbook create(File file) throws IOException, InvalidFormatException {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".csv") || fileName.endsWith(".txt")) {
            SmartCsvReader csvReader = new SmartCsvReader();
            return csvReader.read(file);
        } else {
            return WorkbookFactory.create(file);
        }
    }
}
