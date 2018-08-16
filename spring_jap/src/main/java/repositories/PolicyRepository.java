package repositories;


import enteties.Policy;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class PolicyRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public boolean isPolicyExist(int policyId) {
        return getPolicy(policyId) != null;
    }


    public Policy getPolicy(int id) {
        return entityManager.find(Policy.class, id);
    }


    @Transactional
    public void createPolicy(Policy policy) {
        entityManager.persist(policy);
    }


}
