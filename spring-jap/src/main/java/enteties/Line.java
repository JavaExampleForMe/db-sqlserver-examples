package enteties;

import enums.LineStatus;
import lombok.*;
import lombok.experimental.Delegate;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@DynamicUpdate
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Line extends BaseEntity  {

    @EmbeddedId
    @Delegate
    private LineKey linekey;

    private int lineStatus;
    private LocalDateTime creationDate;
    private String model;
    private String color;

    public LineStatus getStatus() {
        return LineStatus.getStatusByValue(lineStatus);
    }

    public void setStatus(LineStatus status) {
        lineStatus = status.getValue();
    }


    public Line(Policy policy, LineStatus lineStatus, String model, String color) {
        linekey = new LineKey();
        linekey.setPolicy(policy);
        this.lineStatus = lineStatus.getValue();
        this.model = model;
        this.color = color;
    }
}
