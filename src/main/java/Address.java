import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Address {

    String street;
    String number;
    @Id
    @GeneratedValue
    private Long id;

}
