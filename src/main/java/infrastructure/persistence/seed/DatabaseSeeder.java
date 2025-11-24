package infrastructure.persistence.seed;

import application.interfaces.UserRepository;
import domain.users.User;
import domain.users.UserRole;
import java.util.logging.Logger;

public class DatabaseSeeder {

    private static final Logger LOGGER = Logger.getLogger(DatabaseSeeder.class.getName());
    private final UserRepository userRepository;

    public DatabaseSeeder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void seed() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User("admin", "1234", UserRole.ADMIN);
            userRepository.save(admin);
            LOGGER.info("SEED: Admin user created (admin / 1234)");
        } else {
            LOGGER.info("SEED: Previous database detected successfully.");
        }
    }
}
