package jdbc.tempalate;

import com.microsoft.sqlserver.jdbc.SQLServerBulkCSVFileRecord;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dml {
    public List<TablA> generateQuery()  {

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(" SELECT * FROM (\n" +
                "SELECT 1 id, 'A' name, GETUTCDATE() creationDateTime\n" +
                "UNION ALL\n" +
                "SELECT 2 , 'B', GETUTCDATE()\n" +
                "UNION ALL\n" +
                "SELECT 3 , 'C', GETUTCDATE()) A   ");

        JdbcTemplate jdbcTemplate = Application.getJdbcTemplate();


        List<TablA> catalogs = jdbcTemplate.query(queryBuilder.toString(), new TablA[]{}, (rs, x2) ->
        {
            TablA tablA = new TablA();
            tablA.setId(rs.getInt("id"));
            tablA.setName(rs.getString("name"));
            tablA.setToday(rs.getDate("creationDateTime"));
            return tablA;
        });
        return catalogs;
    }

    public ResultSet getChilds(Integer parentId) throws SQLException {
        String title = "parentId,childId,status,creationDate\n";
        ResultSet resultSet;
        JdbcTemplate jdbcTemplate = Application.getJdbcTemplate();
        String sql = "SELECT * FROM Child WHERE parentId=? ";

        try (java.sql.Connection connection = jdbcTemplate.getDataSource().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, parentId);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println(getPrepareDataFromResultSetToStream(resultSet));
            }
        }
        return resultSet;
    }

    private String getPrepareDataFromResultSetToStream(ResultSet resultSet) throws SQLException {
        String result = "";
        result += resultSet.getInt("parentId") + ",";
        result += resultSet.getInt("childId") + ",";
        result += StatusType.fromCode(resultSet.getInt("status")) + ",";
        result += resultSet.getTimestamp("creationDate")+"\n";
        return result;
    }

    public void bulkInsertFromCsv() throws SQLException, IOException {

        // We are going to build a CSV document to use for the bulk insert
        StringBuilder stringBuilder = new StringBuilder();

        // Add table column names to CSV
        stringBuilder.append("id, name\n");
        // Copy data from map and append to CSV
        for (int i=0; i < 100000; i++) {
            stringBuilder.append(String.format("%s,%s\n", i, "John Smith"));
        }

        PrintWriter pw = new PrintWriter(new File("test.csv"));
        pw.write(stringBuilder.toString());
        pw.close();
        try (// Pass in input stream and set column information
             SQLServerBulkCSVFileRecord fileRecord = new SQLServerBulkCSVFileRecord(
                     "test.csv", StandardCharsets.UTF_8.name(), ",", true)) {

            fileRecord.addColumnMetadata(1, "id", Types.INTEGER, 0, 0);
            fileRecord.addColumnMetadata(2, "name", Types.VARCHAR, 0, 0);

            JdbcTemplate jdbcTemplate = Application.getJdbcTemplate();

            try (java.sql.Connection connection = jdbcTemplate.getDataSource().getConnection();) {

                // Set bulk insert options, for example here I am setting a batch size
                SQLServerBulkCopyOptions copyOptions = new SQLServerBulkCopyOptions();
                copyOptions.setBatchSize(10000);

                // Write the CSV document to the database table
                try (SQLServerBulkCopy bulkCopy = new SQLServerBulkCopy(connection)) {
                    bulkCopy.setBulkCopyOptions(copyOptions);
                    bulkCopy.setDestinationTableName("TestCsv");
                    bulkCopy.writeToServer(fileRecord);
                }
            }
        }
    }
}
