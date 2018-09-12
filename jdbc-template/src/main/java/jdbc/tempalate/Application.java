package jdbc.tempalate;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

class Application {

    public static void main(String[] args) throws SQLException {

        Dml dml = new Dml();
        // create csv file
        final List<TablA> tablAS = dml.generateQuery();
        final ResultSet childs = dml.getChilds(1);

        // execute stored procedure with input table output table input and output parameters
        HandleSp handleSp = new HandleSp();
        handleSp.execSP(555,tablAS);
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