package nl.tudelft.sem.sem54.fridge.service;

import java.util.Optional;
import nl.tudelft.sem.sem54.fridge.domain.Fridge;
import nl.tudelft.sem.sem54.fridge.repository.FridgeRepository;
import nl.tudelft.sem.sem54.fridge.service.base.FridgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FridgeServiceImpl implements FridgeService {

    private FridgeRepository fridgeRepository;

    @Autowired
    FridgeServiceImpl(FridgeRepository fridgeRepository) {
        this.fridgeRepository = fridgeRepository;
    }

    @Override
    public Fridge save(Fridge fridge) {
        return fridgeRepository.save(fridge);
    }

    @Override
    public Optional<Fridge> getFridgeById(long id) {
        return fridgeRepository.findById(id);
    }

    @Override
    public Fridge getDefaultFridge() {
        return fridgeRepository.findAll().get(0);
    }

    @Override
    public void delete(Fridge fridge) {
        fridgeRepository.delete(fridge);
    }
}

