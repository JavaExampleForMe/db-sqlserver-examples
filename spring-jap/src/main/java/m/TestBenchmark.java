package m;



import org.openjdk.jmh.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import repositories.RepositoriesContainer;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Component
public class TestBenchmark {

    @Autowired
    RepositoriesContainer service;

    @Setup
    public void init() {
        Main.autowireBean(this);
    }


//    @Benchmark
//    @Fork(0)
   public void test1() {
        service.getMyBenchmarkRepository().createXMyBenchmarks();
    }

}
