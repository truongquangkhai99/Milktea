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
package org.joeffice.database.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import org.joeffice.database.JDBCTopComponent;
import org.joeffice.desktop.ui.OfficeTopComponent;
import org.netbeans.swing.etable.ETable;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;

/**
 * Action to allow the user to select the column to view.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "View/Office/Database",
        id = "org.joeffice.database.actions.ViewColumnsAction")
@ActionRegistration(
        iconBase = "org/joeffice/database/actions/tab_delete.png",
        displayName = "#CTL_ViewColumnsAction")
@ActionReferences(value = {
    @ActionReference(path = "Office/Database/Toolbar", position = 1050)})
@Messages({"CTL_ViewColumnsAction=View Columns",
    "MSG_AllColumns=All columns"})
public final class ViewColumnsAction implements ActionListener {

    private JCheckBox allComlumns;
    private List<JCheckBox> columnCheckboxes;

    @Override
    public void actionPerformed(ActionEvent e) {
        JDBCTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(JDBCTopComponent.class);
        if (currentTopComponent != null) {
            ETable table = (ETable) currentTopComponent.getSelectedTableComponent().getDataTable();
            table.showColumnSelectionDialog();
        }
        /*Object dialogResult = askColumns();
        if (dialogResult == DialogDescriptor.OK_OPTION) {
            JDBCTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(JDBCTopComponent.class);
            currentTopComponent.getSelectedTableComponent();
        }*/
    }

    /*public Object askColumns() {
        String title = NbBundle.getMessage(getClass(), "CTL_ViewColumnsAction");
        JPanel choicePanel = new JPanel();
        choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.Y_AXIS));
        String allColumnsText = NbBundle.getMessage(getClass(), "MSG_AllColumns");
        allComlumns = new JCheckBox(allColumnsText);
        choicePanel.add(allComlumns);
        choicePanel.add(new JSeparator());
        columnCheckboxes = new ArrayList<>();
        for (String columnName : getColumnNames()) {
            JCheckBox columnCheckbox = new JCheckBox(columnName);
            columnCheckboxes.add(columnCheckbox);
            choicePanel.add(columnCheckbox);
        }

        DialogDescriptor descriptor;
        if (columnCheckboxes.isEmpty()) {
            return DialogDescriptor.CANCEL_OPTION;
        } else if (columnCheckboxes.size() > FieldsPanel.LIMIT_FOR_SCROLLPANE) {
            descriptor = new DialogDescriptor(new JScrollPane(choicePanel), title);
        } else {
            descriptor = new DialogDescriptor(choicePanel, title);
        }
        Object dialogResult = DialogDisplayer.getDefault().notify(descriptor);
        return dialogResult;
    }

    private List<String> getColumnNames() {
        List<String> columnNames = new ArrayList<>();
        JDBCTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(JDBCTopComponent.class);
        if (currentTopComponent != null) {
            try {
                Connection conn = currentTopComponent.getDatabaseConnection();
                TableMetaDataModel metaData = new TableMetaDataModel(conn, currentTopComponent.getSelectedTableName());
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String columnName = (String) metaData.getValueAt(0, i);
                    String labelText = OfficeUIUtils.toDisplayable(columnName);
                    columnNames.add(labelText);
                }
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return columnNames;
    }*/
}
