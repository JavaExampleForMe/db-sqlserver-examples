package enteties;



import enums.LineStatus;
import lombok.*;
import lombok.experimental.Delegate;

import javax.persistence.*;
import java.time.LocalDateTime;


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

    public LineStatus getStatus() {
        return LineStatus.getStatusByValue(lineStatus);
    }

    public void setStatus(LineStatus status) {
        lineStatus = status.getValue();
    }


    public Line(Policy policy, LineStatus lineStatus) {
        linekey = new LineKey();
        linekey.setPolicy(policy);
        this.lineStatus = lineStatus.getValue();
    }
}
