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

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.RowSet;
import javax.swing.*;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.joeffice.database.JDBCTopComponent;
import org.joeffice.database.tablemodel.JDBCSheet;
import org.joeffice.database.tablemodel.TableMetaDataModel;
import org.joeffice.desktop.ui.OfficeTopComponent;
import org.joeffice.desktop.ui.OfficeUIUtils;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle;
import org.openide.util.NbBundle.Messages;

/**
 * Action that adds a new entry to the database.
 * A dialog is display to the user to fill the data.
 *
 * @author Anthony Goubard - Japplis
 */
@ActionID(
        category = "Edit/Office/Database",
        id = "org.joeffice.database.actions.AddEntryAction")
@ActionRegistration(
        displayName = "#CTL_AddEntryAction")
@ActionReferences(value = {
    @ActionReference(path = "Office/Database/Toolbar", position = 1000)})
@Messages("CTL_AddEntryAction=Add Entry...")
public final class AddEntryAction extends AbstractAction {

    private final static String COMPONENTS_KEY = "Components";

    @Override
    public void actionPerformed(ActionEvent ae) {
        JDBCTopComponent currentTopComponent = OfficeTopComponent.getSelectedComponent(JDBCTopComponent.class);
        if (currentTopComponent != null) {
            try {
                Connection conn = currentTopComponent.getDatabaseConnection();
                TableMetaDataModel metaData = new TableMetaDataModel(conn, currentTopComponent.getSelectedTableName());
                JPanel fieldsPanel = createFieldsPanel(metaData);
                String title = NbBundle.getMessage(getClass(), "CTL_AddEntryAction");
                DialogDescriptor descriptor = new DialogDescriptor(fieldsPanel, title);
                Object dialogResult = DialogDisplayer.getDefault().notify(descriptor);
                if (dialogResult == DialogDescriptor.OK_OPTION) {
                    JDBCSheet sheet = currentTopComponent.getSelectedTableComponent().getSheet();
                    addEntry(fieldsPanel, sheet);
                }
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    protected JPanel createFieldsPanel(TableMetaDataModel metaData) {
        JPanel fieldsPanel = new JPanel();
        GroupLayout layout = new GroupLayout(fieldsPanel);
        fieldsPanel.setLayout(layout);
        List<JLabel> fieldLabels = new ArrayList<>();
        List<JComponent> fieldComponents = new ArrayList<>();
        for (int i = 0; i < metaData.getColumnCount(); i++) {
            JLabel fieldLabel = getLabelForField(metaData, i);
            fieldLabels.add(fieldLabel);

            JComponent fieldComponent = getComponentForField(metaData, i);
            fieldComponents.add(fieldComponent);
        }
        fieldsPanel.putClientProperty(COMPONENTS_KEY, fieldComponents);

        GroupLayout.ParallelGroup labelGroup = layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false);
        for (JLabel fieldLabel : fieldLabels) {
            labelGroup.addComponent(fieldLabel, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
        }
        GroupLayout.ParallelGroup componentGroup = layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false);
        for (JComponent fieldComponent : fieldComponents) {
            componentGroup.addComponent(fieldComponent, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE);
        }
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(labelGroup)
                .addPreferredGap(ComponentPlacement.UNRELATED)
                .addGroup(componentGroup)
                .addContainerGap()));

        GroupLayout.SequentialGroup fieldsGroup = layout.createSequentialGroup().addContainerGap();
        for (int i = 0; i < fieldLabels.size(); i++) {
            JLabel fieldLabel = fieldLabels.get(i);
            JComponent fieldComponent = fieldComponents.get(i);
            fieldsGroup.addPreferredGap(ComponentPlacement.UNRELATED);
            fieldsGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(fieldLabel)
                    .addComponent(fieldComponent));
        }
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                .addGroup(fieldsGroup));
        return fieldsPanel;
    }

    private JLabel getLabelForField(TableMetaDataModel metaData, int index) {
        String columnName = (String) metaData.getValueAt(0, index);
        String labelText = OfficeUIUtils.toDisplayable(columnName);
        JLabel fieldLabel = new JLabel(labelText);
        if ((Boolean) metaData.getValueAt(2, index) || (Boolean) metaData.getValueAt(3, index)) {
            fieldLabel.setFont(fieldLabel.getFont().deriveFont(Font.BOLD));
        }
        return fieldLabel;
    }

    private JComponent getComponentForField(TableMetaDataModel metaData, int index) {
        JComponent fieldComponent;
        String fieldType = (String) metaData.getValueAt(1, index);
        switch (fieldType) {
            case "BOOLEAN":
                fieldComponent = new JCheckBox();
                break;
            default:
                fieldComponent = new JTextField(40);
        }
        return fieldComponent;
    }

    public void addEntry(JPanel fieldsPanel, JDBCSheet sheet) throws SQLException {
        RowSet dataModel = sheet.getDataModel();
        dataModel.moveToInsertRow();
        List<JComponent> components = (List<JComponent>) fieldsPanel.getClientProperty(COMPONENTS_KEY);
        for (int i = 0; i < components.size(); i++) {
            JComponent fieldComponent = components.get(i);
            if (fieldComponent instanceof JTextField) {
                String value = ((JTextField) fieldComponent).getText();
                sheet.setColumnValue(i, value);
            } else if (fieldComponent instanceof JCheckBox) {
                Boolean value = ((JCheckBox) fieldComponent).isSelected();
                sheet.setColumnValue(i, value);
            }
        }
        dataModel.insertRow();
        sheet.init();
    }
}
