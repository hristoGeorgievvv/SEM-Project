package nl.tudelft.sem.sem54.fridge.repository;

import nl.tudelft.sem.sem54.fridge.domain.Fridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FridgeRepository extends JpaRepository<Fridge, Long> { }
