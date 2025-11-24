package application.session;

import domain.users.User;
import java.util.Optional;

/**
 * Holds the information of the currently logged-in user.
 * Acts as a Singleton for the application lifecycle.
 */
public class SessionContext {

    private static SessionContext instance;
    private User currentUser;

    private SessionContext() {
    }

    public static synchronized SessionContext getInstance() {
        if (instance == null) {
            instance = new SessionContext();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public Optional<User> getCurrentUser() {
        return Optional.ofNullable(currentUser);
    }

    public void logout() {
        this.currentUser = null;
    }
}
