/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.joeffice.database;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import org.joeffice.desktop.ui.OfficeUIUtils;

import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.CloneableTopComponent;

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
public final class JDBCTopComponent extends CloneableTopComponent {

    private H2DataObject h2DataObject;
    private transient JTabbedPane tables;
    private transient Connection dbConnection;

    public JDBCTopComponent() {
    }

    public JDBCTopComponent(H2DataObject h2DataObject) {
        init(h2DataObject);
    }

    private void init(H2DataObject h2DataObject) {
        this.h2DataObject = h2DataObject;
        initComponents();
        FileObject docxFileObject = h2DataObject.getPrimaryFile();
        String fileDisplayName = FileUtil.getFileDisplayName(docxFileObject);
        setToolTipText(fileDisplayName);
        setName(docxFileObject.getName());
        loadDocument();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        JToolBar databaseToolbar = createToolbar();
        tables = createTabbbedPane();

        add(databaseToolbar, BorderLayout.NORTH);
        add(tables);
    }

    protected JToolBar createToolbar() {
        JToolBar spreadsheetToolbar = new JToolBar();
        return spreadsheetToolbar;
    }

    protected JTabbedPane createTabbbedPane() {
        JTabbedPane tablesComponent = new JTabbedPane(JTabbedPane.BOTTOM);
        return tablesComponent;
    }

    private void loadDocument() {
        File h2File = FileUtil.toFile(h2DataObject.getPrimaryFile());
        try {
            Class.forName("org.h2.Driver");
            String filePath = h2File.getAbsolutePath().replace('\\', '/');
            if (filePath.endsWith(".h2.db")) {
                filePath = filePath.substring(0, filePath.length() - 6);
            }
            dbConnection = DriverManager.getConnection("jdbc:h2:" + filePath, "sa", "");
            ResultSet rsTableNames = dbConnection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
            while (rsTableNames.next()) {
                String nextTableName = rsTableNames.getString("TABLE_NAME");

                TableComponent tableComp = new TableComponent(dbConnection, nextTableName);

                String tabLabel = OfficeUIUtils.toDisplayable(nextTableName);
                tables.addTab(tabLabel, tableComp);
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public boolean canClose() {
        int answer = OfficeUIUtils.checkSaveBeforeClosing(h2DataObject, this);
        boolean canClose = answer == JOptionPane.YES_OPTION || answer == JOptionPane.NO_OPTION;
        return canClose;
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

    @Override
    protected CloneableTopComponent createClonedObject() {
        return new JDBCTopComponent(h2DataObject);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        super.writeExternal(out);
        out.writeObject(h2DataObject);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        super.readExternal(in);
        H2DataObject storedH2DataObject = (H2DataObject) in.readObject();
        init(storedH2DataObject);
    }
}
