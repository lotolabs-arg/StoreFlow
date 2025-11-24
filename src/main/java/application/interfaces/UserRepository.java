package application.interfaces;

import domain.users.User;
import java.util.Optional;

/**
 * Contract for User persistence operations.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(String id);

    /**
     * Finds a user by their unique username.
     * Used primarily for authentication.
     *
     * @param username the username to search for
     * @return an Optional containing the user if found
     */
    Optional<User> findByUsername(String username);
}
