package repositories;

import enteties.MyBenchmark;
import enums.LineStatus;
import m.Main;
import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
//@State(Scope.Benchmark)
public class MyBenchmarkRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Setup
    public void init() {
        Main.autowireBean(this);
        Main.autowireBean(entityManager);
        Main.autowireBean(platformTransactionManager);
    }

    public Optional<MyBenchmark> getLastMyBenchmark() {
        String sql = "SELECT t FROM MyBenchmark t ORDER BY t.id DESC";
        Query query = entityManager.createQuery(sql);
        query.setFirstResult(0);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }


    @Transactional
    public void updateStatusForLastMyBenchmark(LineStatus status) {
        Optional<MyBenchmark> lastLine = getLastMyBenchmark();
        lastLine.ifPresent(benchmark -> {
            benchmark.setStatus(status);
            updateMyBenchmark(benchmark);
        });
    }

    @Transactional
    public void updateMyBenchmark(MyBenchmark benchmark) {
        entityManager.merge(benchmark);
    }
    public List<MyBenchmark> getAllLines() {
        return entityManager.createQuery("FROM MyBenchmark", MyBenchmark.class)
                .getResultList();
    }

    public MyBenchmark getMyBenchmark(int id) {

        return entityManager.find(MyBenchmark.class, id);
    }


    @Transactional
    public MyBenchmark createNewMyBenchmark(MyBenchmark benchmark) {
        int newId = getLastId();
        benchmark.setId(newId+1);
        entityManager.persist(benchmark);
        return benchmark;
    }

    private int getLastId() {
        Optional<MyBenchmark> lastMyBenchmark = getLastMyBenchmark();
        int newId;
        if (lastMyBenchmark.isPresent())
            newId = lastMyBenchmark.get().getId();
        else
            newId =0;
        return newId;
    }


 //   @Benchmark @BenchmarkMode(Mode.AverageTime)
    @Transactional
//    @Benchmark
//    @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.MINUTES)
//    @Fork(0)
//    @Warmup(iterations = 0) 		// Warmup Iteration = 3
//    @Measurement(iterations = 8)
        //@Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.MINUTES)
       public void createXMyBenchmarks() {
 //       Session unwrap = entityManager.unwrap(Session.class);
 //       unwrap.setJdbcBatchSize(10);
        int lastId = 3;//getLastId();
        for (int i = 0; i < 200; i++) {
            MyBenchmark myBenchmark = new MyBenchmark(lastId +i+ 1, LineStatus.INQUEUE, "PEGOT" , "BLUE");
            entityManager.persist(myBenchmark);
//            entityManager.flush();
//            entityManager.clear();
        }
    }

    public Integer getNewId(Integer id) {
        String sql = "SELECT MAX(j.id)+1 FROM MyBenchmark j WHERE j.id=:Id";
        Query query = entityManager.createQuery(sql);
        query.setParameter("Id", id);
        Object queryResult = query.getSingleResult();
        return queryResult == null ? 1 : (Integer) queryResult;
    }

    public List<MyBenchmark> getMyBenchmarks(int pageSize, int pageNumber) {
        String sql = "FROM MyBenchmark j ORDER BY j.id";
        Query query = entityManager.createQuery(sql, MyBenchmark.class);
        query.setFirstResult((pageNumber -1)*pageSize);
        query.setMaxResults(pageSize);

        final List<MyBenchmark> resultList = query.getResultList();
       return resultList;
    }

    @Transactional
    public void deleteLastMyBenchmark() {
        getLastMyBenchmark().ifPresent(myBenchmark -> {
            entityManager.remove(myBenchmark);
        });
    }


}
