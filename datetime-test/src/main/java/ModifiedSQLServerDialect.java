import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

public class ModifiedSQLServerDialect extends SQLServer2012Dialect {
    public ModifiedSQLServerDialect () {
        super();
        registerColumnType(Types.TIMESTAMP, "datetime");
        registerColumnType(Types.DATE, "datetime");
        registerColumnType(Types.TIME, "datetime");
        registerHibernateType(Types.TIMESTAMP, "datetime");
        registerHibernateType(Types.DATE, "datetime");
        registerHibernateType(Types.TIME, "datetime");
    }
}
