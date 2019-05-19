package repositories;

import enteties.Line;
import enteties.Reservation;
import enums.LineStatus;
import org.hibernate.Session;
import org.openjdk.jmh.annotations.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository

public class LineRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Reservation reservation;

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
        return entityManager.createQuery("FROM Line", Line.class)
                .getResultList();
    }

    public Line getLine(int id) {

        return entityManager.find(Line.class, id);
    }


    @Transactional
    public Line createNewLine(Line line) {
        int newId = getNewId(line.getReservation().getReservationId());
        line.setLineId(newId);
        entityManager.persist(line);
        return line;
    }



    @Transactional
       public void createXLines() {
        int newId = 660;//getNewId(reservation.getReservationId());
 //       Session unwrap = entityManager.unwrap(Session.class);
 //       unwrap.setJdbcBatchSize(10);
        for (int i = 0; i < 200; i++) {
            Line line = new Line(reservation, LineStatus.INQUEUE, "PEGOT" , "BLUE");
            line.setLineId(newId++);
            entityManager.persist(line);
//            entityManager.flush();
//            entityManager.clear();
        }
    }

    public Integer getNewId(Integer reservationId) {
        String sql = "SELECT MAX(j.linekey.lineId)+1 FROM Line j WHERE j.linekey.reservation.reservationId=:reservationId";
        Query query = entityManager.createQuery(sql);
        query.setParameter("reservationId", reservationId);
        Object queryResult = query.getSingleResult();
        return queryResult == null ? 1 : (Integer) queryResult;
    }

    public List<Line> getLines(Reservation reservation, int pageSize, int pageNumber) {
        String sql = "FROM Line j WHERE j.linekey.reservation.reservationId = :reservationId ORDER BY j.linekey.lineId";
        Query query = entityManager.createQuery(sql, Line.class);
        query.setParameter("reservationId", reservation.getReservationId());
        query.setFirstResult((pageNumber -1)*pageSize);
        query.setMaxResults(pageSize);

        final List<Line> resultList = query.getResultList();
       return resultList;
    }

    @Transactional
    public void deleteLastLine() {
        getLastLine().ifPresent(line -> {
            entityManager.remove(line);
        });
    }


}
