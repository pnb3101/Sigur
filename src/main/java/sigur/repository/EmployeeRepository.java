package sigur.repository;

import sigur.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    List<Employee> findAllByFiredTime(OffsetDateTime time);

    Boolean existsByCard(byte[] card);

    Optional<Employee> findByCard(byte[] card);
}
