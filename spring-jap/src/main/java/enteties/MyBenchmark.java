package enteties;

import enums.LineStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@DynamicUpdate
@Entity
@NoArgsConstructor
public class MyBenchmark  {
    @Id
    @Getter
    @Setter
    private int id;
    private int status;
    private String model;
    private String color;

    public LineStatus getStatus() {
        return LineStatus.getStatusByValue(status);
    }

    public void setStatus(LineStatus status) {
        this.status = status.getValue();
    }


    public MyBenchmark(int id, LineStatus status, String model, String color) {
        this.id = id;
        this.status = status.getValue();
        this.model = model;
        this.color = color;
    }


}
