package com.example.spring.jpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringJpaApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SpringJpaApplication.class, args);
        MyDao dao = ctx.getBean(MyDao.class);
        Person p = new Person();
        p.setName("ABC");
        p.setAge(33);
        long pk = dao.addPerson(p);

        Person fromDB = dao.getPerson(pk);

    }

}
