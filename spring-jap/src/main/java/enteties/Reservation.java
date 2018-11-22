package enteties;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class Reservation extends BaseEntity {

    public Reservation(int mediaTypes, int orderTypes) {
        this.mediaTypes = mediaTypes;
        this.orderTypes = orderTypes;
    }


    public Reservation() {
    }

    @Column(name = "reservationId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int reservationId;

//    @JoinColumn(name = "orderId", referencedColumnName = "orderId")
//    @OneToMany(fetch = FetchType.EAGER)
//    private List<Line> lines;


    private int mediaTypes;

    private int orderTypes;


}
