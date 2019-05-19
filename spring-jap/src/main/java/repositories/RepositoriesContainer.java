package repositories;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public class RepositoriesContainer {


    private LineRepository lineRepository;
    private ReservationRepository reservationRepository;
    private MyBenchmarkRepository myBenchmarkRepository;

    @Autowired
    public RepositoriesContainer(LineRepository lineRepo, ReservationRepository reservationRepository, MyBenchmarkRepository myBenchmarkRepository) {
        this.lineRepository = lineRepo;
        this.reservationRepository = reservationRepository;
        this.myBenchmarkRepository = myBenchmarkRepository;

    }



}
