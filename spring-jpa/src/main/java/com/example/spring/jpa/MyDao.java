package com.example.spring.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
public class MyDao {

    @Autowired
    private EntityManager em;

    @Transactional
    public  long addPerson(Person p){
        em.persist(p);
        return p.getId();
    }

    @Transactional
    public Person getPerson(long id) {
        return em.find(Person.class, id);
    }
}
