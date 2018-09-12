package jdbc.tempalate;

import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
}
