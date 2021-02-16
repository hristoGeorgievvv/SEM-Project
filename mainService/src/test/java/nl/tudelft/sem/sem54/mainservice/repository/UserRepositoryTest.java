package nl.tudelft.sem.sem54.mainservice.repository;

import static org.assertj.core.api.Assertions.assertThat;

import nl.tudelft.sem.sem54.mainservice.entities.UserEntity;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void testUpdateAllCredits_negativeCredits() {
        UserEntity user1 = new UserEntity("bob", -52f);
        UserEntity user2 = new UserEntity("rob", 102f);

        userRepository.save(user1);
        userRepository.save(user2);

        float newCredits = -10.0f;
        userRepository.updateAllCredits(newCredits);

        user1 = userRepository.findByUsername(user1.getUsername()).get();
        user2 = userRepository.findByUsername(user2.getUsername()).get();

        assertThat(user1.getCredits()).isEqualTo(newCredits);
        assertThat(user2.getCredits()).isEqualTo(newCredits);
    }

    @Test
    void testUpdateAllCredits_allToZero() {
        UserEntity user1 = new UserEntity("bob", -52f);
        UserEntity user2 = new UserEntity("rob", 102f);

        userRepository.save(user1);
        userRepository.save(user2);

        float newCredits = 0f;
        userRepository.updateAllCredits(newCredits);

        user1 = userRepository.findByUsername(user1.getUsername()).get();
        user2 = userRepository.findByUsername(user2.getUsername()).get();

        assertThat(user1.getCredits()).isEqualTo(newCredits);
        assertThat(user2.getCredits()).isEqualTo(newCredits);
    }

    @Test
    void testUpdateAllCredits_positiveCredits() {
        UserEntity user1 = new UserEntity("bob", -52f);
        UserEntity user2 = new UserEntity("rob", 102f);

        userRepository.save(user1);
        userRepository.save(user2);

        float newCredits = 10.2f;
        userRepository.updateAllCredits(newCredits);

        user1 = userRepository.findByUsername(user1.getUsername()).get();
        user2 = userRepository.findByUsername(user2.getUsername()).get();

        assertThat(user1.getCredits()).isEqualTo(newCredits);
        assertThat(user2.getCredits()).isEqualTo(newCredits);
    }
}