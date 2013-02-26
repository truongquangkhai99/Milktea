package org.joeffice.spreadsheet;

import static javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT;

import java.awt.BorderLayout;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Component that displays several sheets.
 *
 * @author Anthony Goubard - Japplis
 */
public class SpreadsheetComponent extends JPanel implements ChangeListener {

    private JTabbedPane sheets;
    private Workbook workbook;

    public SpreadsheetComponent() {
        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout());
        sheets = new JTabbedPane(JTabbedPane.BOTTOM, SCROLL_TAB_LAYOUT);
        sheets.addChangeListener(this);
        add(sheets);
    }

    public void load(Workbook workbook) {
        this.workbook = workbook;
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = workbook.getSheetName(i);
            JPanel sheetPanel = new SheetComponent(sheet);
            sheets.addTab(sheetName, sheetPanel);
        }
        sheets.setSelectedIndex(workbook.getActiveSheetIndex());
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        workbook.setActiveSheet(sheets.getSelectedIndex());
    }
}
