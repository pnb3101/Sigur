package sigur.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
public class Employee extends Person {
    private OffsetDateTime hireTime;
    private OffsetDateTime firedTime;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Employee() {
        super();
        this.type = PersonType.EMPLOYEE;
    }
}
