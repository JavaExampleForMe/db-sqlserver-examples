import config.ConfigurationDemo;
import enteties.Line;
import enteties.Policy;
import enums.LineStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Import;
import repositories.DeletionRepositoriesContainer;

import java.util.List;

@Import({ConfigurationDemo.class})
public class Main {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(ConfigurationDemo.class);

        DeletionRepositoriesContainer deletionRepositoriesContainer = context.getBean(DeletionRepositoriesContainer.class);

        Policy policy1 = new Policy(1,1);

        deletionRepositoriesContainer.getPolicyRepository().createPolicy(policy1);

        Policy policy = deletionRepositoriesContainer.getPolicyRepository().getPolicy(1);

        Line line1 = new Line(policy, LineStatus.INQUEUE);
        deletionRepositoriesContainer.getLineRepository().createNewLine(line1);
        deletionRepositoriesContainer.getLineRepository().updateStatusForLastLine(LineStatus.COMPLETED);
        List<Line> lines = deletionRepositoriesContainer.getLineRepository().getLines(1, 100, 1);


        System.out.println("test");

    }


}
