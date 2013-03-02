package org.joeffice.database.tablemodel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.joeffice.desktop.ui.OfficeUIUtils;

import org.openide.util.Exceptions;

/**
 * TableModel representing one table of the database.
 *
 * @author Anthony Goubard - Japplis
 */
public class JDBCSheet extends DefaultTableModel {

    public final static String BINARY_DATA_LABEL = "<binary data...>"; // No I18N
    private Connection conn;
    private String tableName;
    private ResultSetMetaData columnsMetaData;

    public JDBCSheet(Connection conn, String tableName) {
        this.conn = conn;
        this.tableName = tableName;
        init();
    }

    private void init() {
        fillWithQuery("select * from " + tableName);
    }

    private void fillWithQuery(String query) {
        try {
            ResultSet tableData = conn.createStatement().executeQuery(query);

            columnsMetaData = tableData.getMetaData();

            // TODO not go through the whole table
            while (tableData.next()) {
                Object[] rowValues = new Object[columnsMetaData.getColumnCount()];
                for (int columnIndex = 0; columnIndex < columnsMetaData.getColumnCount(); columnIndex++) {
                    Object value = getResultSetValueAsObject(tableData, columnIndex + 1);
                    rowValues[columnIndex] = value;
                }
                addRow(rowValues);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Object getResultSetValueAsObject(ResultSet tableData, int columnIndex) throws SQLException {
        int columnType = columnsMetaData.getColumnType(columnIndex);
        Object dataValue = null;
        switch (columnType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                dataValue = tableData.getString(columnIndex);
                break;
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.NUMERIC:
            case Types.ROWID:
            case Types.SMALLINT:
            case Types.TINYINT:
                dataValue = tableData.getInt(columnIndex);
                break;
            case Types.DECIMAL:
            case Types.DOUBLE:
                dataValue = tableData.getDouble(columnIndex);
                break;
            case Types.FLOAT:
                dataValue = tableData.getFloat(columnIndex);
                break;
            case Types.TIME:
                dataValue = tableData.getTime(columnIndex);
                break;
            case Types.TIMESTAMP:
                dataValue = tableData.getTimestamp(columnIndex);
                break;
            case Types.BLOB:
            case Types.CLOB:
            case Types.JAVA_OBJECT:
            case Types.NCLOB:
            case Types.VARBINARY:
                dataValue = BINARY_DATA_LABEL; // Do not read
                break;
        }
        return dataValue;
    }

    @Override
    public int getColumnCount() {
        try {
            return columnsMetaData.getColumnCount();
        } catch (SQLException ex) {
            return 0;
        }
    }

    @Override
    public String getColumnName(int column) {
        try {
            String columnName = columnsMetaData.getColumnName(column + 1);
            String displayedName = OfficeUIUtils.toDisplayable(columnName);
            return displayedName;
        } catch (SQLException ex) {
            return super.getColumnName(column);
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        boolean editable = false;
        try {
            editable = columnsMetaData.isWritable(column + 1)
                    && columnsMetaData.getColumnType(column + 1) != Types.ROWID;
        } catch (SQLException ex) {
            // Not editable
        }
        return editable;
    }

    @Override
    public void setValueAt(Object newValue, int row, int column) {
        try {
            int maxSize = columnsMetaData.getPrecision(column + 1);
            if (newValue instanceof String && newValue.toString().length() > maxSize) {
                JOptionPane.showMessageDialog(null, "The text should be no longer than " + maxSize + " characters"); // NO I18N
                return;
            }
        } catch (SQLException ex) {
        }
        super.setValueAt(newValue, row, column);
        // TODO Also update the database
        updateDatabase(newValue, row, column);
    }

    private void updateDatabase(Object newValue, int row, int column) {
        try {
            String columnName = columnsMetaData.getColumnName(column + 1);
            String query = "update " + tableName + " set " + columnName + " = ?";
            String whereClause = findRowCriteria(column, row);
            query += whereClause;
            PreparedStatement updateStmt = conn.prepareStatement(query);
            setStatementValue(updateStmt, column, newValue);
            int updatedRowsCount = updateStmt.executeUpdate();
            System.out.println(updatedRowsCount + " rows updated.");
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    public String findRowCriteria(int updatedColumn, int row) throws SQLException {
        String whereClause = "";
        String conjunction = " where ";
        for (int i = 0; i < columnsMetaData.getColumnCount(); i++) {
            int columnType = columnsMetaData.getColumnType(i + 1);
            switch (columnType) {
                case Types.CHAR:
                case Types.VARCHAR:
                case Types.LONGVARCHAR:
                case Types.NVARCHAR:
                case Types.LONGNVARCHAR:
                    String columnValue = (String) getValueAt(row, i);
                    if (i != updatedColumn && columnValue != null && "".equals(columnValue)) {
                        whereClause += conjunction + columnsMetaData.getColumnName(i + 1) + " = '" + columnValue + "'";
                        conjunction = " and ";
                    }
                    break;
                case Types.BIGINT:
                case Types.INTEGER:
                case Types.NUMERIC:
                case Types.ROWID:
                case Types.SMALLINT:
                case Types.TINYINT:
                    Integer columnIntValue = (Integer) getValueAt(row, i);
                    if (i != updatedColumn && columnIntValue != null) {
                        whereClause += conjunction + columnsMetaData.getColumnName(i + 1) + " = " + columnIntValue;
                        conjunction = " and ";
                    }
                    break;
            }
        }
        return whereClause;
    }

    public void setStatementValue(PreparedStatement updateStmt, int columnIndex, Object value) throws SQLException {
        int columnType = columnsMetaData.getColumnType(columnIndex + 1);
        switch (columnType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                updateStmt.setString(1, (String) value);
                break;
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.NUMERIC:
            case Types.ROWID:
            case Types.SMALLINT:
            case Types.TINYINT:
                updateStmt.setInt(1, Integer.parseInt((String) value));
                break;
        }
    }
}
