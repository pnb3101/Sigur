package sigur.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
public class Person {
    @Id
    @NotNull
    @Setter
    protected Integer id;

    @Size(max = 16)
    @Setter
    protected byte[] card;

    protected PersonType type;
}
