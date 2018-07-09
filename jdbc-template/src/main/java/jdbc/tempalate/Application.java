package jdbc.tempalate;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

class Application {

    public static void main(String[] args) throws SQLException {

        // create csv file
        final List<TablA> tablAS = generateQuery();
        // execute stored procedure with input table output table input and output parameters
        HandleSp handleSp = new HandleSp();
        handleSp.execSP(555,tablAS);
    }

    public static List<TablA> generateQuery()  {

        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(" SELECT * FROM (\n" +
                "SELECT 1 id, 'A' name, GETUTCDATE() creationDateTime\n" +
                "UNION ALL\n" +
                "SELECT 2 , 'B', GETUTCDATE()\n" +
                "UNION ALL\n" +
                "SELECT 3 , 'C', GETUTCDATE()) A   ");

        JdbcTemplate jdbcTemplate = getJdbcTemplate();


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

    public static JdbcTemplate getJdbcTemplate()  {
        SQLServerDataSource ds = new SQLServerDataSource();
        //ds.setInstanceName("RED14\SQL2012_DEV");
        ds.setServerName("localhost");
        ds.setDatabaseName("idatest");
        ds.setIntegratedSecurity(true);

        return new JdbcTemplate(ds);
    }
}