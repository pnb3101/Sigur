package sigur.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Department {
    @Id
    @NotNull
    private Integer id;

    @Size(max = 32)
    private String name;
}
