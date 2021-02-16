package nl.tudelft.sem.sem54.fridge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import nl.tudelft.sem.sem54.fridge.domain.Fridge;
import nl.tudelft.sem.sem54.fridge.repository.FridgeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@ExtendWith(MockitoExtension.class)
public class FridgeServiceTest {

    @InjectMocks
    private FridgeServiceImpl fridgeService;

    @Spy
    private FridgeRepository fridgeRepository;

    private Fridge fridge;

    @BeforeEach
    void setUp() {
        fridge = mock(Fridge.class);
    }

    @Test
    public void save_test() {
        when(fridgeRepository.save(fridge)).thenReturn(fridge);
        assertEquals(fridgeService.save(fridge), fridge);
    }

    @Test
    public void get_fridge_by_Id_test() {
        long fridgeId = ThreadLocalRandom.current().nextLong();
        when(fridgeRepository.findById(fridgeId)).thenReturn(Optional.of(fridge));
        assertEquals(fridgeService.getFridgeById(fridgeId), Optional.of(fridge));
    }

    @Test
    public void get_default_fridge_test() {
        List<Fridge> fridgeList = new ArrayList<>();
        fridgeList.add(fridge);
        when(fridgeRepository.findAll()).thenReturn(fridgeList);
        assertEquals(fridgeService.getDefaultFridge(), fridge);
    }

    @Test
    public void delete_test() {
        fridgeService.delete(fridge);
        verify(fridgeRepository).delete(fridge);
    }

}
