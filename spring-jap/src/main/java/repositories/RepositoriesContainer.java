package repositories;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public class RepositoriesContainer {


    private LineRepository lineRepository;
    private ReservationRepository reservationRepository;

    @Autowired
    public RepositoriesContainer(LineRepository lineRepo, ReservationRepository reservationRepository) {
        this.lineRepository = lineRepo;
        this.reservationRepository = reservationRepository;
    }



}
