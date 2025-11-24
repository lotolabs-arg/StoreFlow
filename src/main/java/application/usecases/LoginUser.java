package application.usecases;

import application.interfaces.UserRepository;
import application.session.SessionContext;
import domain.common.DomainException;
import domain.users.User;

public class LoginUser {

    private final UserRepository userRepository;
    private final SessionContext sessionContext;

    public LoginUser(UserRepository userRepository, SessionContext sessionContext) {
        this.userRepository = userRepository;
        this.sessionContext = sessionContext;
    }

    public User execute(String username, String password) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new DomainException("Invalid credentials."));

        if (!user.authenticate(password)) {
            throw new DomainException("Invalid credentials.");
        }

        sessionContext.setCurrentUser(user);

        return user;
    }
}
