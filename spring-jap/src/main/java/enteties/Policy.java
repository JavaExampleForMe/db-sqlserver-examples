package enteties;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
public class Policy extends BaseEntity {

    public Policy(int mediaTypes, int deletionTypes) {
        this.mediaTypes = mediaTypes;
        this.deletionTypes = deletionTypes;
    }


    public Policy() {
    }

    @Column(name = "policyId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int policyId;

//    @JoinColumn(name = "policyId", referencedColumnName = "policyId")
//    @OneToMany(fetch = FetchType.EAGER)
//    private List<Line> lines;


    private int mediaTypes;

    private int deletionTypes;


}
