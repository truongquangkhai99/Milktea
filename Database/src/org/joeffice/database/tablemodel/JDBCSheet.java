package org.joeffice.database.tablemodel;

import com.sun.rowset.JdbcRowSetImpl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import javax.sql.RowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import org.joeffice.desktop.ui.OfficeUIUtils;

import org.openide.util.Exceptions;

/**
 * TableModel representing one table of the database.
 *
 * @author Anthony Goubard - Japplis
 */
public class JDBCSheet extends AbstractTableModel {

    public final static String BINARY_DATA_LABEL = "<binary data...>"; // No I18N
    private Connection conn;
    private String tableName;
    private ResultSetMetaData columnsMetaData;
    private RowSet dataModel;

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
            RowSetFactory rowSetFactory = RowSetProvider.newFactory();
            dataModel = rowSetFactory.createJdbcRowSet();
            dataModel.setUrl(conn.getMetaData().getURL());
            dataModel.setUsername(conn.getMetaData().getUserName());
            dataModel.setPassword("");

            dataModel.setCommand(query);
            dataModel.execute();

            columnsMetaData = dataModel.getMetaData();

            // TODO not go through the whole table

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
    public int getRowCount() {
        try {
            dataModel.last();
            return dataModel.getRow();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try {
            dataModel.absolute(rowIndex + 1);
            Object value = getResultSetValueAsObject(dataModel, columnIndex + 1);
            return value;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "";
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
        updateDatabase(newValue, row, column);
    }

    private void updateDatabase(Object newValue, int row, int column) {
        try {
            dataModel.absolute(row + 1);
            setColumnValue(dataModel, column + 1, newValue);
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    // XXX This doesn't update the data: http://docs.oracle.com/javase/tutorial/jdbc/basics/jdbcrowset.html
    // and throws an exception for column not null (which is not edited)
    public void setColumnValue(RowSet rows, int columnIndex, Object value) throws SQLException {
        int columnType = columnsMetaData.getColumnType(columnIndex);
        switch (columnType) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                rows.updateString(1, (String) value);
                break;
            case Types.BIGINT:
            case Types.INTEGER:
            case Types.NUMERIC:
            case Types.ROWID:
            case Types.SMALLINT:
            case Types.TINYINT:
                rows.updateInt(1, Integer.parseInt((String) value));
                break;
        }
        rows.updateRow();
    }
}
