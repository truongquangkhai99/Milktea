package org.joeffice.database;

import static javax.swing.ScrollPaneConstants.UPPER_LEFT_CORNER;

import java.awt.BorderLayout;
import java.sql.Connection;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.joeffice.database.tablemodel.JDBCSheet;
import org.joeffice.spreadsheet.rows.JScrollPaneAdjuster;
import org.joeffice.spreadsheet.rows.JTableRowHeaderResizer;
import org.joeffice.spreadsheet.rows.RowTable;

import org.netbeans.swing.etable.ETable;

/**
 * Component to display one of the database table.
 *
 * @author Anthony Goubard - Japplis
 */
public class TableComponent extends JPanel {

    public TableComponent(Connection conn, String tableName) {
        setLayout(new BorderLayout());
        JDBCSheet sheet = new JDBCSheet(conn, tableName); // Table model
        JTable databaseTable = new ETable(sheet);

        // TODO move this code to the rows spreadsheet package
        JScrollPane scrolling = new JScrollPane(databaseTable);
        JTable rowHeaders = new RowTable(databaseTable);
        scrolling.setRowHeaderView(rowHeaders);
        scrolling.setCorner(UPPER_LEFT_CORNER, rowHeaders.getTableHeader());
        new JTableRowHeaderResizer(scrolling).setEnabled(true);
        new JScrollPaneAdjuster(scrolling);

        add(scrolling);
    }
}
