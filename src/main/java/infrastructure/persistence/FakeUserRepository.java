package infrastructure.persistence;

import application.interfaces.UserRepository;
import domain.users.User;
import domain.users.UserRole;
import java.util.Optional;

public class FakeUserRepository implements UserRepository {

    @Override
    public User save(User user) {
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if ("admin".equals(username)) {
            return Optional.of(new User("admin", "1234", UserRole.ADMIN));
        }
        return Optional.empty();
    }
}
