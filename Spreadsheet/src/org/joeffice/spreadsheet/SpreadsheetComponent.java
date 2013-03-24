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
package org.joeffice.spreadsheet;

import static javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.DefaultEditorKit;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.joeffice.desktop.ui.OfficeTopComponent;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Component that displays several sheets.
 *
 * @author Anthony Goubard - Japplis
 */
public class SpreadsheetComponent extends JTabbedPane implements ChangeListener {

    private Workbook workbook;
    private TableStyleable styleable;
    private SpreadsheetTopComponent spreadsheetAndToolbar;

    public SpreadsheetComponent(SpreadsheetTopComponent spreadsheetAndToolbar) {
        super(JTabbedPane.BOTTOM, SCROLL_TAB_LAYOUT);
        this.spreadsheetAndToolbar = spreadsheetAndToolbar;
        initComponents();
    }

    private void initComponents() {
        addPopupToTabs();
        styleable = new TableStyleable();
        addChangeListener(this);
    }

    public void load(Workbook workbook) {
        this.workbook = workbook;
        int numberOfSheets = workbook.getNumberOfSheets();
        for (int i = 0; i < numberOfSheets; i++) {
            Sheet sheet = workbook.getSheetAt(i);
            String sheetName = workbook.getSheetName(i);
            JPanel sheetPanel = new SheetComponent(sheet, this);
            addTab(sheetName, sheetPanel);
        }
        setSelectedIndex(workbook.getActiveSheetIndex());
    }

    public SpreadsheetTopComponent getSpreadsheetAndToolbar() {
        return spreadsheetAndToolbar;
    }

    public SheetComponent getSelectedSheet() {
        return (SheetComponent) getComponentAt(workbook.getActiveSheetIndex());
    }

    private void addPopupToTabs() {
        List<? extends Action> buildActions = Utilities.actionsForPath("Office/Spreadsheet/Tabs/Popup");
        final JPopupMenu menu = Utilities.actionsToPopup(buildActions.toArray(new Action[buildActions.size()]), this);
        addMouseListener(new MouseAdapter() {
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
                    menu.show(SpreadsheetComponent.this, me.getX(), me.getY());
                }
            }
        });
    }

    public void insertSheet(String name) throws IllegalArgumentException {
        Sheet sheet = workbook.createSheet(name);
        int newSheetPosition = workbook.getActiveSheetIndex() + 1;
        workbook.setSheetOrder(name, newSheetPosition);
        JPanel sheetPanel = new SheetComponent(sheet, this);
        insertTab(name, null, sheetPanel, null, newSheetPosition);

        setSelectedIndex(newSheetPosition);
        setModified(true);
    }

    public void removeCurrentSheet() {
        if (workbook.getNumberOfSheets() > 1) {
            int selectedSheetIndex = workbook.getActiveSheetIndex();
            workbook.removeSheetAt(selectedSheetIndex);
            remove(selectedSheetIndex);
            setModified(true);
        }
    }

    public void renameCurrentSheet(String newName) {
        int selectedSheetIndex = workbook.getActiveSheetIndex();
        workbook.setSheetName(selectedSheetIndex, newName);
        setTitleAt(selectedSheetIndex, newName);
        setModified(true);
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        workbook.setActiveSheet(getSelectedIndex());
        registerActions();
    }

    public void setModified(boolean modified) {
        spreadsheetAndToolbar.getDataObject().setModified(modified);
    }

    /**
     * Registers the table actions also in the TopComponent (for example to active global actions)
     */
    public void registerActions() {
        ActionMap topComponentActions = spreadsheetAndToolbar.getActionMap();
        ActionMap tableActions = getSelectedSheet().getTable().getActionMap();

        // Actives the cut / copy / paste buttons
        topComponentActions.put(DefaultEditorKit.cutAction, tableActions.get(DefaultEditorKit.cutAction));
        topComponentActions.put(DefaultEditorKit.copyAction, tableActions.get(DefaultEditorKit.copyAction));
        topComponentActions.put(DefaultEditorKit.pasteAction, tableActions.get(DefaultEditorKit.pasteAction));
        spreadsheetAndToolbar.getServices().add(styleable);
    }

    public void unregisterActions() {
        spreadsheetAndToolbar.getServices().remove(styleable);
    }

    public static SpreadsheetComponent getSelectedInstance() {
        SpreadsheetTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(SpreadsheetTopComponent.class);
        if (currentTopComponent != null) {
            return (SpreadsheetComponent) currentTopComponent.getMainComponent();
        }
        return null;
    }
}
