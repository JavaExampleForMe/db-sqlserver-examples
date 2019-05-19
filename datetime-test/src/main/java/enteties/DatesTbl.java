package enteties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
//@AllArgsConstructor
@NoArgsConstructor
public class DatesTbl {

 //   @Column(columnDefinition = "DATETIME", nullable = false)
    @Id
    private LocalDateTime dtGMTStartTime;

    private int SearchTaskId;
    private long iArchiveID;
    private int BatchId;

    public DatesTbl(LocalDateTime creationDate) {
        this.dtGMTStartTime = creationDate;
    }
}
