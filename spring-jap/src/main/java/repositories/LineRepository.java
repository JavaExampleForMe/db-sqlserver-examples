package repositories;

import enteties.Line;
import enums.LineStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class LineRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataSource dataSource;

    public Optional<Line> getLastLine() {
        String sql = "SELECT t FROM Line t ORDER BY t.creationDate DESC";
        Query query = entityManager.createQuery(sql);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }


    @Transactional
    public void updateStatusForLastLine(LineStatus status) {
        Optional<Line> lastLine = getLastLine();
        lastLine.ifPresent(line -> {
//            line.setModifyDate(new Date());
            line.setStatus(status);
            updateLine(line);
        });
    }

    @Transactional
    public void updateLine(Line line) {
        entityManager.merge(line);
    }


    public List<Line> getAllLines() {
        return entityManager.createQuery("FROM Job", Line.class)
                .getResultList();
    }

    public Line getLine(int id) {

        return entityManager.find(Line.class, id);
    }


    @Transactional
    public Line createNewLine(Line line) {
        int newId = getNewId(line.getPolicy().getPolicyId());
        line.setLineId(newId);
        entityManager.persist(line);
        return line;
    }


    public Integer getNewId(Integer policyId) {
        String sql = "SELECT MAX(j.linekey.lineId)+1 FROM Line j WHERE j.linekey.policy.policyId=:policyId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("policyId", policyId);
        Object queryResult = query.getSingleResult();
        return queryResult == null ? 1 : (Integer) queryResult;
    }

    public List<Line> getLines(Integer policyId, int pageSize, int pageNumber) {
        // If we do not do the join Hiberhet does a cross join
        //String sql = "SELECT j FROM Line j WHERE j.linekey.policy.policyId=:policyId ";
        String sql = "FROM Line j INNER JOIN j.linekey.policy WHERE j.linekey.policy.policyId=:policyId ORDER BY j.linekey.lineId";
        Query query = entityManager.createQuery(sql, Line.class);
        query.setParameter("policyId", policyId);
        query.setFirstResult((pageNumber -1)*pageSize);
        query.setMaxResults(pageSize);
        List<Line> queryResult = query.getResultList();
        return queryResult;
    }


    @Transactional
    public void deleteLastLine() {
        getLastLine().ifPresent(line -> {
            entityManager.remove(line);
        });
    }


}
