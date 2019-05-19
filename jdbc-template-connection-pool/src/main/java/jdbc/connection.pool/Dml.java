package jdbc.connection.pool;

import com.microsoft.sqlserver.jdbc.SQLServerBulkCSVFileRecord;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopy;
import com.microsoft.sqlserver.jdbc.SQLServerBulkCopyOptions;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jdbc.connection.pool.TablA;
import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
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
        List<TablA> catalogs = new ArrayList<>();
        // Example for DBCP DataSource
        try (BasicDataSource ds = new BasicDataSource()) {
            ds.setDriverClassName(Application.DRIVER_CLASS_NAME);
            ds.setUrl(Application.DB_URL);
            ds.setInitialSize(Application.CONN_POOL_SIZE);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);

            catalogs = jdbcTemplate.query(queryBuilder.toString(), new TablA[]{}, (rs, x2) ->
            {
                TablA tablA = new TablA();
                tablA.setId(rs.getInt("id"));
                tablA.setName(rs.getString("name"));
                tablA.setToday(rs.getDate("creationDateTime"));
                return tablA;
            });
        } catch (SQLException e) {
            System.out.println("EXCEPTION !!!!!!" + e);
        }
        return catalogs;
    }

    public ResultSet getChilds(Integer parentId) throws SQLException {
        String title = "parentId,childId,status,creationDate\n";
        ResultSet resultSet;
        JdbcTemplate jdbcTemplate = Application.getJdbcTemplate();
        String sql = "SELECT * FROM Child WHERE parentId=? ";
        // Example for DBCP DataSource
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(Application.DRIVER_CLASS_NAME);
        config.setJdbcUrl(Application.DB_URL);
        config.setPoolName("RhHikariCp");
        config.setMaximumPoolSize(Application.CONN_POOL_SIZE);
        try (HikariDataSource ds = new HikariDataSource(config);
             java.sql.Connection connection = jdbcTemplate.getDataSource().getConnection();
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

}
