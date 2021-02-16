package nl.tudelft.sem.sem54.fridge.service.base;

import java.util.Optional;
import nl.tudelft.sem.sem54.fridge.domain.Fridge;

public interface FridgeService {
    Fridge save(Fridge fridge);

    Optional<Fridge> getFridgeById(long id);

    Fridge getDefaultFridge();

    void delete(Fridge fridge);

}
