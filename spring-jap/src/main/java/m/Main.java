package m;

import config.ConfigurationDemo;
import enteties.Line;
import enteties.MyBenchmark;
import enteties.Reservation;
import enums.LineStatus;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import repositories.RepositoriesContainer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Import({ConfigurationDemo.class})
public class Main {

    private static ApplicationContext context;
    public static void main(String[] args) throws IOException, RunnerException {

        context = new AnnotationConfigApplicationContext(ConfigurationDemo.class);

        RepositoriesContainer repositoriesContainer = context.getBean(RepositoriesContainer.class);
 //       org.openjdk.jmh.Main.main(args);
        Reservation reservation1 = new Reservation(1,1);
//
//
        repositoriesContainer.getReservationRepository().createReservation(reservation1);
//
        Reservation reservation = repositoriesContainer.getReservationRepository().getReservation(1);
//
        repositoriesContainer.getLineRepository().reservation = reservation;
        Line line = new Line( reservation, LineStatus.INQUEUE, "PEGEOT", "RED");
        line.setLineId(1);
        repositoriesContainer.getLineRepository().createNewLine(line);
       MyBenchmark myBenchmark = new MyBenchmark(2, LineStatus.INQUEUE, "PEGOT", "BLUE");

        repositoriesContainer.getMyBenchmarkRepository().createNewMyBenchmark(myBenchmark);
//        repositoriesContainer.getMyBenchmarkRepository().createXMyBenchmarks();
             repositoriesContainer.getLineRepository().updateStatusForLastLine(LineStatus.COMPLETED);
              List<Line> lines = repositoriesContainer.getLineRepository().getLines(reservation, 100, 1);
    //    System.out.println("test");


    }
    public static void autowireBean(Object bean) {
        AutowireCapableBeanFactory factory = context.getAutowireCapableBeanFactory();
        factory.autowireBean(bean);
    }

}
