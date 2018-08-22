package repositories;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
@Getter
public class DeletionRepositoriesContainer {


    private LineRepository lineRepository;
    private PolicyRepository policyRepository;

    @Autowired
    public DeletionRepositoriesContainer(LineRepository lineRepo, PolicyRepository policyRepo) {
        this.lineRepository = lineRepo;
        this.policyRepository = policyRepo;
    }



}
