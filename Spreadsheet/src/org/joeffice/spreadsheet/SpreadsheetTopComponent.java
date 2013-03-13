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

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.swing.*;
import javax.swing.text.DefaultEditorKit;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.joeffice.desktop.ui.OfficeTopComponent;


import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;

/**
 * Top component which displays the toolbar and the sheets tab panel.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.spreadsheet//Spreadsheet//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "SpreadsheetTopComponent",
        iconBase = "org/joeffice/spreadsheet/spreadsheet-16.png",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.joeffice.spreadsheet.SpreadsheetTopComponent")
/*@ActionReference(path = "Menu/Window")*/
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_SpreadsheetAction",
        preferredID = "SpreadsheetTopComponent")
@Messages({
    "CTL_SpreadsheetAction=Spreadsheet",
    "CTL_SpreadsheetTopComponent=Spreadsheet Window",
    "HINT_SpreadsheetTopComponent=This is a Spreadsheet window"
})
public final class SpreadsheetTopComponent extends OfficeTopComponent {

    private Workbook workbook;

    public SpreadsheetTopComponent() {
    }

    public SpreadsheetTopComponent(XlsxDataObject dataObject) {
        init(dataObject);
    }

    @Override
    protected JToolBar createToolbar() {
        JToolBar spreadsheetToolbar = new JToolBar();
        List<? extends Action> spreadsheetToolbarActions = Utilities.actionsForPath("Office/Spreadsheet/Toolbar");
        for (Action action : spreadsheetToolbarActions) {
            spreadsheetToolbar.add(action);
        }
        return spreadsheetToolbar;
    }

    @Override
    protected JComponent createMainComponent() {
        SpreadsheetComponent spreadsheet = new SpreadsheetComponent(this);
        return spreadsheet;
    }

    public SpreadsheetComponent getSpreadsheetComponent() {
        return (SpreadsheetComponent) getMainComponent();
    }

    public JTable getSelectedTable() {
        return getSpreadsheetComponent().getSelectedSheet().getTable();
    }

    @Override
    public void loadDocument(File xslxFile) {
        try {
            workbook = JoefficeWorkbookFactory.create(xslxFile);

            ((SpreadsheetComponent) getMainComponent()).load(workbook);
        } catch (IOException | InvalidFormatException ex) {
            Exceptions.attachMessage(ex, "Failed to load: " + xslxFile.getAbsolutePath());
            Exceptions.printStackTrace(ex);
        }
    }

    public void setModified(boolean modified) {
        if (modified) {
            getDataObject().setContent(workbook);
        } else {
            getDataObject().setContent(null);
        }
    }

    @Override
    protected void componentActivated() {
        getSpreadsheetComponent().registerActions();
        super.componentActivated();
    }

    @Override
    protected void componentDeactivated() {
        getSpreadsheetComponent().unregisterActions();
        super.componentDeactivated();
    }

    @Override
    public void writeProperties(Properties properties) {
        super.writeProperties(properties);
    }

    @Override
    public void readProperties(Properties properties) {
        super.readProperties(properties);
    }
}
