package repositories;


import enteties.Reservation;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class ReservationRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public boolean isReservationExist(int reservationId) {
        return getReservation(reservationId) != null;
    }


    public Reservation getReservation(int id) {
        return entityManager.find(Reservation.class, id);
    }


    @Transactional
    public void createReservation(Reservation reservation) {
        entityManager.persist(reservation);
    }


}
