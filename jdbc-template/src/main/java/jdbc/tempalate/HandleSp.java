package jdbc.tempalate;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerDataTable;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Types;
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
             SQLServerCallableStatement sqlCallableStatement = (SQLServerCallableStatement) connection.prepareCall("EXEC [dbo].[spToTest] ?,?,?")) {
            SQLServerDataTable sqlServerDataTableParamInputTable = getDataTable(rowsIn);

            sqlCallableStatement.setStructured(1, "[dbo].[tblAType]", sqlServerDataTableParamInputTable);
            sqlCallableStatement.setInt(2, input);
            sqlCallableStatement.registerOutParameter(3, Types.INTEGER);
            sqlCallableStatement.execute();
            try(ResultSet resultSet = sqlCallableStatement.getResultSet()) {

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
