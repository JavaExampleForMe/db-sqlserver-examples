package jdbc.tempalate;


import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TablA {
    private int id;
    private String name;
    private Date today;
}
