package org.joeffice.spreadsheet;

import static javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT;

import java.awt.BorderLayout;
import javax.swing.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Component that displays several sheets.
 *
 * @author Anthony Goubard - Japplis
 */
public class SpreadsheetComponent extends JPanel {

    private JTabbedPane sheets;

    public SpreadsheetComponent() {
        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());
        sheets = new JTabbedPane(JTabbedPane.BOTTOM, SCROLL_TAB_LAYOUT);
        add(sheets);
    }

    public void load(Workbook workbook) {
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = workbook.getSheetName(i);
            JPanel sheetPanel = new SheetComponent(sheet);
            sheets.addTab(sheetName, sheetPanel);
        }
    }
}
