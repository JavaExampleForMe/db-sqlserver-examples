import config.ConfigurationDemo;
import enteties.Line;
import enteties.Reservation;
import enums.LineStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import repositories.RepositoriesContainer;

import java.util.List;

@Import({ConfigurationDemo.class})
public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationDemo.class);

        RepositoriesContainer repositoriesContainer = context.getBean(RepositoriesContainer.class);

        Reservation reservation1 = new Reservation(1,1);

        repositoriesContainer.getReservationRepository().createReservation(reservation1);

        Reservation reservation = repositoriesContainer.getReservationRepository().getReservation(1);

        Line line1 = new Line(reservation, LineStatus.INQUEUE, "PEGOT" , "BLUE");
        repositoriesContainer.getLineRepository().createNewLine(line1);
        repositoriesContainer.getLineRepository().updateStatusForLastLine(LineStatus.COMPLETED);
        List<Line> lines = repositoriesContainer.getLineRepository().getLines(reservation, 100, 1);

        System.out.println("test");

 
    }


}
