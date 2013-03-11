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
import org.netbeans.swing.etable.ETableTransferHandler;

/**
 * Component to display one of the database table.
 *
 * @author Anthony Goubard - Japplis
 */
public class TableComponent extends JPanel {

    public TableComponent(Connection conn, String tableName) {
        setLayout(new BorderLayout());
        JDBCSheet sheet = new JDBCSheet(conn, tableName); // Table model
        ETable databaseTable = new ETable(sheet);

        // TODO move this code to the rows spreadsheet package
        JScrollPane scrolling = new JScrollPane(databaseTable);
        JTable rowHeaders = new RowTable(databaseTable);
        scrolling.setRowHeaderView(rowHeaders);
        scrolling.setCorner(UPPER_LEFT_CORNER, rowHeaders.getTableHeader());
        new JTableRowHeaderResizer(scrolling).setEnabled(true);
        new JScrollPaneAdjuster(scrolling);

        databaseTable.setColumnHidingAllowed(true);
        databaseTable.setTransferHandler(new ETableTransferHandler());
        databaseTable.setDragEnabled(true); // Dragging not working yet

        add(scrolling);
    }
}
