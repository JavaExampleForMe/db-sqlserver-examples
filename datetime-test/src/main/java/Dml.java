import enteties.DatesTbl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public class Dml {

    public void getDatesTbl() {
        JdbcTemplate jdbcTemplate = Main.getJdbcTemplate();
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate.getDataSource());
        LocalDateTime aDateTime = LocalDateTime.of(2002, Month.FEBRUARY, 2, 2, 2, 2,89100000);
        String sql = "SELECT * FROM DatesTbl WHERE dtGMTStartTime=:startTime";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource()
                .addValue("startTime", aDateTime, Types.TIMESTAMP);
        List<DatesTbl> datesTbls = namedParameterJdbcTemplate.queryForList(sql, mapSqlParameterSource, DatesTbl.class);

    }


}
