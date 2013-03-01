package org.joeffice.tools;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates an empty h2 database.
 *
 * C:\Java\projects\Joeffice\tools\src>javac -classpath C:\Java\projects\Joeffice\admin\libs\h2\bin\h2-1.3.170.jar org\joeffice\tools\CreateH2Database.java
 * java -cp .;C:\Java\projects\Joeffice\admin\libs\h2\bin\h2-1.3.170.jar org.joeffice.tools.CreateH2Database
 *
 * @author Anthony Goubard - Japplis
 */
public class CreateH2Database {

    public CreateH2Database() throws SQLException, ClassNotFoundException {
        Class.forName("org.h2.Driver");
        createTest1();
        createTest2();
    }

    public void createTest1() throws SQLException {
        if (!new File("C:/Java/projects/Joeffice/tools/tests/test1.h2.db").exists()) {
            Connection conn = DriverManager.getConnection("jdbc:h2:C:/Java/projects/Joeffice/tools/tests/test1", "sa", "");
            conn.close();
        }
    }

    public void createTest2() throws SQLException {
        if (!new File("C:/Java/projects/Joeffice/tools/tests/test2.h2.db").exists()) {
            Connection conn = DriverManager.getConnection("jdbc:h2:C:/Java/projects/Joeffice/tools/tests/test2", "sa", "");
            PreparedStatement createTable = conn.prepareStatement("CREATE TABLE TEST_TABLE(ID INT PRIMARY KEY, NAME VARCHAR);");
            createTable.execute();
            createTable.close();
            PreparedStatement insertValue = conn.prepareStatement("INSERT INTO TEST_TABLE VALUES(1, 'Hello World');");
            insertValue.execute();
            insertValue.close();
            conn.close();
        }
    }

    public final static void main(String[] args) throws SQLException, ClassNotFoundException {
        new CreateH2Database();
    }
}