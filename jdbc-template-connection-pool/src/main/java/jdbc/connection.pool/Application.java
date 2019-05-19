package jdbc.connection.pool;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jdbc.connection.pool.Dml;
import jdbc.connection.pool.HandleSp;
import jdbc.connection.pool.TablA;
import org.apache.commons.dbcp2.BasicDataSource;
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

    public static final int CONN_POOL_SIZE = 5;

    public static final String DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static final String SERVER_NAME = "localhost";
    public static final String DATABASE_NAME = "idatest";
    public static final String DB_URL = "jdbc:sqlserver://"+SERVER_NAME+";database="+DATABASE_NAME+";integratedSecurity=true";
    public static JdbcTemplate getJdbcTemplate()  {
        // TODO Datasource needs to be closed
        BasicDataSource ds = new BasicDataSource();

//        SQLServerDataSource ds = new SQLServerDataSource();
        //ds.setInstanceName("RED14\SQL2012_DEV");
        ds.setDriverClassName(DRIVER_CLASS_NAME);
        ds.setUrl(DB_URL);
        ds.setInitialSize(CONN_POOL_SIZE);
        return new JdbcTemplate(ds);
    }

    public static JdbcTemplate getJdbcTemplateHikari()  {
        // TODO Datasource needs to be closed
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(DRIVER_CLASS_NAME);
        config.setJdbcUrl(DB_URL);
        config.setPoolName("RhHikariCp");
        config.setMaximumPoolSize(CONN_POOL_SIZE);
        HikariDataSource ds = new HikariDataSource(config);
        return new JdbcTemplate(ds);
    }
}