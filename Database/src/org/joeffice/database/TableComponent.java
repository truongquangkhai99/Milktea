package org.joeffice.database;

import java.awt.BorderLayout;
import java.sql.Connection;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.joeffice.database.tablemodel.JDBCSheet;

/**
 * Component to display one of the database table.
 *
 * @author Anthony Goubard - Japplis
 */
public class TableComponent extends JPanel {

    public TableComponent(Connection conn, String tableName) {
        setLayout(new BorderLayout());
        JDBCSheet sheet = new JDBCSheet(conn, tableName);
        JTable testTable = new JTable(sheet);
        add(new JScrollPane(testTable));
    }
}
