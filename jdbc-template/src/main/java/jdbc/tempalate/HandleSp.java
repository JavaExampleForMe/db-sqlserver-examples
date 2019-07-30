package jdbc.tempalate;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class HandleSp {
    String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";
    public int execSP(int input, List<TablA> rowsIn) throws SQLException {
        int actualoutput = -1;
        List<TablA> rowsout = new ArrayList<>();
        System.out.println("Executing stored procedure. example for input/output params and return select statement");
        System.out.println("Input table type contains : " + Arrays.toString(rowsIn.toArray()));

        JdbcTemplate jdbcTemplate = Application.getJdbcTemplate();

        try (java.sql.Connection connection = jdbcTemplate.getDataSource().getConnection();
             // Option 1 - preferred
             SQLServerCallableStatement sqlCallableStatement = (SQLServerCallableStatement) connection.prepareCall("EXEC [dbo].[spToTest] ?,?,?");
             // Option 2 - Needed when we have the dependency commons-dbcp:commons-dbcp:1.4 in classpath
//            CallableStatement callableStatement = connection.prepareCall("EXEC [dbo].[spToTest] ?,?,?");
//            DelegatingPreparedStatement dstmt = (DelegatingPreparedStatement) callableStatement;
//            SQLServerCallableStatement sqlServerCallableStatement = (SQLServerCallableStatement) dstmt.getInnermostDelegate();
        )
        {

            SQLServerDataTable sqlServerDataTableParamInputTable = getDataTable(rowsIn);

            sqlCallableStatement.setStructured(1, "[dbo].[tblAType]", sqlServerDataTableParamInputTable);
            sqlCallableStatement.setInt(2, input);
            sqlCallableStatement.registerOutParameter(3, Types.INTEGER);
            ResultSet resultSet = null;
            try {
                sqlCallableStatement.execute();
                resultSet = findResultSet(sqlCallableStatement);

                while (resultSet!= null && resultSet.next()) {
                    rowsout.add( new TablA( resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDate("creationDateTime")));
                }
                // if we pass this loop without exceptions, then the store procedure finished without errors. and we will get the procedure printings
                while (!(sqlCallableStatement.getMoreResults() == false && sqlCallableStatement.getUpdateCount() == -1));

            } catch (SQLException sqlException) {
                printStoredProcedurePrintings(sqlCallableStatement);
                // In this case we failed while executing sp.
                System.out.println("Failed to delete from the required tables in stored procedure - SQLErrorCode = " + sqlException.getErrorCode() + " ex =" + sqlException.getMessage());
                throw sqlException;
            }
            finally {
                if (!resultSet.isClosed()) {
                    resultSet.close();
                }
            }
            printStoredProcedurePrintings(sqlCallableStatement);
            actualoutput = sqlCallableStatement.getInt(3);
            System.out.println("output parameter from stored procedure " +actualoutput );
            System.out.println("output select results : " + Arrays.toString(rowsout.toArray()));
        } catch (SQLException e) {
            System.out.println("Failed to delete from the required tables in stored procedure - SQLErrorCode = " + e.getErrorCode() + " ex =" + e.getMessage());
            throw e;

        }
        return actualoutput;
    }

    /***
     *The result of the stored procedure holds empty/irrelevant ResultSets (this is an known issue).
     * The method finds the relevant ResultSet.
     * Took an example from:
     * https://programmaticponderings.com/2012/08/24/calling-sql-server-stored-procedures-with-java-using-jdbc/
     * */
    private ResultSet findResultSet(SQLServerCallableStatement sqlCallableStatement) throws SQLServerException {
        int rowsAffected = 0;
        ResultSet resultSet = null;
        boolean resultSetExists = sqlCallableStatement.getResultSet() != null;

        // Protects against lack of SET NOCOUNT in stored procedure
        while (resultSetExists || rowsAffected != -1) {
            if (resultSetExists) {
                resultSet = sqlCallableStatement.getResultSet();
                break;
            } else {
                rowsAffected = sqlCallableStatement.getUpdateCount();
            }
            resultSetExists = sqlCallableStatement.getMoreResults();
        }
        return resultSet;
    }


    private void printStoredProcedurePrintings(SQLServerCallableStatement sqlCallableStatement) {
        // Print the debug information from the stored procedure
        try {
            SQLWarning warnings = sqlCallableStatement.getWarnings();
            while (warnings != null) {
                System.out.println("[dbo].[spToTest] =>" + warnings.getMessage());
                warnings = warnings.getNextWarning();
            }
        } catch (SQLServerException e) {
            System.out.println("Failed to get stored procedure printings ex =" + e.getMessage());
        }
    }


    private SQLServerDataTable getDataTable(List<TablA> rowsIn) throws SQLException {
        System.out.println("get Data Table. rows count = " + rowsIn.size());
        SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT, Locale.ENGLISH);


        SQLServerDataTable table = new SQLServerDataTable();
        table.addColumnMetadata("id", Types.BIGINT);
        table.addColumnMetadata("name", Types.NVARCHAR);
        table.addColumnMetadata("creationDateTime", Types.DATE);

        for (TablA currRow : rowsIn) {
            table.addRow(currRow.getId(), currRow.getName(), currRow.getToday());
        }
        return table;
    }

}
