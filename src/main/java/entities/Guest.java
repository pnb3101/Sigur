package entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
public class Guest extends Person {
    private OffsetDateTime visitTime;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Guest() {
        super();
        this.type = PersonType.GUEST;
    }
}
