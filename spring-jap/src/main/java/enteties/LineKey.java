package enteties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@Data
@Embeddable
public class LineKey implements Serializable {


    public LineKey(int lineId, Reservation reservation) {
        this.lineId = lineId;
        this.reservation = reservation;
    }

    public LineKey() {
    }

    private int lineId;

    @JoinColumn(name = "reservationId", referencedColumnName = "reservationId")
    @ManyToOne(fetch = FetchType.EAGER)
    private Reservation reservation;

}
