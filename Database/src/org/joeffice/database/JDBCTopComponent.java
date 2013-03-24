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
package org.joeffice.database;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import org.joeffice.desktop.file.OfficeDataObject;

import org.joeffice.desktop.ui.OfficeTopComponent;
import org.joeffice.desktop.ui.OfficeUIUtils;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays the database data.
 */
@ConvertAsProperties(
        dtd = "-//org.joeffice.database//JDBC//EN",
        autostore = false)
@TopComponent.Description(
        preferredID = "JDBCTopComponent",
        iconBase = "org/joeffice/database/database-16.png",
        persistenceType = TopComponent.PERSISTENCE_ONLY_OPENED)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "org.joeffice.database.JDBCTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_JDBCAction",
        preferredID = "JDBCTopComponent")
@Messages({
    "CTL_JDBCAction=JDBC",
    "CTL_JDBCTopComponent=JDBC Window",
    "HINT_JDBCTopComponent=This is a JDBC window"
})
public final class JDBCTopComponent extends OfficeTopComponent {

    private transient Connection dbConnection;
    private List<String> tableNames;

    public JDBCTopComponent() {
    }

    public JDBCTopComponent(OfficeDataObject h2DataObject) {
        super(h2DataObject);
    }


    @Override
    protected JComponent createMainComponent() {
        JTabbedPane tablesComponent = new JTabbedPane(JTabbedPane.BOTTOM);
        return tablesComponent;
    }

    @Override
    public void loadDocument(File h2File) {
        try {
            dbConnection = H2DataObject.getConnection(h2File);
            ResultSet rsTableNames = dbConnection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
            tableNames = new ArrayList<>();
            while (rsTableNames.next()) {
                String nextTableName = rsTableNames.getString("TABLE_NAME");
                tableNames.add(nextTableName);

                TableComponent tableComp = new TableComponent(dbConnection, nextTableName);

                String tabLabel = OfficeUIUtils.toDisplayable(nextTableName);
                ((JTabbedPane) getMainComponent()).addTab(tabLabel, tableComp);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void componentClosed() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    public List<String> getTableNames() {
        return tableNames;
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
