package repository;

import entities.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GuestRepository extends JpaRepository<Guest, Integer> {
    Optional<Guest> findByCard(byte[] card);

    Boolean existsByCard(byte[] card);

    List<Guest> findAllByVisitTimeBetween(OffsetDateTime start, OffsetDateTime end);
}
