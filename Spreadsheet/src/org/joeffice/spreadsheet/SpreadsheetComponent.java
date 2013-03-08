package org.joeffice.spreadsheet;

import static javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openide.util.Utilities;

/**
 * Component that displays several sheets.
 *
 * @author Anthony Goubard - Japplis
 */
public class SpreadsheetComponent extends JPanel implements ChangeListener {

    private JTabbedPane sheets;
    private Workbook workbook;
    private SpreadsheetTopComponent spreadsheetAndToolbar;

    public SpreadsheetComponent(SpreadsheetTopComponent spreadsheetAndToolbar) {
        this.spreadsheetAndToolbar = spreadsheetAndToolbar;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        sheets = new JTabbedPane(JTabbedPane.BOTTOM, SCROLL_TAB_LAYOUT);
        addPopupToTabs();
        sheets.addChangeListener(this);
        add(sheets);
    }

    public void load(Workbook workbook) {
        this.workbook = workbook;
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = workbook.getSheetName(i);
            JPanel sheetPanel = new SheetComponent(sheet, this);
            sheets.addTab(sheetName, sheetPanel);
        }
        sheets.setSelectedIndex(workbook.getActiveSheetIndex());
    }

    public SpreadsheetTopComponent getSpreadsheetAndToolbar() {
        return spreadsheetAndToolbar;
    }

    public SheetComponent getSelectedSheet() {
        return (SheetComponent) sheets.getComponentAt(workbook.getActiveSheetIndex());
    }

    private void addPopupToTabs() {
        List<? extends Action> buildActions = Utilities.actionsForPath("Office/Spreadsheet/Tabs/Popup");
        final JPopupMenu menu = Utilities.actionsToPopup(buildActions.toArray(new Action[buildActions.size()]), sheets);
        sheets.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent me) {
                showPopup(me);
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                showPopup(me);
            }

            private void showPopup(MouseEvent me) {
                if (me.isPopupTrigger()) {
                    menu.show(sheets, me.getX(), me.getY());
                }
            }
        });
    }

    public void insertSheet(String name) throws IllegalArgumentException {
        Sheet sheet = workbook.createSheet(name);
        int newSheetPosition = workbook.getActiveSheetIndex() + 1;
        workbook.setSheetOrder(name, newSheetPosition);
        JPanel sheetPanel = new SheetComponent(sheet, this);
        sheets.insertTab(name, null, sheetPanel, null, newSheetPosition);

        sheets.setSelectedIndex(newSheetPosition);
        setModified(true);
    }

    public void removeCurrentSheet() {
        if (workbook.getNumberOfSheets() > 1) {
            int selectedSheetIndex = workbook.getActiveSheetIndex();
            workbook.removeSheetAt(selectedSheetIndex);
            sheets.remove(selectedSheetIndex);
            setModified(true);
        }
    }

    public void renameCurrentSheet(String newName) {
        int selectedSheetIndex = workbook.getActiveSheetIndex();
        workbook.setSheetName(selectedSheetIndex, newName);
        sheets.setTitleAt(selectedSheetIndex, newName);
        setModified(true);
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        workbook.setActiveSheet(sheets.getSelectedIndex());
    }

    public void setModified(boolean modified) {
        spreadsheetAndToolbar.setModified(modified);
    }
}
